package com.cognizant.ciqdashboardapi.repos;

import com.cognizant.ciqdashboardapi.models.LOB;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LOBRepository extends MongoRepository<LOB, String> {
    Optional<LOB> findByLobName(String lobName);

    List<LOB> findByOrgId(String orgId);
}
