package com.poseidoncapitalsolutions.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poseidoncapitalsolutions.trading.model.Trade;


public interface TradeRepository extends JpaRepository<Trade, Integer> {
    
}
