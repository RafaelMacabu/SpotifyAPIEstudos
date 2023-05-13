package com.spotify.oauth2.api.applicationAPI;

import com.spotify.oauth2.api.RestResource;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.ConfigLoader;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.spotify.oauth2.api.Route.PLAYLISTS;
import static com.spotify.oauth2.api.Route.USERS;
import static com.spotify.oauth2.api.SpecBuilder.getReqSpec;
import static com.spotify.oauth2.api.SpecBuilder.getResSpec;
import static com.spotify.oauth2.api.TokenManager.getToken;

import static io.restassured.RestAssured.given;

public class PlaylistApi {
    //static String access_token = "BQDY2IHJWfW809674Pr0rC-VnffrHI3x83Ponfi3Y3DqzFM11_u18Zd2XMpcdUXEbWyYuLRIoaFnhZkEhSK5NaLrn4o-9y0WGOoqXdcPXDf72M6AgwOglLMPTVa-_Bh5F6lpoXrqbM7GxTGNRVis3IjbkjafzSwe18dv2-P_UoPc6fr2MmJ6NNt1m4rEb1VkvytgOhwf_gXccW9TSVXm4f0S_HjryKgJTbYINKwsCOKT2vRM-RCBuCBE3-LNFy02d78MJZ3bLnzzxw";

    @Step
    public static Response post(Playlist requestPlaylist){
        return RestResource.post(USERS + "/" + ConfigLoader.getInstance().getUserId() + PLAYLISTS,getToken(),requestPlaylist);

    }

    public static Response post(Playlist requestPlaylist,String token){
        return RestResource.post(USERS + "/" + ConfigLoader.getInstance().getUserId() + PLAYLISTS,token,requestPlaylist);
    }

    public static Response get(String playlistId){
        return RestResource.get(PLAYLISTS + "/" + playlistId,getToken());

    }

    public static Response update(Playlist requestPlaylist,String playlistId){
        return RestResource.update(PLAYLISTS + "/" + playlistId,getToken(),requestPlaylist);

    }
}
