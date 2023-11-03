package com.asldev.uz.roombookingapi;

import com.asldev.uz.roombookingapi.enums.RoomType;
import com.asldev.uz.roombookingapi.repository.entity.Resident;
import com.asldev.uz.roombookingapi.service.dto.BookingDtoRequest;
import com.asldev.uz.roombookingapi.service.dto.RoomDtoRequest;
import com.asldev.uz.roombookingapi.service.utils.ConstantMessages;
import io.restassured.http.Header;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoomBookingEndpointTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "http://localhost:";
    private static final String COUSTOM_HEADER_VALUE = "value";
    private static final String COUSTOM_HEADER_NAME = "x-coustom-header";


    @Test
    @Order(1)
    public void givenCustomRoomObj_whenObjIsValid_thenCreateRoom(){
        String url = BASE_URL + port + "/api/rooms";

        given()
            .body(new RoomDtoRequest("ybky", RoomType.team, 14))
            .header(new Header("x-custom-header", "value"))
            .contentType("application/json")
        .when()
            .post(url)
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("name", equalTo("ybky"))
            .body("type", equalTo(RoomType.team.toString()))
            .body("capacity", equalTo(14));
    }

    @Test
    @Order(2)
    public void givenRoomIdAndCustomBookingObj_whenIdAndBookingTimesAreValid_thenBookARoom(){
        String url = BASE_URL + port + "/api/rooms/{roomId}/book/";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConstantMessages.LOCAL_DATE_TIME_FORMATTER);
        String start = LocalDateTime.now().format(formatter);
        String end = LocalDateTime.now().plusHours(2).format(formatter);
        given()
                .pathParam("roomId", 1)
                .body(new BookingDtoRequest(start, end, new Resident("Asliddin Eshonkulov")))
                .contentType("application/json")
        .when()
                .post(url)
        .then()
                .statusCode(201);

    }
    @Test
    @Order(3)
    public void givenRoomId_whenRoomHasAvailableTimes_thenSuccess(){
        String url = BASE_URL + port + "/api/rooms/{roomId}/availability";

        given()
            .pathParam("roomId", 1)
        .when()
            .get(url)
        .then()
            .statusCode(200);
    }

    @Test
    @Order(4)
    public void givenBookingId_whenDelete_thenSuccess(){
        String url = BASE_URL + port + "/api/rooms/bookings/{id}";

        given()
            .pathParam("id", 1)
        .when()
            .delete(url)
        .then()
            .statusCode(204);
}

    @Test
    @Order(5)
    public void givenRoomId_whenDelete_thenSuccess(){
        String url = BASE_URL + port + "/api/rooms/{id}";

        given()
            .pathParam("id", 1)
        .when()
            .delete(url)
        .then()
            .statusCode(204);
    }
}
