package com.asldev.uz.roombookingapi.service.validator;

import com.asldev.uz.roombookingapi.enums.RoomType;
import com.asldev.uz.roombookingapi.service.dto.RoomDto;
import com.asldev.uz.roombookingapi.service.exception.ArgumentIsNotValidException;
import com.asldev.uz.roombookingapi.service.utils.ConstantMessages;

public class Validator {
    public static void validate(RoomDto roomDto){
        String name = roomDto.getName();
        if (name == null || name.isEmpty()){
            throw new ArgumentIsNotValidException(ConstantMessages.BAD_REQUEST);
        }
        RoomType type = roomDto.getType();
        if (type == null){
            throw new ArgumentIsNotValidException(ConstantMessages.BAD_REQUEST);
        }

        int capacity = roomDto.getCapacity();
        if (capacity <= 0){
            throw new ArgumentIsNotValidException(ConstantMessages.BAD_REQUEST);
        }
    }
}
