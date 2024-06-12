package ch.fhnw.swc.mrs.controller;

import ch.fhnw.swc.mrs.Application;
import ch.fhnw.swc.mrs.util.StatusCodes;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("integration")
class ITRentalController {

    private String baseUrl = "http://localhost:";

    @BeforeAll
    static void startApplication() throws Exception {
        Application.main(null);
    }

    @BeforeEach
    void setPort() throws Exception {
        baseUrl = baseUrl + Application.getPort();
    }

    @DisplayName("Delete rental")
    @Test
    void testDeleteRental() {
        String json = get(baseUrl + "/rentals").asString();
        int elementsBefore = new JsonPath(json).getInt("size()");

        given().when().delete(baseUrl + "/rentals/5").then()
                .statusCode(StatusCodes.NO_CONTENT);

        json = get(baseUrl + "/rentals").asString();
        int elementsAfter = new JsonPath(json).getInt("size()");
        assertEquals(elementsBefore, elementsAfter + 1);
    }

    @DisplayName("Create rental")
    @Test
    void testCreateRental() {
        String bodyContent = "{\r\n"
                + "        \"userId\": \"1\",\r\n"
                + "        \"movieId\" : \"8\",\r\n"
                + "        \"rentalDate\" : \"2020-06-10\"\r\n"
                + "    }";

        String json1 = get(baseUrl + "/rentals").asString();
        int elementsBefore = new JsonPath(json1).getInt("size()");

        given().
                body(bodyContent)
                .when()
                .post(baseUrl + "/rentals")
                .then()
                .statusCode(StatusCodes.CREATED)
                .body("userId", equalTo(1))
                .body("$", hasKey("id")); // verify that the id of the new rental is returned

        String json2 = get(baseUrl + "/rentals").asString();
        int elementsAfter = new JsonPath(json2).getInt("size()");
        assertEquals(elementsBefore, elementsAfter - 1);
    }

    @AfterAll
    static void stopSpark() throws Exception {
        Application.stop();
        Thread.sleep(1000);
    }

}
