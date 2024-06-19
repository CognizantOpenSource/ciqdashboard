package com.cognizant.ciqdashboardapi.repos;

import com.cognizant.ciqdashboardapi.models.SourceTools;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceToolsRepo extends MongoRepository<SourceTools, String> {
}
