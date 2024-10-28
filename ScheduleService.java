package com.example.scheduler.service;

import com.example.scheduler.entity.Room;
import com.example.scheduler.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private RoomRepository roomRepository;

    public String assignSchedule() {
        List<Room> rooms = roomRepository.findAll();

        for (Room room : rooms) {
            if (!room.isOccupied()) {
                room.setOccupied(true);
                room.setAvailableFrom("Next available time");
                roomRepository.save(room);
            }
        }

        return "Schedule created successfully";
    }
}
