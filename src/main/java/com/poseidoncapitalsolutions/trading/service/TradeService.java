package com.poseidoncapitalsolutions.trading.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poseidoncapitalsolutions.trading.dto.TradeAddDTO;
import com.poseidoncapitalsolutions.trading.dto.TradeUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.TradeListItemDTO;
import com.poseidoncapitalsolutions.trading.exception.TradeNotFoundException;
import com.poseidoncapitalsolutions.trading.mapper.TradeMapper;
import com.poseidoncapitalsolutions.trading.model.Trade;
import com.poseidoncapitalsolutions.trading.repository.TradeRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class TradeService {
    
    private final TradeRepository tradeRepository;
    private final TradeMapper mapper;

    public List<TradeListItemDTO> getAll(){
        return mapper.toListItemDTOList(tradeRepository.findAll());
    }
    
    @Transactional
    public Trade createTrade(TradeAddDTO tradeAddDTO){
        log.debug("Creating trade from DTO: {}", tradeAddDTO);

        return tradeRepository.save(mapper.toEntity(tradeAddDTO));
    }
          
    @Transactional
    public Trade updateTrade(TradeUpdateDTO tradeUpdateDTO){
        log.debug("Updating trade from DTO: {}", tradeUpdateDTO);

        Trade trade = tradeRepository.findById(tradeUpdateDTO.id())
            .orElseThrow(() -> new TradeNotFoundException("Trade not found with ID: " + tradeUpdateDTO.id()));

        mapper.updateTradeFromDTO(tradeUpdateDTO, trade);

        return tradeRepository.save(trade);
    } 
        
    @Transactional
    public void deleteById(int id){
        log.debug("Deleting trade with id: {}", id);

        Trade trade = tradeRepository.findById(id)
            .orElseThrow(() -> new TradeNotFoundException("Trade not found with ID: " + id));

        tradeRepository.delete(trade);
        log.info("Trade successfully deleted with id: {}", id);
    }
    
    @Transactional(readOnly=true)
    public TradeUpdateDTO getTradeUpdateDTO(int id){
        
        Trade trade = tradeRepository.findById(id)
            .orElseThrow(() -> new TradeNotFoundException("Trade not found with ID: " + id));

        return mapper.toDTO(trade);
    }
}
