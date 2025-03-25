package com.poseidoncapitalsolutions.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poseidoncapitalsolutions.trading.model.CurvePoint;

@Repository
public interface CurvePointRepository extends JpaRepository<CurvePoint, Integer>{

}
