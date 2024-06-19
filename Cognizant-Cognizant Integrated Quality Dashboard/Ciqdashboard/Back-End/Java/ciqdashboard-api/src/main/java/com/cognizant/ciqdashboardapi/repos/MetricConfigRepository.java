package com.cognizant.ciqdashboardapi.repos;

import com.cognizant.ciqdashboardapi.models.MetricConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface MetricConfigRepository extends MongoRepository<MetricConfig, String> {
    Optional<MetricConfig> findByMetricName(String metricName);

    //Optional<MetricConfig> findByLobIdAndMetricName(String lobId,String metricName);

    //Optional<MetricConfig> findByOrgIdAndMetricName(String orgId, String metricName);
}
