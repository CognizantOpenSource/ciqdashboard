package com.cognizant.ciqdashboardapi.services;

import com.cognizant.ciqdashboardapi.errors.ResourceNotFoundException;
import com.cognizant.ciqdashboardapi.models.LOB;
import com.cognizant.ciqdashboardapi.repos.LOBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LOBService {
    @Autowired
    LOBRepository lobRepo;

    public LOB createLOB(LOB lob) throws Exception {
        Optional<LOB> lobObject = lobRepo.findByLobName(lob.getLobName());
        if (lobObject.isPresent()) {
            throw new Exception("Lob with mentioned name already exists!");
        } else {
            return lobRepo.save(lob);
        }
    }

    public List<LOB> getAllLOB() {
        return lobRepo.findAll();
    }

    public LOB getLobById(String id) {
        Optional<LOB> lob = lobRepo.findById(id);
        if (lob.isPresent()) {
            return lob.get();
        } else {
            throw new ResourceNotFoundException("LOB", "id", id);
        }
    }

    public List<LOB> getLobByOrgId(String orgId) {
        return lobRepo.findByOrgId(orgId);
    }

    public LOB assertAndGetByName(String lobName) {
        Optional<LOB> lobObject = lobRepo.findByLobName(lobName);
        if (!lobObject.isPresent()) {
            throw new ResourceNotFoundException("LOB", "name", lobName);
        } else {
            return lobObject.get();
        }
    }

    public void deleteById(String id) {
        Optional<LOB> lob = lobRepo.findById(id);
        if (lob.isPresent()) {
            lobRepo.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Project", "id", id);
        }
    }
}
