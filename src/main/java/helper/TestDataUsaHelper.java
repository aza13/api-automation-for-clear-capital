package helper;


import constants.EndPoints;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.apache.xmlgraphics.util.WriterOutputStream;
import utils.ConfigManager;

import java.io.PrintStream;
import java.io.StringWriter;
import java.util.HashMap;


import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class TestDataUsaHelper {

    private static final Logger logger = Logger.getLogger(TestDataUsaHelper.class);
    private static final String BASE_URL = ConfigManager.getInstance().getString("baseUrl");

    static StringWriter requestWriter = null;
    static PrintStream requestCapture = null;

    public TestDataUsaHelper(){
        baseURI = BASE_URL;
    }

    public Response getDataUSA(){
        RestAssured.useRelaxedHTTPSValidation();
        Response response = null;
        try {
            requestWriter = new StringWriter();
            requestCapture = new PrintStream(new WriterOutputStream(requestWriter));
            response = given().filter(new RequestLoggingFilter(requestCapture))
                    .contentType(ContentType.JSON)
                    .get(EndPoints.GET_DATA_USA)
                    .andReturn();
        } catch (Exception e){
            logger.error("Failed to process api request in:: getDataUSA" + e.getMessage());
        }
        finally {
            requestCapture.flush();
        }
        logger.info("Request from DataUSA: " + requestWriter.toString());
        assert response != null;
        logger.info("Response from DataUSA: " + response.asString());
        return response;
    }




}
