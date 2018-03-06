package org.jenkinsci.plugins.sample.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jenkinsci.plugins.sample.model.wrapper.DefaultResponse;
import org.jenkinsci.plugins.sample.model.wrapper.GetBasicTestResponse;
import org.jenkinsci.plugins.sample.model.JMeterTestBasicDetailsDTO;
import org.jenkinsci.plugins.sample.model.wrapper.JMeterRunningSessionResponse;
import org.jenkinsci.plugins.sample.model.wrapper.StartSessionResponse;
import org.jenkinsci.plugins.sample.util.RestUtil;

import java.util.List;

/**
 * Created by furkanbrgl on 13/11/2017.
 */
public class LoadiumService {

    private static LoadiumService instance = null;

    private RestUtil rest;

    protected LoadiumService() {
    }

    public static LoadiumService getInstance() {
        if (instance == null) {
            instance = new LoadiumService();
        }
        return instance;
    }

    public List<JMeterTestBasicDetailsDTO> getTests() throws Exception {

        rest = new RestUtil();
        String url = "/tests";
        String result = rest.getResourceRestCall(url);

        ObjectMapper mapper = new ObjectMapper();
        GetBasicTestResponse getBasicTestResponse = mapper.readValue(result, GetBasicTestResponse.class);

        return getBasicTestResponse.getTestBasicDetailsDTOs();

    }

    public StartSessionResponse startSession(String testKey) throws Exception {

        rest = new RestUtil();
        String url = "/tests/" + testKey + "/session";
        String result = rest.postResourceRestCall(url, null);
        ObjectMapper mapper = new ObjectMapper();
        StartSessionResponse startSessionResponse = mapper.readValue(result, StartSessionResponse.class);

        return startSessionResponse;

    }

    public JMeterRunningSessionResponse getSessionStatus(String sessionKey) throws Exception {

        rest = new RestUtil();
        String url = "/session/" + sessionKey + "/detail";
        String result = rest.postResourceRestCall(url, null);
        ObjectMapper mapper = new ObjectMapper();
        JMeterRunningSessionResponse jMeterRunningSessionResponse = mapper.readValue(result, JMeterRunningSessionResponse.class);

        return jMeterRunningSessionResponse;

    }

    public DefaultResponse stopSession(String sessionKey, String testKey) throws Exception {

        rest = new RestUtil();
        String url = "/tests/" + testKey + "/session/" + sessionKey;
        String result = rest.deleteResourceRestCall(url, null);
        ObjectMapper mapper = new ObjectMapper();
        DefaultResponse defaultResponse = mapper.readValue(result, DefaultResponse.class);

        return defaultResponse;

    }

}
