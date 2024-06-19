package com.cognizant.ciqdashboardapi.repos;

import com.cognizant.ciqdashboardapi.models.CIQDashboardTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CIQDashboardTemplateRepository extends MongoRepository<CIQDashboardTemplate, String> {
    List<CIQDashboardTemplate> findByCategory(String category);
    Optional<CIQDashboardTemplate> findByName(String name);
}
