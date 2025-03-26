package com.poseidoncapitalsolutions.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poseidoncapitalsolutions.trading.model.RuleName;


public interface RuleNameRepository extends JpaRepository<RuleName, Integer> {
}
