package com.cognizant.ciqdashboardapi.repos;

import com.cognizant.ciqdashboardapi.models.DirectChartData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DirectChartDataRepository extends MongoRepository<DirectChartData, String> {

    Optional<DirectChartData> findByItemId(String itemId);

    Optional<DirectChartData> findByItemIdAndDashboardIdAndLayerIdAndPageId(String itemId,String dashboardId,String layerId,String pageId);

    public void deleteByItemId(String itemId);
}
