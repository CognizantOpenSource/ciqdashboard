package com.cognizant.ciqdashboardapi.services;


import com.cognizant.ciqdashboardapi.errors.InvalidDetailsException;
import com.cognizant.ciqdashboardapi.errors.ResourceExistsException;
import com.cognizant.ciqdashboardapi.models.CIQDashboardTemplate;
import com.cognizant.ciqdashboardapi.models.Filter;
import com.cognizant.ciqdashboardapi.models.FilterConfig;
import com.cognizant.ciqdashboardapi.repos.CIQDashboardTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class CIQDashboardTemplateService {
    @Autowired
    CIQDashboardTemplateRepository dashboardTemplateRepository;

    public List<CIQDashboardTemplate> getAllTemplates(String category) throws Exception {
        if (category.equalsIgnoreCase("undefined")) {
            return dashboardTemplateRepository.findAll();
        }
        List<CIQDashboardTemplate> templateList = dashboardTemplateRepository.findByCategory(category);
        if (templateList.isEmpty()) {
            throw new Exception("No Templates found!!");
        }
        return templateList;
    }

    public List<CIQDashboardTemplate> getAllTemplates() throws Exception {

        List<CIQDashboardTemplate> templateList = dashboardTemplateRepository.findAll();
        if (templateList.isEmpty()) {
            throw new Exception("No Templates found!!");
        }
        return templateList;
    }

    public CIQDashboardTemplate add(CIQDashboardTemplate cIQDashboardTemplate) {
        assertInsert(cIQDashboardTemplate);
        if (cIQDashboardTemplate != null){
            if (cIQDashboardTemplate.getPages() != null) {
                for (int i = 0; i < cIQDashboardTemplate.getPages().size(); i++) {
                    if(cIQDashboardTemplate.getPages().get(i).getItems()!=null) {
                        for (int j = 0; j < cIQDashboardTemplate.getPages().get(i).getItems().size(); j++) {
                            List<FilterConfig> filters = cIQDashboardTemplate.getPages().get(i).getItems().get(j).getFilters();
                            if(filters!=null) {
                                for (int k = 0; k < filters.size(); k++) {
                                    List<Filter> configs = filters.get(k).getConfigs();
                                    if (configs!=null) {
                                        for (int l = 0; l < configs.size(); l++) {
                                            if (configs.get(l).getField().equalsIgnoreCase("projectName")) {
                                                //configs.remove(configs.get(l));
                                                filters.remove(filters.get(k));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }
        cIQDashboardTemplate.setOpenAccess(false);
        return dashboardTemplateRepository.save(cIQDashboardTemplate);
    }

     private void assertInsert(CIQDashboardTemplate cIQDashboardTemplate) {
        if (!StringUtils.isEmpty(cIQDashboardTemplate.getId())) {
            throw new InvalidDetailsException("Id should be null/empty");
        }
        Optional<CIQDashboardTemplate> optional = getByNameIgnoreCase(cIQDashboardTemplate.getName());
        if (optional.isPresent()) {
            throw new ResourceExistsException("Template", "name", cIQDashboardTemplate.getName());
        }
    }

    public Optional<CIQDashboardTemplate> getByNameIgnoreCase(String name) {
        return dashboardTemplateRepository.findByName(name);
    }
}
