package com.example.lakeside.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lakeside.exception.InvalidBookingRequestException;
import com.example.lakeside.exception.ResourceNotFoundException;
import com.example.lakeside.model.BookedRoom;
import com.example.lakeside.model.Room;
import com.example.lakeside.response.BookingResponse;
import com.example.lakeside.response.RoomResponse;
import com.example.lakeside.service.BookingService;
import com.example.lakeside.service.RoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {
	private final BookingService bookingService;
	private final RoomService roomService;
//	@GetMapping("/allBookings")
//	 public List<BookedRoom> bookings(){
//		 List<BookedRoom> bookedRooms=bookingService.getAllBookedRooms();
//		 return bookedRooms;
//	 }
	@GetMapping("/allBookings")
	public ResponseEntity<List<BookingResponse>> getAllBooking(){
		 List<BookedRoom> bookings=bookingService.getAllBookedRooms();
		 List<BookingResponse> bookingResponse=new ArrayList<>();
		 for(BookedRoom booking:bookings) {
			 //converting each booked room into bookingresponse
			 BookingResponse response=getBookingResponse(booking);
			 //adding the booking response into list
			 bookingResponse.add(response);
		 }
		return ResponseEntity.ok(bookingResponse);
	}
	
	

	@GetMapping("/confirmation/{confirmationCode}")
	public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationcode ){
		try {
			 BookedRoom booking=bookingService.findByBookingConfirmationCode(confirmationcode);
			 
			 BookingResponse bookingResponse=getBookingResponse(booking);
			 return ResponseEntity.ok(bookingResponse);
		}catch(ResourceNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
			
		}
	}
	@PostMapping("/bookroom/{id}")
	public ResponseEntity<?> saveBooking(@PathVariable Long id,@RequestBody BookedRoom bookingRequest){
		 try {
			 String confirmationCode=bookingService.saveBooking(id,bookingRequest);
			 return ResponseEntity.ok(
					 "Room booked sucessfully,your booking conformation code is:"+confirmationCode);
		 }catch(InvalidBookingRequestException e) {
			 return ResponseEntity.badRequest().body(e.getMessage());
			 
		 }
		
	}
	@DeleteMapping("/{bookingId}/delete")
	public void cancleBooking(@PathVariable Long bookingId) {
		bookingService.cancleBooking(bookingId);
	}
	
	private BookingResponse getBookingResponse(BookedRoom booking) {
		 
		Room theRoom= roomService.getRoomById(booking.getRoom().getId()).get();
		RoomResponse room=new RoomResponse(theRoom.getId(),theRoom.getRoomType(),theRoom.getRoomPrice());
		return new BookingResponse(booking.getBookingId(),
				booking.getCheckInDate(),
				booking.getCheckOutDate(),
				booking.getGuestFullName(),
				booking.getGuestEmail(),
				booking.getNumOfChildren(),
				booking.getNumofAdults(),
				booking.getTotalNumofGuest(),
				booking.getBookingConfirmationCode(),room);
	}
	
}
