/*
 *   © [2021] Cognizant. All rights reserved.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.cognizant.ciqdashboardapi.services;

import com.cognizant.ciqdashboardapi.errors.InvalidDetailsException;
import com.cognizant.ciqdashboardapi.models.*;
import com.cognizant.ciqdashboardapi.models.aggregate.GroupAggregate;
import com.cognizant.ciqdashboardapi.models.chart.data.*;
import com.cognizant.ciqdashboardapi.repos.SchedulerRunsRepository;
import com.cognizant.ciqdashboardapi.repos.DirectChartDataRepository;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.security.util.FieldUtils.getField;

/**
 * ChartAggregationComponent
 *
 * @author Cognizant
 */

@Component
public class ChartAggregationComponent {

    @Autowired
    CIQDashboardDataSourceService dataSourceService;
    @Autowired
    CollectorService collectorService;
    @Autowired
    ValidatorService validatorService;
    @Autowired
    DirectChartDataRepository directChartDataRepository;
    @Autowired
    SchedulerRunsRepository schedulerRunsRepository;

    public IDChartItemDataDTO getChartAggregation(IDChartItem chartItem,String dashboardId,String layerId, List<FilterConfig> filters) throws NoSuchFieldException, IllegalAccessException {
        Optional<Type.ChartItemType> type = Type.ChartItemType.getChartItemType(chartItem.getType());
        if (type.isPresent() && type.get() == Type.ChartItemType.COMBO) {
            List<ChartData> data = new ArrayList<>();
            Chart chart = new Chart();
           // LinkedChart linkedChart = new LinkedChart(chart,data);
           // List<LinkedData> linkedData = new ArrayList<>();
            IDChartItemDataDTO dataDTO = new IDChartItemDataDTO();
            Map<String, ComboChartGroup> comboGroupBy = chartItem.getComboGroupBy();
            if (CollectionUtils.isEmpty(comboGroupBy))
                throw new InvalidDetailsException("ComboGroupBy should be empty/null if type is 'combo'");
            comboGroupBy.entrySet().forEach(entry -> {
                IDChartItem item = new IDChartItem();
                BeanUtils.copyProperties(chartItem, item);
                ComboChartGroup combo = entry.getValue();
                item.setGroupBy(new ArrayList<>());
                if (!CollectionUtils.isEmpty(combo.getGroupBy())) item.setGroupBy(combo.getGroupBy());
                if (!CollectionUtils.isEmpty(combo.getFilters())) item.getFilters().addAll(combo.getFilters());
                item.setType(combo.getType());
                IDChartItemDataDTO result = null;
                try {
                    result = getChartAggregationResult(item,dashboardId,layerId, filters);
                } catch (NoSuchFieldException e) {
                    //e.printStackTrace();
                    System.err.println(e);
                } catch (IllegalAccessException e) {
                   // e.printStackTrace();
                    System.err.println(e);
                }
                data.add(new ChartData(entry.getKey(), null,entry.getKey(),null, (List<ChartData>) result.getData(), null));
                //linkedData.add(new LinkedData(entry.getKey(),linkedChart));
                item = null;
            });
            dataDTO.setChartItemDetails(chartItem);
            dataDTO.setData(data);
           // dataDTO.setLinkeddata(linkedData);
            return dataDTO;
        } else {
            return getChartAggregationResult(chartItem,dashboardId,layerId, filters);
        }
    }

    public IDChartItemDataDTO getChartAggregationResult(IDChartItem chartItem,String dashboardId,String layerId, List<FilterConfig> filters) throws NoSuchFieldException, IllegalAccessException {
        Optional<Type.ChartItemType> optional = Type.ChartItemType.getChartItemType(chartItem.getType());
        Type.ChartItemType chartItemType = optional.isPresent() ? optional.get() : Type.ChartItemType.PIE_CHART;
        Type.GenericChartItemType genericChartItemType = Type.getGenericChartItemType(chartItemType);
        if (CollectionUtils.isEmpty(chartItem.getGroupBy()) && null != chartItem.getAggregate()) {
            genericChartItemType = Type.GenericChartItemType.AGGREGATE;
        }

        CIQDashboardDataSource source = dataSourceService.assertAndGetByName(chartItem.getSource());
        Set<String> fields = collectorService.getFieldsByCollection(source.getCollectionName());

        if (CollectionUtils.isEmpty(chartItem.getFilters())) chartItem.setFilters(new ArrayList<>());
        if (CollectionUtils.isEmpty(chartItem.getGroupBy())) chartItem.setGroupBy(new ArrayList<>());

        Object chartData = null;
        List<LinkedData> linkedData = new ArrayList<>();
        IDChartItemDataDTO dataDTO = new IDChartItemDataDTO();
        boolean isRequired = false;
        switch (genericChartItemType) {
            case DRILL_DOWN_CHART:
                if (chartItem.getSource().equalsIgnoreCase("metrics")) {
                    System.out.println("inside drilldown -- ");
                    List<ChartData> chartData1 = getDrillDownChart(chartItem, fields, source.getCollectionName(), filters);
                    dataDTO.setChartItemDetails(chartItem);
                    dataDTO.setChartData(chartData1);

                }
                else {
                        chartData = getDrillDownChart(chartItem, fields, source.getCollectionName(), filters);
                       dataDTO.setData(chartData);
                }
                break;
            case FUSION_CHART2:
                    chartData = getFusionChart(chartItem, fields, source.getCollectionName(), filters);
                    dataDTO.setData(chartData);
                break;
            case FUSION_CHART:
                    chartData = getDrillDownChart(chartItem, fields, source.getCollectionName(), filters);
                    Object object1 = null;
                    Object object2 = null;
                    for (int i = 0; i < ((List<?>) chartData).size(); i++) {
                        Field field = getField(((List<?>) chartData).get(i).getClass(), "children");
                        Field field2 = getField(((List<?>) chartData).get(i).getClass(), "name");
                        object1 = (Object) field.get(((List<?>) chartData).get(i));
                        object2 = (Object) field2.get(((List<?>) chartData).get(i));
                        Chart chart = new Chart();
                        if (object2 != null) {
                            chart.setCaption("Priority:" + object2.toString());
                        }
                        chart.setTheme("fusion");
                        chart.setRotateValues("0");
                        chart.setPlottooltext("$label, $dataValue,  $percentValue");
                        LinkedChart linkedChart = new LinkedChart(chart, object1);
                        if (object2 != null) {
                            linkedData.add(new LinkedData(object2.toString().toLowerCase(), linkedChart));
                        } else {
                            linkedData.add(new LinkedData(null, linkedChart));
                        }
                    }
                    JSONObject dataObj = new JSONObject();
                    dataObj.put("data", chartData);
                    dataObj.put("linkeddata", linkedData);
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.add(dataObj);
                    dataDTO.setData(jsonArray);
                break;
            case FUSION_CHART_VERTICAL_GROUP:
                    chartData = getBarChart(chartItem, fields, source.getCollectionName(), filters);
                    Map<String,List<Value>> mappedvalues = new HashMap<>();
                    Categories categoriesData = new Categories();
                    ((List<ChartData>) chartData).stream().forEach(data->{
                        Map<String,Integer> value = new HashMap<>();
                        categoriesData.getCategory().add(
                                new Category().setLabel(data.getName())
                        );

                        var seriesDataMap = data.getSeries();
                        seriesDataMap.forEach(seriesData->{
                            var seriesName = seriesData.getName();
                            if(mappedvalues.get(seriesName) == null){
                                mappedvalues.put(seriesName,new LinkedList<>());
                            }
                            mappedvalues.get(seriesName).add(new Value().setValue((Integer) seriesData.getValue(), null));
                        });
                    });
                    List<Map> transformedData = mappedvalues.keySet().stream().sorted().map(key->{
                        Map<String,Object>  data = new LinkedHashMap<>();
                        data.put("seriesName",key);
                        data.put("data",mappedvalues.get(key));

                        return data;
                    }).collect(Collectors.toList());
                    StackedFusionChartData stackedFusionChartData = new StackedFusionChartData( "#chart_ui_data",List.of(categoriesData),transformedData);
                    //dataDTO.setData(stackedFusionChartData);
                    JSONArray jsonArray1 = new JSONArray();
                    jsonArray1.add(stackedFusionChartData);
                    dataDTO.setData(jsonArray1);
                break;
            case BAR_CHART:
                    chartData = getBarChart(chartItem, fields, source.getCollectionName(), filters);
                    dataDTO.setData(chartData);
                break;
            case BAR_GAUGE_CHART:
                    chartData = getBarGaugeChart(chartItem, fields, source.getCollectionName(), filters);
                    dataDTO.setData(chartData);
                break;
            case TABLE:
                    chartData = getTable(chartItem, fields, source.getCollectionName(), filters);
                    dataDTO.setData(chartData);
                break;
            case AGGREGATE:
                    chartData = getFieldsAggregate(chartItem, fields, source.getCollectionName(), filters);
                    dataDTO.setData(chartData);
                break;
            case NONE:
                chartData = new ArrayList<>();
                dataDTO.setData(chartData);
                break;
            case LINE:
            default:
                    chartData = getLineChart(chartItem, fields, source.getCollectionName(), filters);
                    dataDTO.setData(chartData);
                break;
        }
        if (!chartItem.getSource().equalsIgnoreCase("metrics")) {
            dataDTO.setChartItemDetails(chartItem);
           // dataDTO.setData(chartData);
        }
        return dataDTO;
    }

    private List<ChartData> getFieldsAggregate(IDChartItem chartItem, Set<String> fields, String collectionName, List<FilterConfig> filters1) {
        //List<FilterConfig> filters = chartItem.getFilters();
        List<FilterConfig> filters = filters1;
        GroupAggregate aggregate = chartItem.getAggregate();
        String title = (String) chartItem.getOptions().get("title");
        title = StringUtils.isEmpty(title) ? "" : title;
        aggregate.setName(title);
        Map<String, String> fieldsAndTypes = collectorService.getFieldsAndTypesByCollection(collectionName);
        GroupAggregate validateAggregate = validatorService.validateAggregate(aggregate, fields, fieldsAndTypes);
        List<ChartData> list = new ArrayList<>(collectorService.getGroupAggregate(filters, validateAggregate, fields, collectionName));
        if (list.size() > 0) {
            Collections.sort(list, new SortByName());
        }
        return list;
    }

    private List<ChartData> getLineChart(IDChartItem chartItem, Set<String> fields, String collectionName, List<FilterConfig> filters) {
        LineChartAggregation lineChartAggregation = new LineChartAggregation();
        if (CollectionUtils.isEmpty(chartItem.getGroupBy()))
            throw new InvalidDetailsException("GroupBy list should not be empty or null");
        lineChartAggregation.setGroupBy(chartItem.getGroupBy().get(0));
        //lineChartAggregation.setFilters(chartItem.getFilters());
        lineChartAggregation.setFilters(filters);
        DrillDownChartAggregation validateLineChartAggregation = validatorService.validateLineChartAggregation(lineChartAggregation.getChartAggregation(), fields);
        List<ChartData> list = new ArrayList<>(collectorService.getChart(validateLineChartAggregation, collectionName));
        if (list.size() > 0) {
            Collections.sort(list, new SortByName());
        }
        return list;
    }

    private List<DataGridRow> getTable(IDChartItem chartItem, Set<String> fields, String collectionName, List<FilterConfig> filters) {
        TableAggregation tableAggregation = new TableAggregation();
        //tableAggregation.setFilters(chartItem.getFilters());
        tableAggregation.setFilters(filters);
        tableAggregation.setProjection(chartItem.getProjection());
        Object maxRecords = chartItem.getOptions().get("maxRecords");
        if (Objects.nonNull(maxRecords) && maxRecords instanceof Integer) {
            tableAggregation.setLimit((Integer) maxRecords);
        }
        TableAggregation validateTableAggregation = validatorService.validateTableAggregation(tableAggregation, fields);
        return collectorService.getTable(validateTableAggregation, collectionName);
    }

    private List<ChartData> getBarChart(IDChartItem chartItem, Set<String> fields, String collectionName, List<FilterConfig> filters) {
        BarChartAggregation barChartAggregation = new BarChartAggregation();
        //barChartAggregation.setFilters(chartItem.getFilters());
        barChartAggregation.setFilters(filters);
        barChartAggregation.setGroupBy(chartItem.getGroupBy());
        BarChartAggregation validateBarChartAggregation = validatorService.validateBarChartAggregation(barChartAggregation, fields);
        List<ChartData> list = new ArrayList<>(collectorService.getBarChart(validateBarChartAggregation, collectionName));
        if (list.size() > 0) {
            Collections.sort(list, new SortByName());
        }
        return list;
    }

    private List<LinkedHashMap> getBarGaugeChart(IDChartItem chartItem, Set<String> fields, String collectionName, List<FilterConfig> filters) {
        BarChartAggregation barChartAggregation = new BarChartAggregation();
        //barChartAggregation.setFilters(chartItem.getFilters());
        barChartAggregation.setFilters(filters);
        BarChartAggregation validateBarGaugeChartAggregation = validatorService.validateBarGaugeChartAggregation(barChartAggregation, fields);
        return collectorService.getBarGaugeChart(validateBarGaugeChartAggregation, collectionName);
    }

    private List<ChartData> getDrillDownChart(IDChartItem chartItem, Set<String> fields, String collectionName, List<FilterConfig> filters) {
        DrillDownChartAggregation drillDownChartAggregation = new DrillDownChartAggregation();
        drillDownChartAggregation.setFilters(filters);
        drillDownChartAggregation.setGroupBy(chartItem.getGroupBy());
        DrillDownChartAggregation validateChartAggregation = validatorService.validateChartAggregation(drillDownChartAggregation, fields);
       List<ChartData> list = new ArrayList<>(collectorService.getChart(validateChartAggregation, collectionName));
       if (list.size() > 0) {
            Collections.sort(list, new SortByName());
        }
        return list;
    }

    private List<ChartDataForFusion> getFusionChart(IDChartItem chartItem, Set<String> fields, String collectionName, List<FilterConfig> filters) {
        DrillDownChartAggregation drillDownChartAggregation = new DrillDownChartAggregation();
        //drillDownChartAggregation.setFilters(chartItem.getFilters());
        drillDownChartAggregation.setFilters(filters);
        drillDownChartAggregation.setGroupBy(chartItem.getGroupBy());
        DrillDownChartAggregation validateChartAggregation = validatorService.validateChartAggregation(drillDownChartAggregation, fields);
        //List<ChartDataForFusion> list = collectorService.getChartForFusion(validateChartAggregation, collectionName);
        List<ChartDataForFusion> list = new ArrayList<>(collectorService.getChartForFusion(validateChartAggregation, collectionName));
       if (list.size() > 0) {
            // Collections<ChartData>.sort(list);
            Collections.sort(list, new SortByName());
        }
        return list;
    }

}
