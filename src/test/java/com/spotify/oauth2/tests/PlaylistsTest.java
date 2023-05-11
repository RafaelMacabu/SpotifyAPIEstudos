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
    public void create_playlist() {
        Playlist requestPlaylist = playlistBuilder("New Playlist", "New Playlist Description", false);

        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.statusCode(), 201);

        Playlist responsePlaylist = response.as(Playlist.class);
        assertPlaylistEqual(responsePlaylist, requestPlaylist);


    }


    @Test
    public void get_playlist() {
        Playlist requestPlaylist = playlistBuilder("Playlist API 2", "Descrição API 2",true);

        Response response = PlaylistApi.get(DataLoader.getInstance().getPlaylistId());
        assertStatusCode(response.statusCode(), 200);

        Playlist responsePlaylist = response.as(Playlist.class);
        assertPlaylistEqual(responsePlaylist, requestPlaylist);

    }


    @Test
    public void update_playlist() {
        Playlist requestPlaylist = playlistBuilder("Playlist API 2", "Descrição API 2", true);

        Response response = PlaylistApi.update(requestPlaylist, DataLoader.getInstance().getUpdatePlaylistId());
        assertStatusCode(response.statusCode(), 200);

    }

    @Test
    public void should_not_be_able_create_playlist_without_name() {
        Playlist requestPlaylist = playlistBuilder("", "Descrição API 222", true);

        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.statusCode(), 400);

        ErrorRoot error = response.as(ErrorRoot.class);

        assertError(error, 400, "Missing required field: name");


    }

    @Test
    public void should_not_be_able_create_playlist_with_expired_token() {
        Playlist requestPlaylist = playlistBuilder("Playlist API 2", "Descrição API 2", true);

        Response response = PlaylistApi.post(requestPlaylist, "12345");
        assertStatusCode(response.statusCode(), 401);

        ErrorRoot error = response.as(ErrorRoot.class);

        assertError(error, 401, "Invalid access token");


    }

    public Playlist playlistBuilder(String name, String description, boolean _public) {
        return Playlist.builder().
                name(name).
                description(description).
                _public(_public).
                build();

    }

    public void assertPlaylistEqual(Playlist responsePlaylist, Playlist requestPlaylist) {
        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.get_public(), equalTo(requestPlaylist.get_public()));

    }

    public void assertStatusCode(int actualStatusCode, int expectedStatusCode) {
        assertThat(actualStatusCode, equalTo(expectedStatusCode));

    }

    public void assertError(ErrorRoot responseError, int expectedStatusCode, String expectedMessage) {
        assertThat(responseError.getError().getStatus(), equalTo(expectedStatusCode));
        assertThat(responseError.getError().getMessage(), equalTo(expectedMessage));
    }
}
