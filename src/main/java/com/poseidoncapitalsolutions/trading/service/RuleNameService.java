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


/**
 * Service for managing rule names.
 */
@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class RuleNameService {

    private final RuleNameRepository ruleNameRepository;
    private final RuleNameMapper ruleNameMapper;

    /**
     * Retrieves all rule names.
     *
     * @return list of RuleNameListItemDTO
     */
    public List<RuleNameListItemDTO> getAll() {
        return ruleNameMapper.toListItemDTOList(ruleNameRepository.findAll());
    }

    /**
     * Creates a new rule name.
     *
     * @param ruleNameAddDTO data to create
     * @return RuleName entity
     */
    public RuleName createRuleName(RuleNameAddDTO ruleNameAddDTO) {
        log.debug("Creating rule name from DTO: {}", ruleNameAddDTO);
        return ruleNameRepository.save(ruleNameMapper.toEntity(ruleNameAddDTO));
    }

    /**
     * Updates a rule name.
     *
     * @param ruleNameUpdateDTO data to update
     * @return updated RuleName
     * @throws RuleNameNotFoundException if not found
     */
    public RuleName updateRuleName(RuleNameUpdateDTO ruleNameUpdateDTO) {
        log.debug("Updating rating from DTO: {}", ruleNameUpdateDTO);
        RuleName updatedRuleName = ruleNameRepository.findById(ruleNameUpdateDTO.id())
                .orElseThrow(() -> new RuleNameNotFoundException("Rule name point not found with ID: " + ruleNameUpdateDTO.id()));
        ruleNameMapper.updateEntityFromDTO(ruleNameUpdateDTO, updatedRuleName);
        return ruleNameRepository.save(updatedRuleName);
    }

    /**
     * Deletes a rule name.
     *
     * @param id the ID to delete
     * @throws RuleNameNotFoundException if not found
     */
    public void deleteById(int id) {
        log.debug("Deleting rule name with id: {}", id);
        RuleName ruleName = ruleNameRepository.findById(id)
                .orElseThrow(() -> new RuleNameNotFoundException("Rule name not found with ID: " + id));
        ruleNameRepository.delete(ruleName);
        log.info("Rule name successfully deleted with id: {}", id);
    }

    /**
     * Retrieves a DTO for updating rule name.
     *
     * @param id the rule name ID
     * @return RuleNameUpdateDTO
     * @throws RuleNameNotFoundException if not found
     */
    public RuleNameUpdateDTO getRuleNameUpdateDTO(int id) {
        RuleName ruleName = ruleNameRepository.findById(id)
                .orElseThrow(() -> new RuleNameNotFoundException("Rule name not found with ID: " + id));
        return ruleNameMapper.toDTO(ruleName);
    }
}