package com.poseidoncapitalsolutions.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poseidoncapitalsolutions.trading.model.BidList;


public interface BidListRepository extends JpaRepository<BidList, Integer> {

}
