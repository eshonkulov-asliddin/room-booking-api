package com.asldev.uz.roombookingapi;

import com.asldev.uz.roombookingapi.enums.RoomType;
import com.asldev.uz.roombookingapi.repository.entity.Resident;
import com.asldev.uz.roombookingapi.repository.entity.Room;
import com.asldev.uz.roombookingapi.service.dto.BookingDto;
import com.asldev.uz.roombookingapi.service.utils.ConstantMessages;
import io.restassured.http.Header;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class RoomBookingEndpointTest {

    @BeforeAll
    public static void setUp(){
        baseURI = "http://localhost:8080";
        basePath = "/api/rooms";
    }

    @Test
    public void givenCustomRoomObj_whenObjValid_thenCreateRoom(){
        given()
            .body(new Room("ybky", RoomType.team, 14))
            .header(new Header("x-custom-header", "value"))
            .contentType("application/json")
        .when()
            .post("")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("name", equalTo("ybky"))
            .body("type", equalTo(RoomType.team.toString()))
            .body("capacity", equalTo(14));
    }

    @Test
    public void givenRoomIdAndCustomBookingObj_whenIdAndBookingTimesAreValid_thenBookARoom(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ConstantMessages.LOCAL_DATE_TIME_FORMATTER);
        String start = LocalDateTime.now().format(formatter);
        String end = LocalDateTime.now().plusHours(2).format(formatter);
        given()
                .pathParam("roomId", 1)
                .body(new BookingDto(start, end, new Resident("Asliddin Eshonkulov")))
                .contentType("application/json")
        .when()
                .post("/{roomId}/book")
        .then()
                .statusCode(201);

    }
    @Test
    public void givenRoomId_whenRoomHasAvailableTimes_thenSuccess(){
        given()
            .pathParam("roomId", 1)
        .when()
            .get("{roomId}/availability")
        .then()
            .statusCode(200);
    }

    @Test
    public void givenRoomId_whenDelete_thenSuccess(){
        given()
                .pathParam("id", 1)
                .when()
                .delete("/{id}")
                .then()
                .statusCode(204);
    }
}
