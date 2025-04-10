package com.poseidoncapitalsolutions.trading.service;

import java.sql.Timestamp;
import java.util.List;

import com.poseidoncapitalsolutions.trading.model.Bid;
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

/**
 * Service for managing trades.
 */
@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class TradeService {

    private final TradeRepository tradeRepository;
    private final TradeMapper tradeMapper;

    /**
     * Retrieves all trades.
     *
     * @return list of TradeListItemDTO
     */
    public List<TradeListItemDTO> getAllTrades() {
        return tradeMapper.toListItemDTOList(tradeRepository.findAll());
    }

    /**
     * Creates a new trade.
     *
     * @param tradeAddDTO trade data
     * @return created Trade entity
     */
    public Trade createTrade(TradeAddDTO tradeAddDTO) {
        log.debug("Creating trade from DTO: {}", tradeAddDTO);
        Trade newTrade = tradeMapper.toEntity(tradeAddDTO);
        newTrade.setCreationDate(new Timestamp(System.currentTimeMillis()));
        return tradeRepository.save(newTrade);
    }

    /**
     * Updates a trade.
     *
     * @param tradeUpdateDTO trade update data
     * @return updated Trade entity
     * @throws TradeNotFoundException if not found
     */
    public Trade updateTrade(TradeUpdateDTO tradeUpdateDTO) {
        log.debug("Updating trade from DTO: {}", tradeUpdateDTO);
        Trade updatedTrade = tradeRepository.findById(tradeUpdateDTO.id())
                .orElseThrow(() -> new TradeNotFoundException("Trade not found with ID: " + tradeUpdateDTO.id()));
        tradeMapper.updateTradeFromDTO(tradeUpdateDTO, updatedTrade);
        updatedTrade.setRevisionDate(new Timestamp(System.currentTimeMillis()));
        return tradeRepository.save(updatedTrade);
    }

    /**
     * Deletes a trade by ID.
     *
     * @param id trade ID
     * @throws TradeNotFoundException if not found
     */
    public void deleteById(int id) {
        log.debug("Deleting trade with id: {}", id);
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new TradeNotFoundException("Trade not found with ID: " + id));
        tradeRepository.delete(trade);
        log.info("Trade successfully deleted with id: {}", id);
    }

    /**
     * Retrieves a DTO to update a trade.
     *
     * @param id the trade ID
     * @return TradeUpdateDTO
     * @throws TradeNotFoundException if not found
     */
    public TradeUpdateDTO getTradeUpdateDTO(int id) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new TradeNotFoundException("Trade not found with ID: " + id));
        return tradeMapper.toDTO(trade);
    }
}
