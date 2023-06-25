package com.asldev.uz.roombookingapi.service;

import com.asldev.uz.roombookingapi.enums.RoomType;
import com.asldev.uz.roombookingapi.repository.RoomRepository;
import com.asldev.uz.roombookingapi.repository.entity.Room;
import com.asldev.uz.roombookingapi.service.dto.PageDto;
import com.asldev.uz.roombookingapi.service.dto.RoomDtoRequest;
import com.asldev.uz.roombookingapi.service.dto.RoomDtoResponse;
import com.asldev.uz.roombookingapi.service.dto.RoomDtoUpdate;
import com.asldev.uz.roombookingapi.service.exception.NotFoundException;
import com.asldev.uz.roombookingapi.service.utils.ConstantMessages;
import com.asldev.uz.roombookingapi.service.validator.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final ModelMapper mapper;

    @Autowired
    public RoomService(RoomRepository roomRepository){
        this.roomRepository = roomRepository;
        this.mapper = new ModelMapper();
    }

    public PageDto findAll(String search, RoomType type, int pageNumber, int pageSize){
        Slice<Room> all;
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        if (search != null && type != null) {
            all = roomRepository.findByNameAndType(search, type, paging);
        }else if (search != null){
            all = roomRepository.findByName(search, paging);
        }else if (type != null){
            all = roomRepository.findByType(type, paging);
        }else {
            all = roomRepository.findAll(paging);
        }

        PageDto pageDto = new PageDto();
        List<RoomDtoResponse> allContents = all.getContent()
                .stream()
                .map(content -> mapper.map(content, RoomDtoResponse.class))
                .toList();
        pageDto.setPage(pageNumber);
        pageDto.setCount(all.getNumberOfElements());
        pageDto.setPage_size(pageSize);
        pageDto.setResults(allContents);

        return pageDto;
    }

    public RoomDtoResponse findById(Long id){
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstantMessages.NOT_FOUND));
        return mapper.map(room, RoomDtoResponse.class);
    }

    public RoomDtoResponse create(RoomDtoRequest createRequest){
        Validator.validate(createRequest);

        Room room = mapper.map(createRequest, Room.class);
        Room save = roomRepository.save(room);
        return mapper.map(save, RoomDtoResponse.class);
    }

    public RoomDtoResponse update(Long id, RoomDtoUpdate updateRequest) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstantMessages.NOT_FOUND));

        Validator.validate(updateRequest);

        String name = room.getName();
        String dtoName = updateRequest.getName();
        if (!dtoName.equalsIgnoreCase(name)){
            room.setName(dtoName);
        }
        RoomType type = room.getType();
        RoomType dtoType = updateRequest.getType();
        if (dtoType != type){
            room.setType(dtoType);
        }
        int capacity = room.getCapacity();
        int dtoCapacity = updateRequest.getCapacity();
        if (dtoCapacity != capacity){
            room.setCapacity(dtoCapacity);
        }
        Room save = roomRepository.save(room);
        return mapper.map(save, RoomDtoResponse.class);
    }
    public void delete(Long id){
        Room byId = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstantMessages.NOT_FOUND));
        roomRepository.delete(byId);
    }
}

