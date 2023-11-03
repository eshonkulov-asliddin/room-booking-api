package com.asldev.uz.roombookingapi.service.validator;

import com.asldev.uz.roombookingapi.enums.RoomType;
import com.asldev.uz.roombookingapi.service.dto.RoomDtoUpdate;
import com.asldev.uz.roombookingapi.service.exception.ArgumentIsNotValidException;
import com.asldev.uz.roombookingapi.service.utils.ConstantMessages;

public class RoomUpdateValidator implements Validator<RoomDtoUpdate> {
    @Override
    public void validate(RoomDtoUpdate roomDtoUpdate) {
        Long id = roomDtoUpdate.getId();
        if (id == null){
            throw new ArgumentIsNotValidException(ConstantMessages.BAD_REQUEST);
        }
        String name = roomDtoUpdate.getName();
        if (name == null || name.isEmpty()){
            throw new ArgumentIsNotValidException(ConstantMessages.BAD_REQUEST);
        }
        RoomType type = roomDtoUpdate.getType();
        if (type == null){
            throw new ArgumentIsNotValidException(ConstantMessages.BAD_REQUEST);
        }

        int capacity = roomDtoUpdate.getCapacity();
        if (capacity <= 0){
            throw new ArgumentIsNotValidException(ConstantMessages.BAD_REQUEST);
        }
    }
}
