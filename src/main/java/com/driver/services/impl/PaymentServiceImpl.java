package com.driver.services.impl;

import com.driver.Entities.Payment;
import com.driver.Entities.Reservation;
import com.driver.model.PaymentMode;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation=null;
        Optional<Reservation> optionalReservation=reservationRepository2.findById(reservationId);
        if(optionalReservation.isPresent()){
            reservation=optionalReservation.get();
        }
        else throw new Exception("Reservation id not available");
        if(amountSent<reservation.getSpot().getPricePerHour()*reservation.getNumberOfHours()){
            throw new Exception("Insufficient Amount");
        }
        PaymentMode paymentMode=null;
        if(mode.toUpperCase().equals(PaymentMode.CARD.toString())){
            paymentMode=PaymentMode.CARD;
        }
        else if(mode.toUpperCase().equals(PaymentMode.UPI.toString())){
            paymentMode=PaymentMode.UPI;
        }
        else if(mode.toUpperCase().equals(PaymentMode.CASH.toString())){
            paymentMode=PaymentMode.CASH;
        }
        else{
            throw new Exception("Payment mode not detected");
        }
        Payment payment=new Payment();
        payment.setPaymentMode(paymentMode);
        payment.setPaymentCompleted(true);
        payment.setReservation(reservation);
        reservation.setPayment(payment);
        paymentRepository2.save(payment);
        return payment;
    }
}
