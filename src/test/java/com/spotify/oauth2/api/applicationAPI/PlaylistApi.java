package com.spotify.oauth2.api.applicationAPI;

import com.spotify.oauth2.api.RestResource;
import com.spotify.oauth2.pojo.Playlist;
import io.restassured.response.Response;

import static com.spotify.oauth2.api.SpecBuilder.getReqSpec;
import static com.spotify.oauth2.api.SpecBuilder.getResSpec;
import static com.spotify.oauth2.api.TokenManager.getToken;

import static io.restassured.RestAssured.given;

public class PlaylistApi {
    //static String access_token = "BQDY2IHJWfW809674Pr0rC-VnffrHI3x83Ponfi3Y3DqzFM11_u18Zd2XMpcdUXEbWyYuLRIoaFnhZkEhSK5NaLrn4o-9y0WGOoqXdcPXDf72M6AgwOglLMPTVa-_Bh5F6lpoXrqbM7GxTGNRVis3IjbkjafzSwe18dv2-P_UoPc6fr2MmJ6NNt1m4rEb1VkvytgOhwf_gXccW9TSVXm4f0S_HjryKgJTbYINKwsCOKT2vRM-RCBuCBE3-LNFy02d78MJZ3bLnzzxw";


    public static Response post(Playlist requestPlaylist){
        return RestResource.post("/users/22c4ik3nkrsqpzpdcsgfopjfa/playlists",getToken(),requestPlaylist);

    }

    public static Response post(Playlist requestPlaylist,String token){
        return RestResource.post("/users/22c4ik3nkrsqpzpdcsgfopjfa/playlists",token,requestPlaylist);
    }

    public static Response get(String playlistId){
        return RestResource.get("/playlists/" + playlistId,getToken());

    }

    public static Response update(Playlist requestPlaylist,String playlistId){
        return RestResource.update("/playlists/" + playlistId,getToken(),requestPlaylist);

    }
}
