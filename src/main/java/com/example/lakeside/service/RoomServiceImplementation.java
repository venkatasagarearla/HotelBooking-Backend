package com.example.lakeside.service;

import java.io.IOException;
import java.math.BigDecimal;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.lakeside.Repository.RoomRepository;
import com.example.lakeside.exception.InternalServerException;
import com.example.lakeside.exception.ResourceNotFoundException;
import com.example.lakeside.model.Room;

import lombok.RequiredArgsConstructor;

//import javax.sql.rowset.serial.SerialBlob;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class RoomServiceImplementation  implements RoomService{
  
	private final RoomRepository roomRepository;

	
	
	@Override
	public List<String> getAllRoomTypes() {
		// TODO Auto-generated method stub
		return roomRepository.findDistinctRoomTypes();
	}
	
	@Override
	public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws SQLException, IOException {
		Room room=new Room();
		room.setRoomType(roomType);
		room.setRoomPrice(roomPrice);
		if(!file.isEmpty()) {
			byte[] photoBytes=file.getBytes();
			Blob photoBlob=new SerialBlob(photoBytes);
			room.setPhoto(photoBlob);
		}
		return roomRepository.save(room);
	}

	@Override
	public List<Room> getAllRooms() {
		
		return roomRepository.findAll();
	}

	@Override
	public byte[] getRoomPhotoByRoomId(Long roomid) throws SQLException {
	    Optional<Room> theRoom=roomRepository.findById(roomid);
	    if(theRoom.isEmpty()) {
	    	throw new ResourceNotFoundException("sorry,Room not found");
	    }
	    Blob photoBlob=theRoom.get().getPhoto();
	    if(photoBlob!=null) {
	    	return photoBlob.getBytes(1, (int)photoBlob.length());
	    }
		return null;
	}

	@Override
	public void deleteRoom(Long roomId) {
		Optional<Room> theRoom=roomRepository.findById(roomId);
		if(theRoom.isPresent()) {
			roomRepository.deleteById(roomId);
		}
		
	}

	@Override
	public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) {
		// TODO Auto-generated method stub
				Room room=roomRepository.findById(roomId).orElseThrow(()->new ResourceNotFoundException("Room not found"));
				if(roomType!=null) room.setRoomType(roomType);
				if(roomPrice!=null) room.setRoomPrice(roomPrice);
			if(photoBytes!=null && photoBytes.length>0) {
			try {
				room.setPhoto((new SerialBlob(photoBytes)));
			}
			
			catch(SQLException ex) {	
				throw new InternalServerException("Error updating room");
			}
			}

		  return roomRepository.save(room);
	}

	@Override
	public Optional<Room> getRoomById(Long roomId) {
		// TODO Auto-generated m
		return Optional.of(roomRepository.findById(roomId).get());
	}


}
