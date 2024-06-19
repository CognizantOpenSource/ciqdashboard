package com.cognizant.ciqdashboardapi.services;

import com.cognizant.ciqdashboardapi.models.SourceTools;
import com.cognizant.ciqdashboardapi.repos.SourceToolsRepo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SourceToolsService {
    @Autowired
    private SourceToolsRepo repository;
    @Autowired
    private MongoOperations mongoOperations;

    public List<SourceTools> getAll() {
        return repository.findAll();
    }

    public JSONArray getDistinctSourceData() throws ClassNotFoundException {
        List<SourceTools> list = getAll();
        System.out.println("List - " + list);
        JSONArray sourceTools=new JSONArray();
        for (SourceTools st : list) {
            String toolName = st.getToolName();
            String projectField = st.getProjectField();
            String sourceData = st.getSourceData();

            Class c = Class.forName("com.cognizant.ciqdashboardapi.models." + sourceData);
            List<String> val = mongoOperations.findDistinct(projectField, c, String.class);
            JSONObject obj = new JSONObject();
            obj.put("projectNames", val);
            obj.put("toolName", toolName);

            sourceTools.add(obj);
            System.out.println(sourceTools);
//        save this to data base of ciqdashboardprojects;
        }
        return sourceTools;
    }
}
