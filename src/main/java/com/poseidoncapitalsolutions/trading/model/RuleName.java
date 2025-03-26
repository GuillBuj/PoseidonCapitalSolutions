package com.poseidoncapitalsolutions.trading.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rulename")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleName {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    String description;

    @Column(name = "json")
    String json;

    @Column(name = "template")
    String template;

    @Column(name = "sqlStr")
    String sqlStr;

    @Column(name = "sqlPart")
    String sqlPart;

}
