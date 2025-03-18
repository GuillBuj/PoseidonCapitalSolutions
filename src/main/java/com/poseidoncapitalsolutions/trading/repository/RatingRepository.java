package com.poseidoncapitalsolutions.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poseidoncapitalsolutions.trading.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

}
