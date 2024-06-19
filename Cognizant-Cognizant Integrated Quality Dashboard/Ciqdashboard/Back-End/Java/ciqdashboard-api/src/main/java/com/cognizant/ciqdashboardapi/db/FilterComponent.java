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

package com.cognizant.ciqdashboardapi.db;

import com.cognizant.ciqdashboardapi.models.Filter;
import com.cognizant.ciqdashboardapi.models.FilterConfig;
import com.cognizant.ciqdashboardapi.services.DateFilterComponent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.cognizant.ciqdashboardapi.models.Filter.OPType.*;
import static com.cognizant.ciqdashboardapi.models.FilterConfig.LogicalOperatorType.AND;
import static com.cognizant.ciqdashboardapi.models.FilterConfig.LogicalOperatorType.OR;

/**
 * FilterComponent
 * @author Cognizant
 */

@Component
public class FilterComponent {
    @Autowired
    DateFilterComponent dateFilterComponent;

    public List<Filter> getFilterList(List<FilterConfig> filters, FilterConfig.LogicalOperatorType type) {
        return filters.stream()
                .filter(config -> (config.getLogicalOperator() == type && (null!=config.getActive() && config.getActive())))
                .flatMap(filterConfig -> filterConfig.getConfigs().stream())
                .collect(Collectors.toList());
    }

    public List<AggregationOperation> getMatchOperation(List<FilterConfig> filters, Map<String, String> collection) {
        List<AggregationOperation> operations = new ArrayList<>();
        Map<String, String> dateFieldsMap = new HashMap<>();
        Set<String> dateFields = filters.stream().map(FilterConfig::getConfigs)
                .flatMap(Collection::stream)
                .filter(filter -> {
                    String type = collection.get(filter.getField());
                    return (StringUtils.hasText(type) && Date.class.getSimpleName().equalsIgnoreCase(type));
                })
                .map(Filter::getField)
                .collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(dateFields)) {
            dateFields.forEach(field -> dateFieldsMap.put(field, "customString" + field));
            operations.add(new AddFieldsOperation(dateFieldsMap));
        }
        Criteria criteria = getCriteria(filters, collection, dateFieldsMap);
        operations.add(Aggregation.match(criteria));
        return operations;
    }

    public Criteria getCriteria(List<FilterConfig> filters, Map<String, String> collection, Map<String, String> dateFieldsMap) {
        List<Filter> orFilter = getFilterList(filters, OR);
        List<Filter> andFilter = getFilterList(filters, AND);
        Criteria logicalCriteria = new Criteria();
        List<Criteria> list = getCriteriaList(orFilter, collection, dateFieldsMap);
        if (!CollectionUtils.isEmpty(list)) {
            Criteria[] criteriaArray = list.toArray(new Criteria[list.size()]);
            logicalCriteria.orOperator(criteriaArray);
        }
        list.clear();
        list = getCriteriaList(andFilter, collection, dateFieldsMap);
        if (!CollectionUtils.isEmpty(list)) {
            Criteria[] criteriaArray = list.toArray(new Criteria[list.size()]);
            logicalCriteria.andOperator(criteriaArray);
        }
        return logicalCriteria;
    }

    public List<Criteria> getCriteriaList(List<Filter> filters, Map<String, String> collection, Map<String, String> dateFieldsMap) {
        List<Criteria> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(filters)) return list;
        filters.forEach(filter -> {
            Criteria criteria = getCriteria(filter, collection, dateFieldsMap);
            if (null != criteria) list.add(criteria);
        });
        return list;
    }

    public Criteria getCriteria(Filter filterMain, Map<String, String> collection, Map<String, String> dateFieldsMap) {
        Filter filter = new Filter();
        BeanUtils.copyProperties(filterMain, filter);
        String type = collection.get(filter.getField());
        if (Date.class.getSimpleName().equalsIgnoreCase(type) && filter.getValue() instanceof Integer) {
            filter = dateFilterComponent.getMinAndMaxDate(filter);
        }
        Filter.OPType opType = filter.getOp();
        Object value = filter.getValue();
        Object maxValue = filter.getMaxValue();
        if (Date.class.getSimpleName().equalsIgnoreCase(type) && value instanceof String) {
            String field = dateFieldsMap.get(filter.getField());
            if (StringUtils.hasText(field)) filter.setField(dateFieldsMap.get(filter.getField()));
            Instant parse = Instant.parse((CharSequence) value);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            value = simpleDateFormat.format(Date.from(parse));
        }

        if (opType == between && Date.class.getSimpleName().equalsIgnoreCase(type) && maxValue instanceof String) {
            String field = dateFieldsMap.get(filter.getField());
            if (StringUtils.hasText(field)) filter.setField(dateFieldsMap.get(filter.getField()));
            Instant parse = Instant.parse((CharSequence) maxValue);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            maxValue = simpleDateFormat.format(Date.from(parse));
        }

        if ((opType == in || opType == nin)
                && value instanceof String
                && StringUtils.hasText(value.toString())
        ){
            value = new ArrayList<>(Arrays.asList(((String) value).split(",")));
        }

        switch (opType) {
            case eq:
            case equals:
                return Criteria.where(filter.getField()).is(value);
            case ne:
                return Criteria.where(filter.getField()).ne(value);
            case gt:
                return Criteria.where(filter.getField()).gt(value);
            case gte:
                return Criteria.where(filter.getField()).gte(value);
            case lt:
                return Criteria.where(filter.getField()).lt(value);
            case lte:
                return Criteria.where(filter.getField()).lte(value);
            case between:
                return Criteria.where(filter.getField()).gte(value).lt(maxValue);

            case in:
                if (value instanceof List)
                    return Criteria.where(filter.getField()).in((List) value);
                return new Criteria();
            case nin:
                if (value instanceof List)
                    return Criteria.where(filter.getField()).nin((List) value);
                return new Criteria();

            /* if value type is String then below cases is applicable.
             * if not, opType is below and value is not string type below scenario not applicable
             */
            case startswith:
                if (value instanceof String)
                    return Criteria.where(filter.getField()).regex(String.format("^%s", (String) value));
                return new Criteria();
            case endswith:
                if (value instanceof String)
                    return Criteria.where(filter.getField()).regex(String.format("%s$", (String) value));
                return new Criteria();
            case contains:
                if (value instanceof String)
                    return Criteria.where(filter.getField()).regex(String.format(".*%s*.", (String) value));
                return new Criteria();
            case notcontains:
                if (value instanceof String)
                    return Criteria.where(filter.getField()).regex(String.format("^(?:(?!%s).)*$", (String) value));
                return new Criteria();
            case regex:
            case matches:
                if (value instanceof String)
                    return Criteria.where(filter.getField()).regex((String) value);
                return new Criteria();

            default:
                return null;
        }
    }
}
