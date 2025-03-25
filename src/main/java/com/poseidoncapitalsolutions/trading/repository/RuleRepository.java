package com.poseidoncapitalsolutions.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poseidoncapitalsolutions.trading.model.Rule;


public interface RuleRepository extends JpaRepository<Rule, Integer> {
}
