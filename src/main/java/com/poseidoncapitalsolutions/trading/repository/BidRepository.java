package com.poseidoncapitalsolutions.trading.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidoncapitalsolutions.trading.model.Bid;


@Repository
public interface BidRepository extends JpaRepository<Bid, Integer> {
    Optional<Bid> findByAccount(String account);
}
