package com.example.lakeside.Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.lakeside.Repository.RoomRepository;
import com.example.lakeside.exception.PhotoRetrievalException;
import com.example.lakeside.exception.ResourceNotFoundException;
import com.example.lakeside.model.BookedRoom;
import com.example.lakeside.model.Room;
import com.example.lakeside.response.BookingResponse;
import com.example.lakeside.response.RoomResponse;
import com.example.lakeside.service.BookingService;
import com.example.lakeside.service.BookingServiceImplementation;
import com.example.lakeside.service.RoomService;


import lombok.RequiredArgsConstructor;
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
	private final RoomService roomService;
	private final BookingService bookingService;

	@PostMapping("/add/new-room")
	public ResponseEntity<RoomResponse> addNewRoom(@RequestParam("photo") MultipartFile photo,
			@RequestParam("roomType") String roomType, @RequestParam("roomPrice") BigDecimal roomPrice)  throws SQLException, IOException {
		Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
		RoomResponse response = new RoomResponse(savedRoom.getId(), savedRoom.getRoomType(), savedRoom.getRoomPrice());
		return ResponseEntity.ok(response);
	}
	@GetMapping("/room/type")
	public List<String> getRoomTypes(){
		return roomService.getAllRoomTypes();
	}
	@GetMapping("/rooms/all-rooms")
	public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
		List<Room> rooms=roomService.getAllRooms();
		List<RoomResponse> roomResponses=new ArrayList<>();
		for(Room room:rooms) {
			// method to get room photos by roomId
			byte[] photoBytes=roomService.getRoomPhotoByRoomId(room.getId());
			if(photoBytes!=null && photoBytes.length>0) {
				String base64Photo=Base64.encodeBase64String(photoBytes);
				RoomResponse roomResponse=getRoomResponse(room);
				roomResponse.setPhoto(base64Photo);
				roomResponses.add(roomResponse);
			}
		}
		return ResponseEntity.ok(roomResponses);
		
	}
	@DeleteMapping("delete/room/{roomId}")
	public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId){
		
		roomService.deleteRoom(roomId);
		//HttpStatus.NO_CONTENT 
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
@PutMapping("/update/{roomId}")
public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,@RequestParam(required=false)String roomType,@RequestParam(required=false)BigDecimal roomPrice,@RequestParam(required=false)MultipartFile photo) throws IOException, SQLException{
	byte[] photoBytes=photo!=null &&!photo.isEmpty()?
			photo.getBytes():roomService.getRoomPhotoByRoomId(roomId);
	Blob photoBlob=photoBytes!=null &&photoBytes.length>0?new SerialBlob(photoBytes):null;
	Room theRoom=roomService.updateRoom(roomId,roomType,roomPrice,photoBytes);
	theRoom.setPhoto(photoBlob);
	RoomResponse roomResponse=getRoomResponse(theRoom);
	return ResponseEntity.ok(roomResponse);

	
}
@GetMapping("/roomgetById/{roomId}")
public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId){
	Optional<Room> theRoom=roomService.getRoomById(roomId);
	return theRoom.map(room->{
		RoomResponse roomResponse=getRoomResponse(room);
		return ResponseEntity.ok(Optional.of(roomResponse));
	}).orElseThrow(()->new ResourceNotFoundException("Room not found"));
}

	//to get all the room response from database
	private RoomResponse getRoomResponse(Room room) {
		//booking history and photo of each room
		List<BookedRoom> bookings=getAllBookingsByRoomId(room.getId());
		//we are getting all the bookings for the room from our database then we are trying to convert booked room to booking response
//		List<BookingResponse> bookingInfo=bookings.stream().map(booking ->new BookingResponse(booking.getBookingId(),booking.getCheckInDate(),booking.getCheckOutDate(),booking.getBookingConfirmationCode())).toList();
		byte[] photoBytes=null;
		Blob photoBlob=room.getPhoto();
		if(photoBlob!=null) {
			try {
				photoBytes=photoBlob.getBytes(1, (int) photoBlob.length());
			}catch(SQLException e) {
				throw new PhotoRetrievalException("Error retriving photo");
			}
		}
		return new RoomResponse(room.getId(),room.getRoomType(),room.getRoomPrice(),room.isBooked(), photoBytes);
	}
	// is to return all the  booked rooms from the booked room table
	private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
		
		return  bookingService.getAllBookingsByRoomId(roomId);
	}
	
	@GetMapping("/available-rooms")
	public ResponseEntity<List<RoomResponse>> getAvailableRooms(
	@RequestParam("checkInDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate checkInDate,
	@RequestParam("checkOutDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
	@RequestParam("roomType") String roomType) throws SQLException{
		List<Room> availableRooms=roomService.getAvialbleRoom(checkInDate,checkOutDate,roomType);
		List<RoomResponse> roomRespose=new ArrayList<>();
		for(Room room:availableRooms) {
			byte[] photoBytes=roomService.getRoomPhotoByRoomId(room.getId());
			if(photoBytes!=null && photoBytes.length>0) {
				String photoBase64=Base64.encodeBase64String(photoBytes);
				RoomResponse roomResponse=getRoomResponse(room);
				roomResponse.setPhoto(photoBase64);
				roomRespose.add(roomResponse);
				
			}
		}
		if(roomRespose.isEmpty()) {
			return ResponseEntity.noContent().build();
		}else {
			return ResponseEntity.ok(roomRespose);
		}
	}
	
}
