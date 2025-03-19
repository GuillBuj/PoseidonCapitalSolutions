package com.poseidoncapitalsolutions.trading.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidoncapitalsolutions.trading.dto.BidAddDTO;
import com.poseidoncapitalsolutions.trading.dto.BidUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.BidListItemDTO;
import com.poseidoncapitalsolutions.trading.exception.ItemNotFoundException;
import com.poseidoncapitalsolutions.trading.model.Bid;
import com.poseidoncapitalsolutions.trading.repository.BidRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BidService {

    @Autowired
    BidRepository bidRepository;

    public List<BidListItemDTO> getAllBids(){
        return bidRepository.findAll().stream()
                    .map(bid -> new BidListItemDTO(bid.getId(), bid.getAccount(), bid.getType(), bid.getBidQuantity()))
                    .toList();
    }
    
    public Bid createBid(BidAddDTO bidAddDTO){
        log.debug("Creating bid from DTO: {}", bidAddDTO);

        Bid newBid = new Bid();

        newBid.setAccount(bidAddDTO.account());
        newBid.setType(bidAddDTO.type());
        newBid.setBidQuantity(bidAddDTO.quantity());
        newBid.setCreationDate(new Timestamp(System.currentTimeMillis()));

        return bidRepository.save(newBid);
    }

    public Bid updateBid(BidUpdateDTO bidUpdateDTO){
        log.debug("Updating bid from DTO: {}", bidUpdateDTO);

        Bid updatedBid = bidRepository.findById(bidUpdateDTO.id())
            .orElseThrow(() -> new ItemNotFoundException("Bid not found with ID: " + bidUpdateDTO.id()));
        
        updatedBid.setAccount(bidUpdateDTO.account());
        updatedBid.setType(bidUpdateDTO.type());
        updatedBid.setBidQuantity(bidUpdateDTO.quantity());
        updatedBid.setRevisionDate(new Timestamp(System.currentTimeMillis()));
        
        return bidRepository.save(updatedBid);
    }

    public boolean deleteById(int id){
        log.debug("Deleting bid with id: {}", id);

        if(bidRepository.existsById(id)){
            bidRepository.deleteById(id);
            log.info("Bid deleted with id: {}", id);
            return true;
        }
        
        log.info("Bid NOT deleted with id: {}", id);
        return false;    
    }

    public BidUpdateDTO getBidUpdateDTO(int id){
        Bid bid = bidRepository.findById(id)
            .orElseThrow(() -> new ItemNotFoundException("Bid not found with ID: " + id));

        return new BidUpdateDTO(bid.getId(), bid.getAccount(), bid.getType(), bid.getBidQuantity());
    }
}
