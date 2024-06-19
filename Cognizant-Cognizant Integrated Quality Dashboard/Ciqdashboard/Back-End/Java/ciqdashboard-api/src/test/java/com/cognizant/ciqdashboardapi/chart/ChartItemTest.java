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

package com.cognizant.ciqdashboardapi.chart;

import com.cognizant.ciqdashboardapi.models.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
/**
 * ChartItemTest
 * @author Cognizant
 */

@SpringBootTest
class ChartItemTest {

    /*
    @Test
    void chartTest(){
        Optional<Type.ChartItemType> optional = Type.ChartItemType.getChartItemType("pie-chart");
        Type.ChartItemType chartItemType = optional.get();
        Type.GenericChartItemType genericChartItemType = Type.getGenericChartItemType(chartItemType);
       Assertions.assertNotNull(genericChartItemType);
    }*/
}
