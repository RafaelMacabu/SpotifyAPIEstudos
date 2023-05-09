package com.spotify.oauth2.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.time.Instant;
import java.util.HashMap;

import static com.spotify.oauth2.api.RestResource.postAccount;
import static io.restassured.RestAssured.given;

public class TokenManager {
    static String access_token;
    static Instant expiry_time;
    public static String getToken(){
        try{
            if (access_token == null || Instant.now().isAfter(expiry_time)){
                Response response  = renewToken();
                access_token = response.path("access_token");
                int expiryDurationInSeconds = response.path("expires_in");
                expiry_time = Instant.now().plusSeconds(expiryDurationInSeconds - 300);
            }else {
                System.out.println("Token is Valid");
            }

        }
        catch (Exception e){
            throw new RuntimeException("Failed to renew token");

        }
        return access_token;
    }

    private static Response renewToken(){
        HashMap<String,String> formParams = new HashMap<>();
        formParams.put("client_id","6072fcff6c9e4999b080218ec025ce39");
        formParams.put("client_secret","913d174602b6457cb7d4cbf64b240c4a");
        formParams.put("refresh_token","AQCjDx1a7uDcu5bVX5VUaEnPRHtFGakfTv-HUW-HtFkd1W7XUB3ms-NtRSlYqP2s_5jbZlAyigsurhc4ZTtIncYXffxYGsNsWwUHvWCVu4_9wOoWYvAZzDNdCr46TyvFddQ");
        formParams.put("grant_type","refresh_token");

        Response response = postAccount(formParams);

        if(response.statusCode() != 200){
            throw new RuntimeException("Renew Token Generation Failed");
        }
        return response;

    }
}
