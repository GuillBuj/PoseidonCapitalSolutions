package com.poseidoncapitalsolutions.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poseidoncapitalsolutions.trading.model.CurvePoint;


public interface CurvePointRepository extends JpaRepository<CurvePoint, Integer> {

}
