package com.cognizant.ciqdashboardapi.repos;

import com.cognizant.ciqdashboardapi.models.AddNewMetricDropDowns;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddNewMetricDropDownsRepository extends MongoRepository<AddNewMetricDropDowns, String> {

    List<AddNewMetricDropDowns> findByDropDownCode(String dropDownCode);
}
