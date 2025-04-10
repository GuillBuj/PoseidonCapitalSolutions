package com.poseidoncapitalsolutions.trading.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poseidoncapitalsolutions.trading.dto.BidAddDTO;
import com.poseidoncapitalsolutions.trading.dto.BidUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.BidListItemDTO;
import com.poseidoncapitalsolutions.trading.exception.BidNotFoundException;
import com.poseidoncapitalsolutions.trading.mapper.BidMapper;
import com.poseidoncapitalsolutions.trading.model.Bid;
import com.poseidoncapitalsolutions.trading.repository.BidRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for managing bids.
 */
@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class BidService {

    private final BidRepository bidRepository;
    private final BidMapper bidMapper;

    /**
     * Retrieves all bids as a list of DTOs.
     *
     * @return list of BidListItemDTO
     */
    public List<BidListItemDTO> getAllBids() {
        return bidMapper.toListItemDTOList(bidRepository.findAll());
    }

    /**
     * Creates a new bid from a DTO.
     *
     * @param bidAddDTO the DTO containing bid data
     * @return the created Bid entity
     */
    public Bid createBid(BidAddDTO bidAddDTO) {
        log.debug("Creating bid from DTO: {}", bidAddDTO);
        Bid newBid = bidMapper.toEntity(bidAddDTO);
        newBid.setCreationDate(new Timestamp(System.currentTimeMillis()));
        return bidRepository.save(newBid);
    }

    /**
     * Updates an existing bid.
     *
     * @param bidUpdateDTO the DTO containing updated bid data
     * @return the updated Bid entity
     * @throws BidNotFoundException if bid does not exist
     */
    public Bid updateBid(BidUpdateDTO bidUpdateDTO) {
        log.debug("Updating bid from DTO: {}", bidUpdateDTO);
        Bid updatedBid = bidRepository.findById(bidUpdateDTO.id())
                .orElseThrow(() -> new BidNotFoundException("Bid not found with ID: " + bidUpdateDTO.id()));
        bidMapper.updateBidFromDTO(bidUpdateDTO, updatedBid);
        updatedBid.setRevisionDate(new Timestamp(System.currentTimeMillis()));
        return bidRepository.save(updatedBid);
    }

    /**
     * Deletes a bid by its ID.
     *
     * @param id the ID of the bid
     * @throws BidNotFoundException if bid does not exist
     */
    public void deleteById(int id) {
        log.debug("Deleting bid with id: {}", id);
        Bid bid = bidRepository.findById(id)
                .orElseThrow(() -> new BidNotFoundException("Bid not found with ID: " + id));
        bidRepository.delete(bid);
        log.info("Bid successfully deleted with id: {}", id);
    }

    /**
     * Retrieves a BidUpdateDTO by bid ID.
     *
     * @param id the ID of the bid
     * @return the corresponding BidUpdateDTO
     * @throws BidNotFoundException if bid does not exist
     */
    public BidUpdateDTO getBidUpdateDTO(int id) {
        Bid bid = bidRepository.findById(id)
                .orElseThrow(() -> new BidNotFoundException("Bid not found with ID: " + id));
        return new BidUpdateDTO(bid.getId(), bid.getAccount(), bid.getType(), bid.getBidQuantity());
    }
}
