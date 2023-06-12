package com.asldev.uz.roombookingapi.service;

import com.asldev.uz.roombookingapi.enums.RoomType;
import com.asldev.uz.roombookingapi.repository.RoomRepository;
import com.asldev.uz.roombookingapi.repository.entity.Room;
import com.asldev.uz.roombookingapi.service.dto.PageDto;
import com.asldev.uz.roombookingapi.service.dto.RoomDto;
import com.asldev.uz.roombookingapi.service.exception.NotFoundException;
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
                .orElseThrow(() -> new NotFoundException("topilmadi"));
        return mapper.map(room, RoomDto.class);
    }

    public RoomDto create(RoomDto roomDto){
        Room room = mapper.map(roomDto, Room.class);
        Room save = roomRepository.save(room);
        return mapper.map(save, RoomDto.class);
    }

    public RoomDto update(Long id, RoomDto roomDto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("topilmadi"));
        String name = room.getName();
        if (!roomDto.getName().equalsIgnoreCase(name)){
            room.setName(roomDto.getName());
        }
        RoomType type = room.getType();
        if (roomDto.getType() != type){
            room.setType(roomDto.getType());
        }
        int capacity = room.getCapacity();
        if (roomDto.getCapacity() != capacity){
            room.setCapacity(roomDto.getCapacity());
        }
        Room save = roomRepository.save(room);
        return mapper.map(save, RoomDto.class);
    }
    public void delete(Long id){
        Room byId = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("topilmadi"));
        roomRepository.delete(byId);
    }
}

