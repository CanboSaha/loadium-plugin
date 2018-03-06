package org.jenkinsci.plugins.sample.util;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.jenkinsci.plugins.sample.services.AuthService;

import static io.restassured.RestAssured.given;

/**
 * Created by furkanbrgl on 14/11/2017.
 */
public class RestUtil {

    private final static Logger LOGGER = Logger.getLogger(RestUtil.class);
    private AuthService authService = AuthService.getInstance();

    private EnviromentUtil enviromentUtil = null;

    public RestUtil() {
        enviromentUtil = EnviromentUtil.getInstance();
    }

    public String getResourceRestCall(String url) throws Exception {

        Response response = given().header("Authorization", "bearer " + authService.getAuthToken())
                .get(enviromentUtil.getResourceBaseURL() + url)
                .then()
                .extract()
                .response();

        if (response.getStatusCode() != 200) {
            LOGGER.info("getResourceRestCall");
            throw new RuntimeException("An unknown error has occurred in attempting to connect the Api :" + String.valueOf(response.getStatusLine()));
        }

        return response.getBody().prettyPrint();
    }

    public String postResourceRestCall(String url, Object o) throws Exception {

        Response response = null;
        if (o == null) {

            response = given().header("Authorization", "bearer " + authService.getAuthToken())
                    .post(enviromentUtil.getResourceBaseURL() + url)
                    .then()
                    .extract()
                    .response();
        }

        if (response.getStatusCode() != 200) {
            LOGGER.info("postResourceRestCall");
            LOGGER.info("Response Code : " + response.getStatusCode());
            throw new RuntimeException("An unknown error has occurred in attempting to connect the Api :" + String.valueOf(response.getStatusLine()));
        }

        return response.getBody().prettyPrint();
    }

    public String deleteResourceRestCall(String url, Object o) throws Exception {

        Response response = null;
        if (o == null) {

            response = given().header("Authorization", "bearer " + authService.getAuthToken())
                    .delete(enviromentUtil.getResourceBaseURL() + url)
                    .then()
                    .extract()
                    .response();
        }

        if (response.getStatusCode() != 200) {
            LOGGER.info("deleteResourceRestCall");
            LOGGER.info("Response Code : " + response.getStatusCode());
            throw new RuntimeException("An unknown error has occurred in attempting to connect the Api :" + String.valueOf(response.getStatusLine()));
        }

        return response.getBody().prettyPrint();
    }

    public String getAuthToken(String userName, String password) throws Exception {

        String accessToken = null;
        Response response = given().header("Authorization",enviromentUtil.getAuthorization())
                .queryParam("grant_type",enviromentUtil.getGrantType())
                .queryParam("username", userName)
                .queryParam("password", password)
                .queryParam("scope", enviromentUtil.getScope())
                .post(enviromentUtil.getAuthServerTokenURL())
                .then()
                .extract().response();

        JsonPath jsonPathEvaluator = response.jsonPath();

        accessToken = jsonPathEvaluator.get("access_token");
        if(accessToken == null){
            throw new Exception();
        }
        return accessToken;

    }
}