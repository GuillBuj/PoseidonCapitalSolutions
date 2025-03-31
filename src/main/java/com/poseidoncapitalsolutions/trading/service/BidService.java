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

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class BidService {

    private final BidRepository bidRepository;
    private final BidMapper bidMapper;

    public List<BidListItemDTO> getAllBids(){
        return bidMapper.toListItemDTOList(bidRepository.findAll());
    }
    
    public Bid createBid(BidAddDTO bidAddDTO){
        log.debug("Creating bid from DTO: {}", bidAddDTO);

        Bid newBid = bidMapper.toEntity(bidAddDTO);
        newBid.setCreationDate(new Timestamp(System.currentTimeMillis()));

        return bidRepository.save(newBid);
    }

    public Bid updateBid(BidUpdateDTO bidUpdateDTO){
        log.debug("Updating bid from DTO: {}", bidUpdateDTO);

        Bid updatedBid = bidRepository.findById(bidUpdateDTO.id())
            .orElseThrow(() -> new BidNotFoundException("Bid not found with ID: " + bidUpdateDTO.id()));
        
        bidMapper.updateBidFromDTO(bidUpdateDTO, updatedBid);
        
        return bidRepository.save(updatedBid);
    }

    public void deleteById(int id) {
        log.debug("Deleting bid with id: {}", id);
    
        Bid bid = bidRepository.findById(id)
            .orElseThrow(() -> new BidNotFoundException("Bid not found with ID: " + id));
       
        bidRepository.delete(bid);
        log.info("Bid successfully deleted with id: {}", id);
    }

    public BidUpdateDTO getBidUpdateDTO(int id){
        Bid bid = bidRepository.findById(id)
            .orElseThrow(() -> new BidNotFoundException("Bid not found with ID: " + id));

        return new BidUpdateDTO(bid.getId(), bid.getAccount(), bid.getType(), bid.getBidQuantity());
    }
}
