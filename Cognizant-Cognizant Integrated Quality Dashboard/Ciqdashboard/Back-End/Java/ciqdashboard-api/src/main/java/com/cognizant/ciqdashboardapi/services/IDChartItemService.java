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
import com.cognizant.ciqdashboardapi.errors.ResourceNotFoundException;
import com.cognizant.ciqdashboardapi.models.*;
import com.cognizant.ciqdashboardapi.models.chart.data.*;
import com.cognizant.ciqdashboardapi.repos.*;
import com.cognizant.ciqdashboardapi.repos.impl.IDChartItemRepositoryImpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.springframework.security.util.FieldUtils.getField;

/**
 * IDChartItemService
 *
 * @author Cognizant
 */

@Service
public class IDChartItemService {

    @Autowired
    IDChartItemRepository repository;
    @Autowired
    IDChartItemRepositoryImpl repositoryImpl;
    @Autowired
    ChartAggregationComponent chartAggregationComponent;
    @Autowired
    MetricsRepository metricRepo;

    @Autowired
    MetricConfigRepository metricConfigRepository;

    @Autowired
    CIQDashboardProjectRepository cIQDashboardProjectRepository;

    @Autowired
    CIQDashboardRepository ciqDashboardRepository;

    @Autowired
    DirectChartDataRepository directChartDataRepository;

    @Autowired
    SchedulerRunsRepository schedulerRunsRepository;


    public List<IDChartItem> getAll() {
        return repository.findAll();
    }

    public List<IDChartItem> getAllByCategory(String category) {

        return repository.findByCategory(category);
    }

    public Optional<IDChartItem> get(String id) {
        return repository.findById(id);
    }

    public IDChartItem assertAndGet(String id) {
        Optional<IDChartItem> optional = get(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ResourceNotFoundException("ChartItem", "id", id);
        }
    }

//    public IDChartItemDataDTO getChartData(String id, Optional<List<FilterConfig>> filters) {
//        IDChartItem chartItem = assertAndGet(id);
//        if (filters.isPresent()){
//            chartItem.getFilters().addAll(filters.get());
//        }
//
//        return chartAggregationComponent.getChartAggregation(chartItem);
//    }


    //public IDChartItemDataDTO getChartData(String id, String projectName, String dashboardName, String category, String lobId, String orgId, Optional<List<FilterConfig>> filters) throws Exception {

    public IDChartItemDataDTO getChartData(String id, String dashboardId, String category, String layerId,String pageId, Optional<List<FilterConfig>> filters) throws Exception {

        IDChartItem chartItem = assertAndGet(id);
        System.err.println("******************## Chart Item --- " + chartItem + " dashboardId - " + dashboardId+ " category - " + category+ " layerId - " + layerId);

        if (filters.isPresent()) {
            chartItem.getFilters().addAll(filters.get());
        }
        System.out.println("Filters : "+chartItem.getFilters());

       if (chartItem.getSource().equalsIgnoreCase("metrics")) {
           System.out.println("** Source is : "+chartItem.getSource());
           //IDChartItemDataDTO chartDTO = chartAggregationComponent.getChartAggregation(chartItem);
           //System.out.println("$$$$$$$$$$$$$$$$$$$$ - " + chartDTO);

           IDChartItemDataDTO dataDTO = null;

           if(layerId !=null && category!=null && dashboardId!=null) {
               dataDTO = getMetricData(id, layerId, dashboardId, category);
           }
           else{
               System.err.println("Check data is null : LayerId "+layerId+ " dashboardId "+dashboardId+" category "+category);
           }

           return dataDTO;
       }
       else {
           // return chartAggregationComponent.getChartAggregation(chartItem);
           return getDirectChartData(id, layerId, dashboardId,pageId,category);
        }
    }

    public DirectDataResponse calculateDirectChartData(String layerId, String dashboardId, String category) throws Exception {

       Optional<CIQDashboard> dashboard = ciqDashboardRepository.findByIdAndProjectId(dashboardId,layerId);

        if (dashboard.isPresent()) {

            System.out.println("Dashboard exists with the name : "+dashboard.get().getName());
            CIQDashboard dashboardResult = dashboard.get();
            List<DirectChartData> metrics = new ArrayList<>();
            DirectDataResponse directDataResponse = new DirectDataResponse();
            if(dashboard.get().getPages()!=null && dashboard.get().getPages().size()>0) {
                for (IDPageConfig page : dashboard.get().getPages()) {

                    List<IDItemConfig> directChartItemList = new ArrayList<>();
                    System.out.println("** Page : "+page.getName());
                    for (IDItemConfig item : page.getItems()) {
                        if (!("derived".equalsIgnoreCase(item.getMetricCategory()))) {
                            IDChartItem chartItem = assertAndGet(item.getId());
                            chartItem.setFilters(item.getFilters());
                            directChartItemList.add(item);
                        }
                    }

                    //System.out.println("ProjectMetric --------------- " + projectMetric);
                    for (IDItemConfig item : directChartItemList) {
                        boolean isRequired = false;
                        IDChartItem chartItem = assertAndGet(item.getId());
                        isRequired = isCalculationRequiredForRawTypeCharts(chartItem.getId(),dashboardId,layerId, page.getPageId(),chartItem.getSource());
                        if (isRequired) {
                            IDChartItemDataDTO dataDTO = new IDChartItemDataDTO();
                            if (category.equalsIgnoreCase("lob")) {
                                System.out.println("ID chart item LOB");
                                dataDTO = chartAggregationComponent.getChartAggregation(chartItem,dashboardId,layerId,item.getFilters());
                                System.out.println("Chart Data DTO "+dataDTO);
                                Object chartData = null;
                                if (chartItem.getType().equalsIgnoreCase("bar-vertical-chart-fusion")) {
                                    List<CIQDashboardProject> cIQDashboardProject = cIQDashboardProjectRepository.findByLobId(layerId);
                                    chartData = dataDTO.getData();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(chartData);
                                    System.out.println("json" + json);
                                    for (int j = 0; j < cIQDashboardProject.size(); j++) {
                                        List<CIQDashboard> ciqDashboard = ciqDashboardRepository.findByProjectName(cIQDashboardProject.get(j).getName());
                                        if (ciqDashboard.size() != 0) {
                                            //String dashboardId = ciqDashboard.get(0).getId();
                                            // String link = projectUrl+"/ciqdashboard/#/ciqdashboard/" + cIQDashboardProject.get(j).getId() + "/dashboards/" + dashboardId+";page=0?category=project";
                                            //  String link = "http://10.120.100.97/ciqdashboard/#/ciqdashboard/" + cIQDashboardProject.get(j).getId() + "/dashboards/" + dashboardId+";page=0?category=project";
                                            //String link = "http://10.120.100.95/ciqdashboard/#/ciqdashboard/" + cIQDashboardProject.get(j).getId() + "/dashboards/" + dashboardId+";page=0?category=project";
                                            //String link = cIQDashboardProject.get(j).getId() + "/dashboards/" + dashboardId + ";page=0?category=project";
                                            String link = cIQDashboardProject.get(j).getId() + "/dashboards/" + dashboardId + ";page=0?category=project";

                                            System.out.println("linkforTest" + link);
                                            json = replaceFirstOccurrenceOfString(json, "#url#", link);
                                            System.out.println("replacejson" + json);
                                        }
                                    }
                                    //  return idChartItemDataDTO;
                                    final GsonBuilder gsonBuilder = new GsonBuilder();
                                    final Gson gson1 = gsonBuilder.create();
                                    ChartDataForFusion[] chartDataForFusions = gson1.fromJson(json, ChartDataForFusion[].class);
                                    dataDTO.setData(chartDataForFusions);
                                }
                            }else{
                                dataDTO = chartAggregationComponent.getChartAggregation(chartItem,dashboardId,layerId, item.getFilters());
                            }
                            if (chartItem.getId() != null) {
                                Optional<DirectChartData> directChartData1 = directChartDataRepository.findByItemIdAndDashboardIdAndLayerIdAndPageId(chartItem.getId(),dashboardId,layerId,page.getPageId());
                                if (directChartData1.isPresent()) {
                                    DirectChartData directChartData2 = directChartData1.get();
                                    directChartData2.setItemId(chartItem.getId());
                                    directChartData2.setChartValues(dataDTO.getData());
                                    directChartData2.setLastCalculatedDate(Instant.now());
                                    directChartData2.setGroupBy(chartItem.getGroupBy());
                                    directChartData2.setFilters(item.getFilters());
                                    directChartData2.setDashboardId(dashboardId);
                                    directChartData2.setLayerId(layerId);
                                    directChartData2.setLayerName(chartItem.getCategory());
                                    directChartData2.setPageId(page.getPageId());
                                    directChartDataRepository.save(directChartData2);
                                } else {
                                    DirectChartData rawData = new DirectChartData();
                                    rawData.setItemId(chartItem.getId());
                                    rawData.setChartValues(dataDTO.getData());
                                    rawData.setLastCalculatedDate(Instant.now());
                                    rawData.setCreatedOn(Instant.now());
                                    rawData.setGroupBy(chartItem.getGroupBy());
                                    rawData.setDashboardId(dashboardId);
                                    rawData.setFilters(item.getFilters());
                                    rawData.setLayerId(layerId);
                                    rawData.setLayerName(chartItem.getCategory());
                                    rawData.setPageId(page.getPageId());
                                    directChartDataRepository.save(rawData);
                                }

                            }
                        }else {
                            Optional<DirectChartData> directChartData1 = directChartDataRepository.findByItemIdAndDashboardIdAndLayerIdAndPageId(chartItem.getId(),dashboardId,layerId,page.getPageId());

                            if (directChartData1.isPresent()) {
                                boolean groupflag =false;
                                boolean filterflag =false;
                                Integer rawDataGroupSize = directChartData1.get().getGroupBy().size();
                                Integer chartItemGroupSize = chartItem.getGroupBy().size();
                               // if(rawDataGroupSize != chartItemGroupSize){
                                if(!(rawDataGroupSize.equals(chartItemGroupSize))){
                                    groupflag = true;
                                }else{
                                    for(int i=0;i<rawDataGroupSize; i++) {
                                        if (!directChartData1.get().getGroupBy().get(i).equals(chartItem.getGroupBy().get(i))) {
                                            groupflag = true;
                                        }
                                    }
                                }
                                if(directChartData1.get().getFilters() == null && chartItem.getFilters().size() !=0) {
                                    filterflag =true;
                                }else if(directChartData1.get().getFilters() != null){
                                    Integer rawDataFilterSize = directChartData1.get().getFilters().size();
                                    Integer chartItemFilterSize = item.getFilters().size();
                                   // if(rawDataFilterSize != chartItemFilterSize){
                                    if(!(rawDataFilterSize.equals(chartItemFilterSize))){
                                        filterflag =true;
                                    }else{
                                        for(int i=0;i<chartItemFilterSize; i++) {
                                            if(item.getFilters().get(i).getActive() == true && directChartData1.get().getFilters().get(i).getActive() == true) {
                                                if (!directChartData1.get().getFilters().get(i).getConfigs().get(0).getField().equals(item.getFilters().get(0).getConfigs().get(0).getField())) {
                                                    filterflag = true;
                                                }
                                                if (!directChartData1.get().getFilters().get(i).getConfigs().get(0).getValue().equals(item.getFilters().get(0).getConfigs().get(0).getValue())) {
                                                    filterflag = true;
                                                }
                                                if (!directChartData1.get().getFilters().get(i).getConfigs().get(0).getOp().equals(item.getFilters().get(0).getConfigs().get(0).getOp())) {
                                                    filterflag = true;
                                                }
                                                if (!directChartData1.get().getFilters().get(i).getConfigs().get(0).getMaxValue().equals(item.getFilters().get(0).getConfigs().get(0).getMaxValue())) {
                                                    filterflag = true;
                                                }
                                            }else{
                                                filterflag = true;
                                            }
                                        }

                                    }

                                }
                                if(groupflag || filterflag)
                                {
                                    IDChartItemDataDTO dataDTO = new IDChartItemDataDTO();
                                    dataDTO = chartAggregationComponent.getChartAggregation(chartItem,dashboardId,layerId, item.getFilters());
                                    Optional<DirectChartData> directChartData3 = directChartDataRepository.findByItemIdAndDashboardIdAndLayerIdAndPageId(chartItem.getId(),dashboardId,layerId,page.getPageId());
                                    if (directChartData3.isPresent()) {
                                        DirectChartData directChartData2 = directChartData3.get();
                                        directChartData2.setItemId(chartItem.getId());
                                        directChartData2.setChartValues(dataDTO.getData());
                                        directChartData2.setLastCalculatedDate(Instant.now());
                                        directChartData2.setGroupBy(chartItem.getGroupBy());
                                        directChartData2.setFilters(item.getFilters());
                                        directChartData2.setDashboardId(dashboardId);
                                        directChartData2.setLayerId(layerId);
                                        directChartData2.setLayerName(chartItem.getCategory());
                                        directChartData2.setPageId(page.getPageId());
                                        directChartDataRepository.save(directChartData2);
                                    }
                                }
                            }
                        }
                        // dataDTO.setData(chartData);

                    }
                }
            }
            return directDataResponse;
        } else {
            throw new Exception("dashboard not exists");
        }
    }


    public IDChartItemDataDTO getDirectChartData(String itemId, String layerId, String dashboardId, String pageId, String category) throws Exception {

        IDChartItem chartItem = assertAndGet(itemId);
        IDChartItemDataDTO dataDTO = new IDChartItemDataDTO();
        System.out.println("chartItem -*-*-*- " + chartItem);
        Optional<DirectChartData> directChartData1 = directChartDataRepository.findByItemIdAndDashboardIdAndLayerIdAndPageId(chartItem.getId(),dashboardId,layerId,pageId);

        if(directChartData1.isPresent()){
            dataDTO.setData(directChartData1.get().getChartValues());
        }else{
            dataDTO.setData(null);
        }

        dataDTO.setChartItemDetails(chartItem);
        return dataDTO;
    }


    public boolean isCalculationRequiredForRawTypeCharts(String itemId,String dashboardId,String layerId,String pageId, String toolName) {
        //System.out.println("metricName - " + metricName + " - dashboardName - " + projectMetric.getDashboardName() + " - itemId - " + projectMetric.getItemId());
        Optional<DirectChartData> sourceRawTypeDataResults = directChartDataRepository.findByItemIdAndDashboardIdAndLayerIdAndPageId(itemId,dashboardId,layerId,pageId);
        Optional<CollectorRunScheduler> collectorRunScheduler = schedulerRunsRepository.findByToolNameIgnoreCase(toolName);

        boolean requireCalculation = true;
        if (collectorRunScheduler.isPresent() && sourceRawTypeDataResults.isPresent()) {

            Instant lastCollectionUpdated = collectorRunScheduler.get().getLastUpdatedDate();
            Instant lastMetricCalculated = sourceRawTypeDataResults.get().getLastCalculatedDate();

            System.out.println("Check metric calculation happened before or after the updated collections");
            System.out.println("last collections updated "+ lastCollectionUpdated);
            System.out.println("last metric calculated "+ lastMetricCalculated);

            requireCalculation = lastCollectionUpdated.isAfter(lastMetricCalculated);

        } else if (!collectorRunScheduler.isPresent()) {
            System.out.println("collectorRunScheduler is not present for the metric tool- " + toolName);
            requireCalculation = false;

        } else if (!sourceRawTypeDataResults.isPresent()) {
            requireCalculation = true;
        }
        System.out.println("Is calculation required : "+ requireCalculation);
        return requireCalculation;
    }

    public static String replaceFirstOccurrenceOfString(String input, String stringToReplace,
                                                        String stringToReplaceWith) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        if (stringToReplace == null || stringToReplace.isEmpty() || stringToReplaceWith == null
                || stringToReplaceWith.isEmpty()) {
            return input;
        }
        String result = input.replaceFirst(Pattern.quote(stringToReplace), stringToReplaceWith);
        return result;
    }


    public List<IDChartItem> searchByNames(List<String> names) {
        return repositoryImpl.findByNameLikeIgnoreCase(names);
    }

    public IDChartItemDataDTO preview(IDChartItem chartItem) throws NoSuchFieldException, IllegalAccessException {
        assertAndGetChartItemType(chartItem.getType());
        return chartAggregationComponent.getChartAggregation(chartItem,chartItem.getDashboardId(),chartItem.getLayerId(), chartItem.getFilters());
    }

    public IDChartItem add(IDChartItem chartItem) {
        assertInsert(chartItem);
        assertAndGetChartItemType(chartItem.getType());
        if (chartItem.getSource().equalsIgnoreCase("metrics")) {
//            chartItem.setMetricCategory("derived");
//            if (chartItem.getMetricName() == null || chartItem.getMetricName().isEmpty()) {
//                throw new InvalidDetailsException(String.format("Empty/blank Metric Name", chartItem.getMetricName()));
//            }
        }
        return repository.insert(chartItem);
    }

    public IDChartItem update(IDChartItem chartItem) {
        assertAndGet(chartItem.getId());
        assertAndGetChartItemType(chartItem.getType());
        return repository.save(chartItem);
    }

    public void deleteById(String id) {
        assertAndGet(id);
        repository.deleteById(id);
        directChartDataRepository.deleteByItemId(id);
    }

    public void deleteByIdIn(List<String> ids) {
        repository.deleteByIdIn(ids);
    }

    private void assertInsert(IDChartItem chartItem) {
        if (!StringUtils.isEmpty(chartItem.getId())) {
            throw new InvalidDetailsException("Id should be null/empty");
        }
    }

    private Type.ChartItemType assertAndGetChartItemType(String type) {
        Optional<Type.ChartItemType> optional = Type.ChartItemType.getChartItemType(type);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new InvalidDetailsException(String.format("invalid chart type(%s) provided", type));
        }
    }


//    public IDChartItemDataDTO getMetricData(String itemId, String metricName, String grouping, String trending, String customFunction, String customFunctionName, String layerId, String dashboardId) throws Exception {
    public IDChartItemDataDTO getMetricData(String itemId, String layerId, String dashboardId, String category) throws Exception {

        IDChartItem chartItem = assertAndGet(itemId);
        System.out.println("chartItem -*-*-*- " + chartItem);

        List<ChartData> data = new ArrayList<>();
        List<ChartDataTrending> dataTrending = new ArrayList<>();
        IDChartItemDataDTO dataDTO = new IDChartItemDataDTO();
        List<LinkedData> linkedData = new ArrayList<>();

        Optional<MetricConfig> metricConfig = metricConfigRepository.findByMetricName(chartItem.getMetricName());
        System.err.println("$$$$$$$$$$$ - " + metricConfig);
        if (metricConfig.isPresent()) {
            MetricConfig metric = metricConfig.get();
            String metricName = metric.getMetricName();
            String grouping = metric.getGrouping();
            String trending = metric.getTrending();
            String customFunction = metric.getCustomFunction();
            String customFunctionName = metric.getCustomFunctionName();

            //Optional<SourceMetrics> metrics1 = metricRepo.findByProjectNameAndDashboardNameAndItemIdAndMetricName(projectName, dashboardName, itemId, metricName);
            Optional<SourceMetrics> metrics1 = metricRepo.findByLayerIdAndDashboardIdAndItemIdAndMetricName(layerId, dashboardId, itemId, metricName);
            System.out.println("metrics is present : " + metrics1.isPresent());

            if (metrics1.isPresent()) {
                SourceMetrics metrics = metrics1.get();
                System.out.println("Source metrics fetched is : " + metrics);
                if ("yes".equalsIgnoreCase(grouping)) {
                    if (chartItem.getType().equalsIgnoreCase("bar-vertical-chart-fusion")) {
                        String calculatedResults = metrics.getMetricValues().toJSONString();
                        JSONArray array = (JSONArray) new JSONParser().parse(calculatedResults);
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject result = (JSONObject) array.get(i);
                            String linkValue = null;
                            if (result.get("link") != null) {
                                linkValue = result.get("link").toString();
                            } else {
                                linkValue = "";
                            }
                            data.add(new ChartData(result.get("groupName").toString(), result.get("result"), result.get("groupName").toString(), linkValue, null, null));
                        }
                        if (data.size() > 0) {
                            Collections.sort(data, new SortByName());
                        }
                        dataDTO.setChartItemDetails(chartItem);
                        dataDTO.setData(data);
                    }
                    else {
                        String calculatedResults = metrics.getMetricValues().toJSONString();
                        JSONArray array = (JSONArray) new JSONParser().parse(calculatedResults);
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject result = (JSONObject) array.get(i);
                            data.add(new ChartData(result.get("groupName").toString(), result.get("result"), null, null, null, null));
                        }
                        if (data.size() > 0) {
                            Collections.sort(data, new SortByName());
                        }

                        dataDTO.setChartItemDetails(chartItem);
                        dataDTO.setData(data);
                    }
                } else if ("yes".equalsIgnoreCase(trending)) {
                    List<TrendingMetric> calculatedResults = metrics.getTrendingMetricValues();
                    SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                    calculatedResults.forEach(calculatedResult -> {
                        Date month = calculatedResult.getMonth();
                        Integer result = calculatedResult.getResult();
                        SimpleDateFormat outputDateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
                        String outputDateString = outputDateFormat.format(month);
                        dataTrending.add(new ChartDataTrending(outputDateString, result));
                    });
                    dataDTO.setChartItemDetails(chartItem);
                    dataDTO.setData(dataTrending);
                } else if ("yes".equalsIgnoreCase(customFunction)) {
                    String calculatedResults = metrics.getMetricValues().toJSONString();
                    JSONArray array = (JSONArray) new JSONParser().parse(calculatedResults);
                    if (customFunctionName.equalsIgnoreCase("jira_defect_ageing") || customFunctionName.equalsIgnoreCase("jira_defect_ageing_by_drops") || customFunctionName.equalsIgnoreCase("jira_defect_ageing_by_drops_all_status")) {
                        System.out.println("DEFECT AGEING FUNCTION ** ");
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject result = (JSONObject) array.get(i);
                            data.add(new ChartData(result.get("name").toString(), result.get("value"), result.get("label").toString(), null, null, (List<ChartData>) result.get("series")));
                        }
                        if (data.size() > 0) {
                            Collections.sort(data, new SortByName());
                        }
                        dataDTO.setChartItemDetails(chartItem);
                        dataDTO.setData(data);
                    } else if (customFunctionName.equalsIgnoreCase("jira_open_closed_defect_count")) {
                        System.out.println("JIRA OPEN CLOSED DEFECT COUNT ** ");
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject result = (JSONObject) array.get(i);
                            data.add(new ChartData(result.get("name").toString(), result.get("value"), result.get("label").toString(), null, null, null));
                        }
                        if (data.size() > 0) {
                            Collections.sort(data, new SortByName());
                        }
                        dataDTO.setChartItemDetails(chartItem);
                        dataDTO.setData(data);
                    }else if (customFunctionName.equalsIgnoreCase("custom_stc_manual")) {
                        System.out.println("CUSTOM STC MANUAL ** ");
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject result = (JSONObject) array.get(i);
                            data.add(new ChartData(result.get("name").toString(), result.get("value"), result.get("label").toString(), null, null, null));
                        }
                        if (data.size() > 0) {
                            Collections.sort(data, new SortByName());
                        }
                        dataDTO.setChartItemDetails(chartItem);
                        dataDTO.setData(data);
                    }else if (customFunctionName.equalsIgnoreCase("jira_defect_ageing_by_priority")) {
                        System.out.println("JIRA DEFECT AGEING BY PRIORITY ** ");
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject result = (JSONObject) array.get(i);
                            data.add(new ChartData(result.get("name").toString(), result.get("value"), result.get("label").toString(), null, null, null));
                        }
                        if (data.size() > 0) {
                            Collections.sort(data, new SortByName());
                        }
                        dataDTO.setChartItemDetails(chartItem);
                        dataDTO.setData(data);
                    }else if (customFunctionName.equalsIgnoreCase("custom_table_stc")) {
                        System.out.println("CUSTOM TABLE STC** ");
                        JSONArray array1 = metrics.getMetricValues();
                        dataDTO.setChartItemDetails(chartItem);
                        dataDTO.setData(array1);
                    }else if (customFunctionName.equalsIgnoreCase("stacked_type_ngx")) {
                        System.out.println("STACKED TYPE NGX ** ");
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject result = (JSONObject) array.get(i);
                            data.add(new ChartData(result.get("name").toString(), result.get("value"), result.get("label").toString(), null, null, (List<ChartData>) result.get("series")));
                        }
                        if (data.size() > 0) {
                            Collections.sort(data, new SortByName());
                        }
                        dataDTO.setChartItemDetails(chartItem);
                        dataDTO.setData(data);
                    }
                    else if (customFunctionName.equalsIgnoreCase("stacked_type_fusion")) {
                        System.out.println("STACKED TYPE FUSION ** ");
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject result = (JSONObject) array.get(i);
                            data.add(new ChartData(result.get("name").toString(), result.get("value"), result.get("label").toString(), null, null, (List<ChartData>) result.get("series")));
                        }
                        if (data.size() > 0) {
                            Collections.sort(data, new SortByName());
                        }
                        Map<String, List<Value>> mappedvalues = new HashMap<>();
                        Categories categoriesData = new Categories();
                        for (ChartData data2 : data) {
                            Map<String, Integer> value = new HashMap<>();
                            categoriesData.getCategory().add(
                                    new Category().setLabel(data2.getName())
                            );

                            ListIterator seriesDataMap = data2.getSeries().listIterator();
                            while (seriesDataMap.hasNext()) {
                                JSONObject seriesData = (JSONObject) seriesDataMap.next();
                                String seriesName = seriesData.get("name").toString();
                                if (mappedvalues.get(seriesName) == null) {
                                    mappedvalues.put(seriesName, new LinkedList<>());
                                }
                                if (seriesData.get("link") != null) {
                                    mappedvalues.get(seriesName).add(new Value().setValue(Integer.valueOf(seriesData.get("value").toString()), seriesData.get("link").toString()));
                                } else {
                                    mappedvalues.get(seriesName).add(new Value().setValue(Integer.valueOf(seriesData.get("value").toString()), ""));
                                }
                            }
                            ;
                        }
                        ;
                        List<Map> transformedData = mappedvalues.keySet().stream().sorted().map(key -> {
                            Map<String, Object> data3 = new LinkedHashMap<>();
                            data3.put("seriesName", key);
                            data3.put("data", mappedvalues.get(key));
                            return data3;
                        }).collect(Collectors.toList());
                        StackedFusionChartData stackedFusionChartData = new StackedFusionChartData("#chart_ui_data", List.of(categoriesData), transformedData);
                        JSONArray jsonArray1 = new JSONArray();
                        jsonArray1.add(stackedFusionChartData);
                        dataDTO.setChartItemDetails(chartItem);
                        dataDTO.setData(jsonArray1);
                    }
                    else if (customFunctionName.equalsIgnoreCase("drilldown_fusion") || customFunctionName.equalsIgnoreCase("jira_defect_ageing_per_dev")) {
                        System.out.println("Drilldown fusion ** ");
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject result = (JSONObject) array.get(i);
                            data.add(new ChartData(result.get("name").toString(), result.get("value"), result.get("label").toString(), null, (List<ChartData>) result.get("series"), null));
                        }
                        if (data.size() > 0) {
                            Collections.sort(data, new SortByName());
                        }
                        Object object1 = null;
                        Object object2 = null;
                        for (int i = 0; i < ((List<?>) data).size(); i++) {
                            Field field = getField(((List<?>) data).get(i).getClass(), "children");
                            Field field2 = getField(((List<?>) data).get(i).getClass(), "name");
                            object1 = (Object) field.get(((List<?>) data).get(i));
                            object2 = (Object) field2.get(((List<?>) data).get(i));
                            Chart chart = new Chart();
                            if (object2 != null) {
                                chart.setCaption("TIME PERIOD : " + object2.toString());
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
                        net.minidev.json.JSONObject dataObj = new net.minidev.json.JSONObject();
                        dataObj.put("data", data);
                        dataObj.put("linkeddata", linkedData);
                        net.minidev.json.JSONArray jsonArray = new net.minidev.json.JSONArray();
                        jsonArray.add(dataObj);
                        dataDTO.setChartItemDetails(chartItem);
                        dataDTO.setData(jsonArray);
                    }
                } else {
                    data.add(new ChartData(metrics.getMetricName(), metrics.getMetricValue(), null, null, null, null));
                    if (data.size() > 0) {
                        Collections.sort(data, new SortByName());
                    }
                    dataDTO.setChartItemDetails(chartItem);
                    dataDTO.setData(data);
                }
            }
        }
        System.out.println(dataDTO);
        return dataDTO;
    }


    /*
    public IDChartItemDataDTO getMetricDataByLOB(String id, String lobId, ChartData data1) {
        IDChartItem chartItem = assertAndGet(id);
        System.out.println("chartItem -*-*-*- " + chartItem);
        List<ChartData> data = new ArrayList<>();
        IDChartItemDataDTO dataDTO = new IDChartItemDataDTO();
        List<SourceMetrics> metrics = metricRepo.findByLobIdAndMetricName(lobId, chartItem.getMetricName());
        System.out.println("metrics -*-*-*- " + metrics);
        for (SourceMetrics metric : metrics) {
            data.add(new ChartData(metric.getProjectName(), metric.getMetricValue(), data1.getChildren(), data1.getSeries()));
        }
        dataDTO.setChartItemDetails(chartItem);
        dataDTO.setData(data);
        return dataDTO;
    }
    public IDChartItemDataDTO getMetricDataByORG(String id, String orgId, ChartData data1) {
        IDChartItem chartItem = assertAndGet(id);
        System.out.println("chartItem -*-*-*- " + chartItem);
        List<ChartData> data = new ArrayList<>();
        IDChartItemDataDTO dataDTO = new IDChartItemDataDTO();
        List<SourceMetrics> metrics = metricRepo.findByOrgIdAndMetricName(orgId, chartItem.getMetricName());
        System.out.println("metrics -*-*-*- " + metrics);
        for (SourceMetrics metric : metrics) {
            data.add(new ChartData(metric.getProjectName(), metric.getMetricValue(), data1.getChildren(), data1.getSeries()));
        }
        dataDTO.setChartItemDetails(chartItem);
        dataDTO.setData(data);
        return dataDTO;
    }
    */

}
