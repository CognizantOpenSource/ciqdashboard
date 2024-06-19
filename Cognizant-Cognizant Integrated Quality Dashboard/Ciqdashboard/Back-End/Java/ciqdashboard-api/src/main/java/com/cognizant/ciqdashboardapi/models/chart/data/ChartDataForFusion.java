package com.cognizant.ciqdashboardapi.models.chart.data;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
public class ChartDataForFusion {

    public String name;
    public Object value;
    public String label;
    public String link;
    public List<ChartDataForFusion> children;
    public List<ChartDataForFusion> series;

    public ChartDataForFusion(String name, Object value, String label, String link, List<ChartDataForFusion> children, List<ChartDataForFusion> series) {
        this.name = name;
        this.value = value;
        this.label = name;
        this.link = "#url#";
        this.children = children;
        this.series = series;
    }

    @Override
    public String toString() {
        return "[name=" + name + ", value=" + value + " label=" + name + " link=" + link + "]";
    }
    /*public String toString() {
        if (children == null && series == null)
            return String.format("(%s:%s)", name, value);
        else if (series == null)
            return String.format("(%s:%s, children:%s)", name, value, children.toString());
        else
            return String.format("(%s:%s)", name, series.toString());
    }
*/
    public static class ChartDataForFusionBuilder {

        public List<ChartDataForFusion> children;
        public List<ChartDataForFusion> series;

        public ChartDataForFusion children(@NonNull ChartDataForFusion... data) {
            this.children = Arrays.asList(data);
            return this.build();
        }

        public ChartDataForFusion series(@NonNull ChartDataForFusion... data) {
            this.series = Arrays.asList(data);
            return this.build();
        }



        public ChartDataForFusion series(List<ChartDataForFusion> data) {
            this.series = data;
            return this.build();
        }
    }
}
