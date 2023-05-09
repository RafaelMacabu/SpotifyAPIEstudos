package com.spotify.oauth2.api;

import com.spotify.oauth2.pojo.Playlist;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;

import static com.spotify.oauth2.api.SpecBuilder.*;
import static io.restassured.RestAssured.given;

public class RestResource {

    public static Response post(String path,String token,Object requestPlaylist){
        return given(getReqSpec()).
                body(requestPlaylist).
                headers("Authorization","Bearer " + token).
                when().
                post(path).
                then().spec(getResSpec()).
                extract().response();
    }


    public static Response get(String path,String token){
        return given(getReqSpec()).
                headers("Authorization","Bearer " + token).
                when().
                get(path).
                then().spec(getResSpec()).
                extract().response();

    }

    public static Response update(String path,String token,Object requestPlaylist){
        return given(getReqSpec()).
                body(requestPlaylist).
                headers("Authorization","Bearer " + token).
                when().
                put(path).
                then().
                extract().response();
    }

    public static Response postAccount(HashMap<String,String> formParams){
        return given(getAccountReqSpec()).
                formParams(formParams).
                when().
                post("/api/token").
                then().spec(SpecBuilder.getResSpec()).
                extract().response();
    }
}
