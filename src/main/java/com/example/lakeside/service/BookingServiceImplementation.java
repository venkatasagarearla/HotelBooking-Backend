package com.example.lakeside.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.lakeside.Repository.BookingRepository;
import com.example.lakeside.Repository.RoomRepository;
import com.example.lakeside.exception.InvalidBookingRequestException;
import com.example.lakeside.exception.ResourceNotFoundException;
import com.example.lakeside.model.BookedRoom;
import com.example.lakeside.model.Room;
import com.example.lakeside.response.BookingResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImplementation  implements BookingService{
	private final BookingRepository bookingRepository;
	private final RoomService roomservice;
//	public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
//		
//		return null;
//	}
	//1
	@Override
	public List<BookedRoom> getAllBookedRooms() {
		List<BookedRoom> bookedRooms=bookingRepository.findAll();
		return bookedRooms;
		
	}
	//2
	
	@Override
	public String saveBooking(Long id, BookedRoom bookingRequest) {
		if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
			throw new InvalidBookingRequestException("check-in date must come before check-out date");
			
		}
		Room room=roomservice.getRoomById(id).get();
		//extract the all the bookings of the that room
		List<BookedRoom> existingBookings=room.getBookings();
		boolean roomIsAvailable=roomIsAvailable(bookingRequest,existingBookings);
		if(roomIsAvailable) {
			room.addBooking(bookingRequest);
		    bookingRepository.save(bookingRequest);
		}else {
			throw new InvalidBookingRequestException("Sorry,This room is not available for the selected dates");
		}
		return bookingRequest.getBookingConfirmationCode();
	}
	

	
	@Override
	public void cancleBooking(Long bookingId) {
		bookingRepository.deleteById(bookingId);
		
	}

	@Override
	public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
	  return bookingRepository.findByRoomId(roomId);
		
	}
	private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
		// TODO Auto-generated method stub
		 return existingBookings.stream()
                .noneMatch(existingBooking ->
                bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                        || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                        || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                        && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                        || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                        && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                        && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                        || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                        && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
        );
	}

	@Override
	public BookedRoom findByBookingConfirmationCode(String bookingConfirmationCode) {
		  return bookingRepository.findByBookingConfirmationCode(bookingConfirmationCode)
	                .orElseThrow(() -> new ResourceNotFoundException("No booking found with booking code :"+bookingConfirmationCode));
      
	}

	
	
	// BookedRoom findByBookingConfirmationCode(String confirmationCode);

}
