package com.asldev.uz.roombookingapi.service;

import com.asldev.uz.roombookingapi.enums.RoomType;
import com.asldev.uz.roombookingapi.repository.RoomRepository;
import com.asldev.uz.roombookingapi.repository.entity.Room;
import com.asldev.uz.roombookingapi.service.dto.PageDto;
import com.asldev.uz.roombookingapi.service.dto.RoomDto;
import com.asldev.uz.roombookingapi.service.exception.NotFoundException;
import com.asldev.uz.roombookingapi.service.utils.ConstantMessages;
import com.asldev.uz.roombookingapi.service.validator.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

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
        pageDto.setPage(pageNumber);
        pageDto.setCount(all.getNumberOfElements());
        pageDto.setPageSize(pageSize);
        pageDto.setResults(all.getContent());

        return pageDto;
    }

    public RoomDto findById(Long id){
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstantMessages.NOT_FOUND));
        return mapper.map(room, RoomDto.class);
    }

    public RoomDto create(RoomDto roomDto){
        Validator.validate(roomDto);

        String dtoName = roomDto.getName();
        RoomType dtoType = roomDto.getType();
        int dtoCapacity = roomDto.getCapacity();
        if ( dtoName == null || dtoName.isEmpty() ||
             dtoType == null ||
             dtoCapacity <= 0 )
        {
            throw new IllegalArgumentException();
        }
        Room room = mapper.map(roomDto, Room.class);
        Room save = roomRepository.save(room);
        return mapper.map(save, RoomDto.class);
    }

    public RoomDto update(Long id, RoomDto roomDto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstantMessages.NOT_FOUND));

        Validator.validate(roomDto);

        String name = room.getName();
        String dtoName = roomDto.getName();
        if (!dtoName.equalsIgnoreCase(name)){
            room.setName(dtoName);
        }
        RoomType type = room.getType();
        RoomType dtoType = roomDto.getType();
        if (dtoType != type){
            room.setType(dtoType);
        }
        int capacity = room.getCapacity();
        int dtoCapacity = roomDto.getCapacity();
        if (dtoCapacity != capacity){
            room.setCapacity(dtoCapacity);
        }
        Room save = roomRepository.save(room);
        return mapper.map(save, RoomDto.class);
    }
    public void delete(Long id){
        Room byId = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ConstantMessages.NOT_FOUND));
        roomRepository.delete(byId);
    }
}

