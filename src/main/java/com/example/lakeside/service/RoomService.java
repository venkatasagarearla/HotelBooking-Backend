package com.example.lakeside.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.example.lakeside.model.Room;

public interface RoomService {

	Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice)throws SQLException, IOException;

	List<String> getAllRoomTypes();

	List<Room> getAllRooms();

	byte[] getRoomPhotoByRoomId(Long roomid) throws SQLException;

	void deleteRoom(Long roomId);

	Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes);

	Optional<Room> getRoomById(Long roomId);

	List<Room> getAvialbleRoom(LocalDate checkInDate, LocalDate checkOutDate, String roomType);



}
