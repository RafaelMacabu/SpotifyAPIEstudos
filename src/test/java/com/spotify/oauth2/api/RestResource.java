package com.spotify.oauth2.api;

import com.spotify.oauth2.pojo.Playlist;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;

import static com.spotify.oauth2.api.Route.API;
import static com.spotify.oauth2.api.Route.TOKEN;
import static com.spotify.oauth2.api.SpecBuilder.*;
import static io.restassured.RestAssured.given;

public class RestResource {

    public static Response post(String path,String token,Object requestPlaylist){
        return given(getReqSpec()).
                body(requestPlaylist).
                auth().oauth2(token).
                when().
                post(path).
                then().spec(getResSpec()).
                extract().response();
    }


    public static Response get(String path,String token){
        return given(getReqSpec()).
                auth().oauth2(token).
                when().
                get(path).
                then().spec(getResSpec()).
                extract().response();

    }

    public static Response update(String path,String token,Object requestPlaylist){
        return given(getReqSpec()).
                body(requestPlaylist).
                auth().oauth2(token).
                when().
                put(path).
                then().
                extract().response();
    }

    public static Response postAccount(HashMap<String,String> formParams){
        return given(getAccountReqSpec()).
                formParams(formParams).
                when().
                post(API + TOKEN).
                then().spec(SpecBuilder.getResSpec()).
                extract().response();
    }
}
