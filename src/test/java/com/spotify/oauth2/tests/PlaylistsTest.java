package com.spotify.oauth2.tests;


import com.spotify.oauth2.api.applicationAPI.PlaylistApi;
import com.spotify.oauth2.pojo.ErrorRoot;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.spotify.oauth2.api.SpecBuilder.getReqSpec;
import static com.spotify.oauth2.api.SpecBuilder.getResSpec;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistsTest {

    @Test
    public void create_playlist(){
        Playlist requestPlaylist = new Playlist()
                .setName("New Playlist")
                .setDescription("New Playlist Description")
                .setPublic(false);

        Response response = PlaylistApi.post(requestPlaylist);
        assertThat(response.statusCode(),equalTo(201));

        Playlist responsePlaylist = response.as(Playlist.class);

        assertThat(responsePlaylist.getName(),equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(),equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.getPublic(),equalTo(requestPlaylist.getPublic()));
    }


    @Test
    public void get_playlist(){

        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("Playlist API 2");
        requestPlaylist.setDescription("Descrição API 2");

        Response response = PlaylistApi.get(DataLoader.getInstance().getPlaylistId());
        assertThat(response.statusCode(),equalTo(200));

        Playlist responsePlaylist = response.as(Playlist.class);


        assertThat(responsePlaylist.getName(),equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(),equalTo(requestPlaylist.getDescription()));
    }


    @Test
    public void update_playlist(){
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("Playlist API 2");
        requestPlaylist.setDescription("Descrição API 2");
        requestPlaylist.setPublic(true);

        Response response = PlaylistApi.update(requestPlaylist, DataLoader.getInstance().getUpdatePlaylistId());
        assertThat(response.statusCode(),equalTo(200));

    }

    @Test
    public void should_not_be_able_create_playlist_without_name(){
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("");
        requestPlaylist.setDescription("Descrição API 222");
        requestPlaylist.setPublic(true);

        Response response = PlaylistApi.post(requestPlaylist);
        assertThat(response.statusCode(),equalTo(400));

        ErrorRoot error = response.as(ErrorRoot.class);


        assertThat(error.getError().getStatus(),equalTo(400));
        assertThat(error.getError().getMessage(),equalTo("Missing required field: name"));

    }

    @Test
    public void should_not_be_able_create_playlist_with_expired_token(){
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("Playlist API 2");
        requestPlaylist.setDescription("Descrição API 2");
        requestPlaylist.setPublic(true);

        Response response = PlaylistApi.post(requestPlaylist,"12345");
        assertThat(response.statusCode(),equalTo(401));

        ErrorRoot error = response.as(ErrorRoot.class);



        assertThat(error.getError().getStatus(),equalTo(401));
        assertThat(error.getError().getMessage(),equalTo("Invalid access token"));
    }
}
