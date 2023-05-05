package com.spotify.oauth2.api;

import com.spotify.oauth2.pojo.Playlist;
import io.restassured.response.Response;

import static com.spotify.oauth2.api.SpecBuilder.getReqSpec;
import static com.spotify.oauth2.api.SpecBuilder.getResSpec;
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
}
