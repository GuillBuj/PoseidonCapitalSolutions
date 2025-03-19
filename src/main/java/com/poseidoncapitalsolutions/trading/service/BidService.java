package com.poseidoncapitalsolutions.trading.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poseidoncapitalsolutions.trading.dto.BidAddDTO;
import com.poseidoncapitalsolutions.trading.dto.display.BidListItemDTO;
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

        return bidRepository.save(newBid);
    }
}
