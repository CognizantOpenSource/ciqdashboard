package com.cognizant.ciqdashboardapi.repos;

import com.cognizant.ciqdashboardapi.models.SourceMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface MetricsRepository extends MongoRepository<SourceMetrics, String> {

    Optional<SourceMetrics> findByMetricNameAndDashboardNameAndPageNameAndItemId(String metricName, String dashboardName, String pageName, String itemId);

    SourceMetrics findByProjectNameAndDashboardNameAndItemId(String projectName, String dashboardName, String itemId);

    Optional<SourceMetrics> findByProjectNameAndDashboardNameAndItemIdAndMetricName(String projectName, String dashboardName, String itemId, String metricName);

    //SourceMetrics findByLobIdAndDashboardNameAndItemId(String lobId, String dashboardName, String itemId);

    //Optional<SourceMetrics> findByLobIdAndDashboardNameAndItemIdAndMetricName(String lobId, String dashboardName, String itemId, String metricName);

    Optional<SourceMetrics> findByLayerIdAndDashboardIdAndItemIdAndMetricName(String layerId, String dashboarId, String itemId, String metricName);

    //SourceMetrics findByOrgIdAndDashboardNameAndMetricName(String orgId, String dashboardName, String metricName);

    //List<SourceMetrics> findByLobIdAndMetricName(String lobId,String metricName);

    //SourceMetrics findByLobIdAndDashboardNameAndMetricName(String lobId, String dashboardName,String metricName);

    //List<SourceMetrics> findByOrgIdAndMetricName(String orgId, String metricName);

    //SourceMetrics findByOrgIdAndDashboardNameAndItemId(String orgId, String dashboardName, String itemId);

    //Optional<SourceMetrics> findByOrgIdAndDashboardNameAndItemIdAndMetricName(String orgId, String dashboardName, String itemId, String metricName);

}
