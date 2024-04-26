package com.example.lakeside.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookedRoom {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long bookingId;
    @Column(name = "check_In")
	private LocalDate checkInDate;
    @Column(name = "check_Out")
	private LocalDate checkOutDate;
    @Column(name = "guest_FullName")
	private String guestFullName;
    @Column(name = "guest_Email")
	private String guestEmail;
    @Column(name = "children")
	private int NumOfChildren;
    @Column(name = "Adults")
	private int NumofAdults;
    @Column(name = "total_guest")
	private int totalNumofGuest;
    @Column(name = "confirmation_code")
	private String bookingConfirmationCode;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "room_id")
	private Room room;
	
	public void calculateTotalNumberOfGuest() {
		this.totalNumofGuest=this.NumOfChildren+this.NumofAdults;
	}

	public void setNumOfChildren(int numOfChildren) {
		this.NumOfChildren = numOfChildren;
		calculateTotalNumberOfGuest();
	}

	public void setNumofAdults(int numofAdults) {
		this.NumofAdults = numofAdults;
		calculateTotalNumberOfGuest();
	}

	public void setBookingConfirmationCode(String bookingConfirmationCode) {
		this.bookingConfirmationCode = bookingConfirmationCode;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	
	
	
	
	
}
