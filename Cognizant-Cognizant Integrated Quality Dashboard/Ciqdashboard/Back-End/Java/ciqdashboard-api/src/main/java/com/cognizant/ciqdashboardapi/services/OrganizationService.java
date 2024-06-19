package com.cognizant.ciqdashboardapi.services;

import com.cognizant.ciqdashboardapi.errors.ResourceNotFoundException;
import com.cognizant.ciqdashboardapi.models.Organization;
import com.cognizant.ciqdashboardapi.repos.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    OrganizationRepository orgRepo;

    public Organization createOrganization(Organization organization) throws Exception {
        Optional<Organization> org = orgRepo.findByOrganizationName(organization.getOrganizationName());
        if (org.isPresent()) {
            throw new Exception("Organization name already exists!");
        } else {
            return orgRepo.save(organization);
        }
    }

    public List<Organization> getAllOrganization() {
        return orgRepo.findAll();
    }

    public Organization getOrganizationById(String id) {
        Optional<Organization> org = orgRepo.findById(id);
        if (org.isPresent()) {
            return org.get();
        } else {
            throw new ResourceNotFoundException("Organization", "id", id);
        }
    }

    public Organization assertAndGetByName(String orgName) {
        Optional<Organization> orgObject = orgRepo.findByOrganizationName(orgName);
        if (!orgObject.isPresent()) {
            throw new ResourceNotFoundException("Organization", "name", orgName);
        } else {
            return orgObject.get();
        }
    }


    public void deleteById(String id) {
        Optional<Organization> org = orgRepo.findById(id);
        if (org.isPresent()) {
            orgRepo.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Organization", "id", id);
        }
    }
}
