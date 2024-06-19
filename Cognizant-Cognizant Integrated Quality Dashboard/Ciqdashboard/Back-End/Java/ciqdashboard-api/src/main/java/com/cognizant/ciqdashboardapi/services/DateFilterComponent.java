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

import com.cognizant.ciqdashboardapi.models.Filter;
import com.cognizant.ciqdashboardapi.models.Type;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * DateFilterComponent
 * @author Cognizant
 */

@Component
public class DateFilterComponent {

    public Filter getMinAndMaxDate(Filter filter) {
        Calendar calendar = getTodayDate();
        int value = (int) filter.getValue();
        switch (filter.getOp()) {
            case thisYear:
                Date firstDayOfCurrentYear = firstDayOfCurrentYearOrMonthOrWeek(calendar, Type.DateType.YEAR);
                filter.setValue(firstDayOfCurrentYear.toInstant().toString());
                filter.setMaxValue(calendar.getTime().toInstant().toString());
                filter.setOp(Filter.OPType.between);
                break;
            case thisMonth:
                Date firstDayOfCurrentMonth = firstDayOfCurrentYearOrMonthOrWeek(calendar, Type.DateType.MONTH);
                filter.setValue(firstDayOfCurrentMonth.toInstant().toString());
                filter.setMaxValue(calendar.getTime().toInstant().toString());
                filter.setOp(Filter.OPType.between);
                break;
            case thisWeek:
                Date firstDayOfCurrentWeek = firstDayOfCurrentYearOrMonthOrWeek(calendar, Type.DateType.WEEK);
                filter.setValue(firstDayOfCurrentWeek.toInstant().toString());
                filter.setMaxValue(calendar.getTime().toInstant().toString());
                filter.setOp(Filter.OPType.between);
                break;
            case thisDay:
                filter.setValue(calendar.getTime().toInstant());
                filter.setOp(Filter.OPType.equals);
                break;
            case lastNYear:
                Date firstDayOfNthYear = firstDayOfNthYearOrMonthOrWeekOrDay(calendar, Type.DateType.YEAR, value);
                filter.setValue(firstDayOfNthYear.toInstant().toString());
//                Date lastDayOfLastYear = lastDayOfLastYearOrMonthOrWeek(calendar, YEAR);
//                filter.setMaxValue(lastDayOfLastYear.toInstant().toString());
                filter.setMaxValue(calendar.getTime().toInstant().toString());
                filter.setOp(Filter.OPType.between);
                break;
            case lastNMonth:
                Date firstDayOfNthMonth = firstDayOfNthYearOrMonthOrWeekOrDay(calendar, Type.DateType.MONTH, value);
                filter.setValue(firstDayOfNthMonth.toInstant().toString());
//                Date lastDayOfLastMonth = lastDayOfLastYearOrMonthOrWeek(calendar, MONTH);
//                filter.setMaxValue(lastDayOfLastMonth.toInstant().toString());
                filter.setMaxValue(calendar.getTime().toInstant().toString());
                filter.setOp(Filter.OPType.between);
                break;
            case lastNWeek:
                Date firstDayOfNthWeek = firstDayOfNthYearOrMonthOrWeekOrDay(calendar, Type.DateType.WEEK, value);
                filter.setValue(firstDayOfNthWeek.toInstant().toString());
//                Date lastDayOfLastWeek = lastDayOfLastYearOrMonthOrWeek(calendar, WEEK);
//                filter.setMaxValue(lastDayOfLastWeek.toInstant().toString());
                filter.setMaxValue(calendar.getTime().toInstant().toString());
                filter.setOp(Filter.OPType.between);
                break;
            case lastNDay:
                Date firstDayOfNthDay = firstDayOfNthYearOrMonthOrWeekOrDay(calendar, Type.DateType.DAY, value);
                filter.setValue(firstDayOfNthDay.toInstant().toString());
                filter.setMaxValue(calendar.getTime().toInstant().toString());
                filter.setOp(Filter.OPType.between);
                break;
            default:
                break;
        }
        return filter;
    }

    public Date firstDayOfNthYearOrMonthOrWeekOrDay(Calendar calendar, Type.DateType dateType, int nValue) {
        switch (dateType) {
            case YEAR:
                Calendar lastNYearStartDay = (Calendar) calendar.clone();
                lastNYearStartDay.add(Calendar.YEAR, -(1 * nValue));   // to get first day of last N Month
                lastNYearStartDay.set(Calendar.DAY_OF_YEAR, 1);
                return lastNYearStartDay.getTime();
            case MONTH:
                Calendar lastNMonthStartDay = (Calendar) calendar.clone();
                lastNMonthStartDay.add(Calendar.MONTH, -(1 * nValue));   // to get first day of last N Month
                lastNMonthStartDay.set(Calendar.DAY_OF_MONTH, 1);
                return lastNMonthStartDay.getTime();
            case WEEK:
                Calendar lastWeekStartDay = (Calendar) calendar.clone();
                int i = lastWeekStartDay.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
                lastWeekStartDay.add(Calendar.DAY_OF_WEEK, -(i + (7 * nValue)));   // to get first day of last N week
                return lastWeekStartDay.getTime();
            case DAY:
            default:
                Calendar lastNDay = (Calendar) calendar.clone();
                lastNDay.add(Calendar.DAY_OF_MONTH, -(1 * nValue));   // to get first day of last N Month
                return lastNDay.getTime();
        }
    }

    public Date lastDayOfLastYearOrMonthOrWeek(Calendar calendar, Type.DateType dateType) {
        switch (dateType) {
            case YEAR:
                Calendar lastYearLastDay = (Calendar) calendar.clone();
                lastYearLastDay.add(Calendar.YEAR, -1);
                lastYearLastDay.set(Calendar.DAY_OF_YEAR, lastYearLastDay.getActualMaximum(Calendar.DAY_OF_YEAR)); // to get last day of last year
                return lastYearLastDay.getTime();
            case MONTH:
                Calendar lastMonthStartDay = (Calendar) calendar.clone();
                lastMonthStartDay.add(Calendar.MONTH, -1);
                lastMonthStartDay.set(Calendar.DAY_OF_MONTH, lastMonthStartDay.getActualMaximum(Calendar.DAY_OF_MONTH)); // to get last day of last Month
                return lastMonthStartDay.getTime();
            case WEEK:
            default:
                Calendar lastWeekStartDay = (Calendar) calendar.clone();
                int i = lastWeekStartDay.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
                lastWeekStartDay.add(Calendar.DAY_OF_WEEK, -(i + 1)); // to get last day of last week
                return lastWeekStartDay.getTime();
        }
    }

    public Date firstDayOfCurrentYearOrMonthOrWeek(Calendar calendar, Type.DateType dateType) {
        switch (dateType) {
            case YEAR:
                Calendar firstDayOfYear = (Calendar) calendar.clone();
                firstDayOfYear.set(Calendar.DAY_OF_YEAR, 1);   // to get first day of current year
                return firstDayOfYear.getTime();
            case MONTH:
                Calendar firstDayOfMonth = (Calendar) calendar.clone();
                firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);   // to get first day of current month
                return firstDayOfMonth.getTime();
            case WEEK:
            default:
                Calendar firstDayOfWeek = (Calendar) calendar.clone();
                firstDayOfWeek.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());   // to get first day of current week
                return firstDayOfWeek.getTime();
        }
    }

    public Calendar getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 00);

        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        return calendar;
    }
}
