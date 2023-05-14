package com.spotify.oauth2.tests;


import com.spotify.oauth2.api.StatusCode;
import com.spotify.oauth2.api.applicationAPI.PlaylistApi;
import com.spotify.oauth2.pojo.ErrorRoot;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;
import io.qameta.allure.*;
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
import static com.spotify.oauth2.utils.FakerUtils.generateDescription;
import static com.spotify.oauth2.utils.FakerUtils.generatePlaylistName;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
@Epic("Spotify OAuth 2.0")
@Feature("Playlist API")
public class PlaylistsTest extends BaseTest {

    @Story("Create Playlist")
    @Description("description 123")
    @Test(description = "creates a playlist")
    public void create_playlist() {
        Playlist requestPlaylist = playlistBuilder(generatePlaylistName(), generateDescription(), false);

        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_201);

        Playlist responsePlaylist = response.as(Playlist.class);
        assertPlaylistEqual(responsePlaylist, requestPlaylist);


    }


    @Test
    public void get_playlist() {
        Playlist requestPlaylist = playlistBuilder("Playlist API 2", "Descrição API 2",true);

        Response response = PlaylistApi.get(DataLoader.getInstance().getPlaylistId());
        assertStatusCode(response.statusCode(), StatusCode.CODE_200);

        Playlist responsePlaylist = response.as(Playlist.class);
        assertPlaylistEqual(responsePlaylist, requestPlaylist);

    }


    @Test
    public void update_playlist() {
        Playlist requestPlaylist = playlistBuilder("Playlist API 2", "Descrição API 2", true);

        Response response = PlaylistApi.update(requestPlaylist, DataLoader.getInstance().getUpdatePlaylistId());
        assertStatusCode(response.statusCode(), StatusCode.CODE_200);

    }
    @Story("Create Playlist")
    @Test
    public void should_not_be_able_create_playlist_without_name() {
        Playlist requestPlaylist = playlistBuilder("", generateDescription(), true);

        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_400);

        ErrorRoot error = response.as(ErrorRoot.class);

        assertError(error, StatusCode.CODE_400,  StatusCode.CODE_400);


    }
    @Story("Create Playlist")
    @Test
    public void should_not_be_able_create_playlist_with_expired_token() {
        Playlist requestPlaylist = playlistBuilder(generatePlaylistName(), generateDescription(), true);

        Response response = PlaylistApi.post(requestPlaylist, "12345");
        assertStatusCode(response.statusCode(), StatusCode.CODE_401);

        ErrorRoot error = response.as(ErrorRoot.class);

        assertError(error, StatusCode.CODE_401, StatusCode.CODE_401);


    }
    @Step
    public Playlist playlistBuilder(String name, String description, boolean _public) {
        return Playlist.builder().
                name(name).
                description(description).
                _public(_public).
                build();

    }
    @Step
    public void assertPlaylistEqual(Playlist responsePlaylist, Playlist requestPlaylist) {
        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.get_public(), equalTo(requestPlaylist.get_public()));

    }
    @Step
    public void assertStatusCode(int actualStatusCode, StatusCode expectedStatusCode) {
        assertThat(actualStatusCode, equalTo(expectedStatusCode.code));

    }

    public void assertError(ErrorRoot responseError, StatusCode expectedStatusCode, StatusCode expectedMessage) {
        assertThat(responseError.getError().getStatus(), equalTo(expectedStatusCode.code));
        assertThat(responseError.getError().getMessage(), equalTo(expectedMessage.msg));
    }
}
