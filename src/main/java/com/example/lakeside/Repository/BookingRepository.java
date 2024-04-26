package com.example.lakeside.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lakeside.model.BookedRoom;

public interface BookingRepository extends JpaRepository<BookedRoom, Long> {

	List<BookedRoom> findByRoomId(Long roomId);

	 BookedRoom findByBookingConfirmationCode(String confirmationcode);



}
