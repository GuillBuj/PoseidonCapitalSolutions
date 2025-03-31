package com.poseidoncapitalsolutions.trading.service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poseidoncapitalsolutions.trading.dto.RuleNameAddDTO;
import com.poseidoncapitalsolutions.trading.dto.RuleNameUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.RuleNameListItemDTO;
import com.poseidoncapitalsolutions.trading.exception.RuleNameNotFoundException;
import com.poseidoncapitalsolutions.trading.mapper.RuleNameMapper;
import com.poseidoncapitalsolutions.trading.model.RuleName;
import com.poseidoncapitalsolutions.trading.repository.RuleNameRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class RuleNameService {
    
    private final RuleNameRepository ruleNameRepository;
    private final RuleNameMapper ruleNameMapper;
    
    public List<RuleNameListItemDTO> getAll(){
        return ruleNameMapper.toListItemDTOList(ruleNameRepository.findAll());
    }

    @Transactional
    public RuleName createRuleName(RuleNameAddDTO ruleNameAddDTO){
        log.debug("Creating rule name from DTO: {}", ruleNameAddDTO);

        return ruleNameRepository.save(ruleNameMapper.toEntity(ruleNameAddDTO));
    }
        
       
    @Transactional
    public RuleName updateRuleName(RuleNameUpdateDTO ruleNameUpdateDTO){
        log.debug("Updating rating from DTO: {}", ruleNameUpdateDTO);

        RuleName ruleName = ruleNameRepository.findById(ruleNameUpdateDTO.id())
            .orElseThrow(()-> new RuleNameNotFoundException("Rule name point not found with ID: " + ruleNameUpdateDTO.id()));

        ruleNameMapper.updateEntityFromDTO(ruleNameUpdateDTO, ruleName);

        return ruleNameRepository.save(ruleName);
    }
    
    @Transactional
    public void deleteById(int id){
        log.debug("Deleting rule name with id: {}", id);

        RuleName ruleName = ruleNameRepository.findById(id)
            .orElseThrow(()-> new RuleNameNotFoundException("Rule name not found with ID: " + id));

        ruleNameRepository.delete(ruleName);
        log.info("Rule name successfully deleted with id: {}", id);
    }
    
    @Transactional(readOnly = true)
    public RuleNameUpdateDTO getRuleNameUpdateDTO(int id){

        RuleName ruleName = ruleNameRepository.findById(id)
            .orElseThrow(()-> new RuleNameNotFoundException("Rule name not found with ID: " + id));

        return ruleNameMapper.toDTO(ruleName);
    }
}