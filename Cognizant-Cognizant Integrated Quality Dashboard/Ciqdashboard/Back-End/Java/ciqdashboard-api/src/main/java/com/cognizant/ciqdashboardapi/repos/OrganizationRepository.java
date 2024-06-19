package com.cognizant.ciqdashboardapi.repos;

import com.cognizant.ciqdashboardapi.models.Organization;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends MongoRepository<Organization, String> {
    Optional<Organization> findByOrganizationName(String organizationName);

}
