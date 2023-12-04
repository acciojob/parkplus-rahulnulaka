package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        Optional<User> optionalUser=userRepository3.findById(userId);
        User user=null;
        if(optionalUser.isPresent()){
            user=optionalUser.get();
        }
        else throw new Exception("Cannot make reservation");
        Optional<ParkingLot> optionalParkingLot=parkingLotRepository3.findById(parkingLotId);
        ParkingLot parkingLot=null;
        if(optionalParkingLot.isPresent()){
            parkingLot=optionalParkingLot.get();
        }
        else throw new Exception("Cannot make reservation");
        Spot reservedSpot=null;
        int minimizedCost=Integer.MAX_VALUE;
        for(Spot spot: parkingLot.getSpotList()){
            int wheels=0;
            if(spot.getSpotType()==SpotType.TWO_WHEELER){
                wheels=2;
            }
            if(spot.getSpotType()==SpotType.FOUR_WHEELER){
                wheels=4;
            }
            if(spot.getSpotType()==SpotType.OTHERS){
                wheels=24;
            }
            if(!spot.getOccupied()&&numberOfWheels<=wheels&&spot.getPricePerHour()*timeInHours<minimizedCost){
                minimizedCost=spot.getPricePerHour()*timeInHours;
                reservedSpot=spot;
            }
        }
        if(reservedSpot==null){
            throw new Exception("Cannot make reservation");
        }
        Reservation reservation=new Reservation();
        reservation.setNumberOfHours(timeInHours);
        reservation.setUser(user);
        reservation.setSpot(reservedSpot);

        reservedSpot.setOccupied(true);
        user.getReservationList().add(reservation);
        reservedSpot.getReservationList().add(reservation);

        userRepository3.save(user);
        spotRepository3.save(reservedSpot);
        return reservation;
    }
}
