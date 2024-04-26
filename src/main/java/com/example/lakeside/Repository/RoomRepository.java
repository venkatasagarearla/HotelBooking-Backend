package com.example.lakeside.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.lakeside.model.Room;

public interface RoomRepository  extends JpaRepository<Room, Long>{

	
	@Query("SELECT DISTINCT r.roomType From Room r")
	List<String> findDistinctRoomTypes();
	
//	List<Room> getAllRooms();

}
