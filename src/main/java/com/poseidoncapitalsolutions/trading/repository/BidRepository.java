package com.poseidoncapitalsolutions.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poseidoncapitalsolutions.trading.model.Bid;


public interface BidRepository extends JpaRepository<Bid, Integer> {

}
