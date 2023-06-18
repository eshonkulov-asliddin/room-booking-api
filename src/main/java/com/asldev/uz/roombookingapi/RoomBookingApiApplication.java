package com.asldev.uz.roombookingapi;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RoomBookingApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomBookingApiApplication.class, args);
    }

    @Bean
    public GroupedOpenApi usersGroup() {
        return GroupedOpenApi.builder().group("users")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Booking API")))
                .packagesToScan("com.asldev.uz.roombookingapi.controller")
                .build();
    }
}
