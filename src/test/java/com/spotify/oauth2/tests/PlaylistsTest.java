package com.spotify.oauth2.tests;


import com.spotify.oauth2.pojo.ErrorRoot;
import com.spotify.oauth2.pojo.Playlist;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistsTest {
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    String access_token = "BQBrVSb9pv0dKWhkvONpw8Kw4rLuC-AYXGzFGA8NA2n9pbhu5wYkMatXSmaBUqFC3WlXfWx07Ye36tgw9dMMiTZVvPa1XNpxKlXiuNr6FhhiaBDg57YqHgSB9SNZFSoSGmpYD2j8wb_-5eIWPxQRu7TGH27xdfLu7QYkADVL42av-ai6Tm5qUthod7m6dervN6trqUufkgYZj-DXWUlZ04-Ehr_XeT30yGyOS6OEfeZJMA5xL1WQ64_XYfK4MceLcSotubrbwlwL5g";
    @BeforeClass
    public void beforeClass(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri("https://api.spotify.com").
                setBasePath("/v1").
                addHeader("Authorization","Bearer " + access_token).
                setContentType(ContentType.JSON).
                log(LogDetail.ALL);

        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
                expectContentType(ContentType.JSON).
                log(LogDetail.ALL);

        responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void create_playlist(){
        Playlist requestPlaylist = new Playlist()
                .setName("New Playlist")
                .setDescription(" New Playlist Description ")
                .setPublic(false);

        Playlist responsePlaylist = given(requestSpecification).
                body(requestPlaylist).
                when().
                post("/users/22c4ik3nkrsqpzpdcsgfopjfa/playlists").
                then().spec(responseSpecification).
                assertThat().
                statusCode(201).
                extract().response().
                as(Playlist.class);

        assertThat(responsePlaylist.getName(),equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(),equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.getPublic(),equalTo(requestPlaylist.getPublic()));
    }


    @Test
    public void get_playlist(){

        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("Playlist API 2");
        requestPlaylist.setDescription("Descrição API 2");

        Playlist responsePlaylist = given(requestSpecification).
                when().
                get("/playlists/27L5BsZ3akYGPINyxe4zTJ").
                then().spec(responseSpecification).
                assertThat().
                statusCode(200)
                .extract().response().
                as(Playlist.class);

        assertThat(responsePlaylist.getName(),equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(),equalTo(requestPlaylist.getDescription()));
    }


    @Test
    public void update_playlist(){
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("Playlist API 2");
        requestPlaylist.setDescription("Descrição API 2");
        requestPlaylist.setPublic(true);
        given(requestSpecification).
                body(requestPlaylist).
                when().
                put("/playlists/27L5BsZ3akYGPINyxe4zTJ").
                then().
                assertThat().
                statusCode(200);

    }

    @Test
    public void should_not_be_able_create_playlist_without_name(){
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("");
        requestPlaylist.setDescription("Descrição API 222");
        requestPlaylist.setPublic(true);

        ErrorRoot error = given(requestSpecification).
                body(requestPlaylist).
                when().
                post("/users/22c4ik3nkrsqpzpdcsgfopjfa/playlists").
                then().spec(responseSpecification).
                assertThat().
                statusCode(400).
                extract().response().
                as(ErrorRoot.class);

        assertThat(error.getError().getStatus(),equalTo(400));
        assertThat(error.getError().getMessage(),equalTo("Missing required field: name"));

    }

    @Test
    public void should_not_be_able_create_playlist_with_expired_token(){
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("Playlist API 2");
        requestPlaylist.setDescription("Descrição API 2");
        requestPlaylist.setPublic(true);
        ErrorRoot error = given().
                baseUri("https://api.spotify.com").
                basePath("/v1").
                header("Authorization","Bearer " + "1234").
                contentType(ContentType.JSON).
                log().all().
                body(requestPlaylist).
                when().
                post("/users/22c4ik3nkrsqpzpdcsgfopjfa/playlists").
                then().spec(responseSpecification).
                assertThat().
                statusCode(401).
                extract().response().
                as(ErrorRoot.class);

        assertThat(error.getError().getStatus(),equalTo(401));
        assertThat(error.getError().getMessage(),equalTo("Invalid access token"));
    }
}
