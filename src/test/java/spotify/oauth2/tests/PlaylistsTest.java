package spotify.oauth2.tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistsTest {
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    String access_token = "BQCqsZLPz82hVcAOV5tthMfjlkL92fwQw50XPhHaGrAC9P2DRl5tZtVyXTdWJkkIf3x4qJbBGgn7Inp3zwJraOAR6B1waSYfPXK1StttX1c4rBbH5ElunUbrj1oZ1m5OsoVvFzEgwABtAgtscC0gWRlzdxbXTnPq20dN7knbLveknmrhpmTO9dxuz1mPgvRKW174yvKgMqw6jLOQbNCzJ6xyvB4ZMVeHUw-e4lCJv9cco5cDI_-n0w84Y-6JMvBPT9zoUv57HKQcBQ";
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
        String payload = "{\n" +
                "    \"name\": \"Playlist API\",\n" +
                "    \"description\": \"Descrição API 222\",\n" +
                "    \"public\": true\n" +
                "}";
        given(requestSpecification).
                body(payload).
                when().
                post("/users/22c4ik3nkrsqpzpdcsgfopjfa/playlists").
                then().spec(responseSpecification).
                assertThat().
                statusCode(201).
                body("name",equalTo("Playlist API"),
                        "description",equalTo("Descrição API 222"));
    }


    @Test
    public void get_playlist(){

        given(requestSpecification).
                when().
                get("/playlists/27L5BsZ3akYGPINyxe4zTJ").
                then().spec(responseSpecification).
                assertThat().
                statusCode(200).
                body("name",equalTo("Playlist API 2"),
                        "description",equalTo("Descrição API 2"));
    }


    @Test
    public void update_playlist(){
        String payload = "{\n" +
                "    \"name\": \"Playlist API 2\",\n" +
                "    \"description\": \"Descrição API 2\",\n" +
                "    \"public\": true\n" +
                "}";
        given(requestSpecification).
                body(payload).
                when().
                put("/playlists/27L5BsZ3akYGPINyxe4zTJ").
                then().
                assertThat().
                statusCode(200);
    }

    @Test
    public void should_not_be_able_create_playlist_without_name(){
        String payload = "{\n" +
                "    \"name\": \"\",\n" +
                "    \"description\": \"Descrição API 222\",\n" +
                "    \"public\": true\n" +
                "}";
        given(requestSpecification).
                body(payload).
                when().
                post("/users/22c4ik3nkrsqpzpdcsgfopjfa/playlists").
                then().spec(responseSpecification).
                assertThat().
                statusCode(400).
                body("error.status",equalTo(400),
                        "error.message",equalTo("Missing required field: name"));
    }

    @Test
    public void should_not_be_able_create_playlist_with_expired_token(){
        String payload = "{\n" +
                "    \"name\": \"Playlist API 2\",\n" +
                "    \"description\": \"Descrição API 2\",\n" +
                "    \"public\": true\n" +
                "}";
        given().
                baseUri("https://api.spotify.com").
                basePath("/v1").
                header("Authorization","Bearer " + "1234").
                contentType(ContentType.JSON).
                log().all().
                body(payload).
                when().
                post("/users/22c4ik3nkrsqpzpdcsgfopjfa/playlists").
                then().spec(responseSpecification).
                assertThat().
                statusCode(401).
                body("error.status",equalTo(401),
                        "error.message",equalTo("Invalid access token"));
    }
}
