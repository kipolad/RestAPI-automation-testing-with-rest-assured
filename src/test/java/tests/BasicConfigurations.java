/**
 * Created by Yulya Telysheva
 */
package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class BasicConfigurations {
    static Properties properties = new Properties();
    private static String apiKey;
    private static String baseUrl;
    private static String hash;

    @BeforeAll
    static void initTest() {
        try {
            InputStream configFile = new FileInputStream("src/main/resources/properties.properties");
            properties.load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        apiKey = properties.getProperty("apiKey");
        baseUrl = properties.getProperty("base_url");
        hash = properties.getProperty("hash");

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static String getHash() {
        return hash;
    }
}
