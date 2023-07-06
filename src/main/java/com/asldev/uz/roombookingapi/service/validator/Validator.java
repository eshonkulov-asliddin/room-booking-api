package com.asldev.uz.roombookingapi.service.validator;

import com.asldev.uz.roombookingapi.enums.RoomType;
import com.asldev.uz.roombookingapi.service.dto.RoomDtoRequest;
import com.asldev.uz.roombookingapi.service.dto.RoomDtoUpdate;
import com.asldev.uz.roombookingapi.service.exception.ArgumentIsNotValidException;
import com.asldev.uz.roombookingapi.service.utils.ConstantMessages;

public class Validator {
    public static void validate(RoomDtoRequest roomDtoRequest){
        String name = roomDtoRequest.getName();
        if (name == null || name.isEmpty()){
            throw new ArgumentIsNotValidException(ConstantMessages.BAD_REQUEST);
        }
        RoomType type = roomDtoRequest.getType();
        if (type == null){
            throw new ArgumentIsNotValidException(ConstantMessages.BAD_REQUEST);
        }

        int capacity = roomDtoRequest.getCapacity();
        if (capacity <= 0){
            throw new ArgumentIsNotValidException(ConstantMessages.BAD_REQUEST);
        }
    }

    public static void validate(RoomDtoUpdate roomDtoRequest){
        Long id = roomDtoRequest.getId();
        if (id == null){
            throw new ArgumentIsNotValidException(ConstantMessages.BAD_REQUEST);
        }
        String name = roomDtoRequest.getName();
        if (name == null || name.isEmpty()){
            throw new ArgumentIsNotValidException(ConstantMessages.BAD_REQUEST);
        }
        RoomType type = roomDtoRequest.getType();
        if (type == null){
            throw new ArgumentIsNotValidException(ConstantMessages.BAD_REQUEST);
        }

        int capacity = roomDtoRequest.getCapacity();
        if (capacity <= 0){
            throw new ArgumentIsNotValidException(ConstantMessages.BAD_REQUEST);
        }
    }
}
