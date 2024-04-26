package com.example.lakeside.service;

import java.util.List;
import java.util.Optional;

import com.example.lakeside.model.BookedRoom;
import com.example.lakeside.response.BookingResponse;

public interface BookingService {

	List<BookedRoom> getAllBookedRooms();

	
	String saveBooking(Long id, BookedRoom bookingRequest);




	void cancleBooking(Long bookingId);


	List<BookedRoom> getAllBookingsByRoomId(Long roomId);


	BookedRoom findByBookingConfirmationCode(String confirmationcode);


	

//	   BookedRoom findByBookingConfirmationCode(String confirmationCode);


	


	

}
