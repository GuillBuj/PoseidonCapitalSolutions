package poseidoncapitalsolutions.trading.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.poseidoncapitalsolutions.trading.dto.BidAddDTO;
import com.poseidoncapitalsolutions.trading.dto.BidUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.BidListItemDTO;
import com.poseidoncapitalsolutions.trading.exception.BidNotFoundException;
import com.poseidoncapitalsolutions.trading.mapper.BidMapper;
import com.poseidoncapitalsolutions.trading.model.Bid;
import com.poseidoncapitalsolutions.trading.repository.BidRepository;
import com.poseidoncapitalsolutions.trading.service.BidService;

@ExtendWith(MockitoExtension.class)
class BidServiceTest {

    @Mock
    private BidRepository bidRepository;

    @Mock
    private BidMapper bidMapper;

    @InjectMocks
    private BidService bidService;

    private Bid bid;
    private BidAddDTO bidAddDTO;
    private BidUpdateDTO bidUpdateDTO;
    private BidListItemDTO bidListItemDTO;

    @BeforeEach
    void setUp() {
        bid = new Bid();
        bid.setId(1);
        bid.setAccount("Account1");
        bid.setType("Type1");
        bid.setBidQuantity(10.00);
    }

    @Test
    void getAllBidsOk() {
        String account = "Account1";
        String type = "Type1";
        Double bidQuantity = 10.00;
        bidListItemDTO = new BidListItemDTO(1, account, type, bidQuantity);

        when(bidRepository.findAll()).thenReturn(Arrays.asList(bid));
        when(bidMapper.toListItemDTOList(anyList())).thenReturn(Arrays.asList(bidListItemDTO));
        
        List<BidListItemDTO> result = bidService.getAllBids();
        
        assertEquals(1, result.size());
        assertEquals(new BidListItemDTO(1, account, type, bidQuantity), result.get(0));
        verify(bidRepository).findAll();
    }

    @Test
    void createBidOk() {
        String expectedAccount = "Account1";
        String expectedType = "Type1";
        Double expectedBidQuantity = 10.00;
        bidAddDTO = new BidAddDTO(expectedAccount, expectedType, expectedBidQuantity);
        
        when(bidMapper.toEntity(any())).thenReturn(bid);
        when(bidRepository.save(any())).thenReturn(bid);
        
        Bid result = bidService.createBid(bidAddDTO);
        
        assertNotNull(result);
        assertEquals(expectedAccount, result.getAccount());
        assertEquals(expectedType, result.getType());
        assertEquals(expectedBidQuantity, result.getBidQuantity());
        verify(bidRepository).save(bid);
    }

    @Test
    void updateBidOk() {
        bidUpdateDTO = new BidUpdateDTO(1, "updatedAccount", "updatedType", 15.00);

        when(bidRepository.findById(1)).thenReturn(Optional.of(bid));
        when(bidRepository.save(bid)).thenReturn(bid);

        Bid result = bidService.updateBid(bidUpdateDTO);

        assertEquals(1, result.getId());
        verify(bidMapper).updateBidFromDTO(bidUpdateDTO, bid);
        verify(bidRepository).save(bid);
    }

    @Test
    void updateBidNotFound() {
        int nonExistentId = 999;
        BidUpdateDTO updateDTO = new BidUpdateDTO(nonExistentId, "newAccount", "newType", 200.0);
        
        when(bidRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(BidNotFoundException.class, () -> bidService.updateBid(updateDTO));
        verify(bidRepository, never()).save(any());
    }


    @Test
    void deleteByIdOk() {
        when(bidRepository.findById(anyInt())).thenReturn(Optional.of(bid));
        doNothing().when(bidRepository).delete(any());
        
        bidService.deleteById(1);
        
        verify(bidRepository).delete(any());
    }

    @Test
    void deleteByIdNotFound() {
        int nonExistentId = 999;
        when(bidRepository.findById(anyInt())).thenReturn(Optional.empty());
       
        assertThrows(BidNotFoundException.class, () -> bidService.deleteById(nonExistentId));

        verify(bidRepository,never()).delete(any());
    }

    @Test
    void getBidUpdateDTOOk() { 
        when(bidRepository.findById(anyInt())).thenReturn(Optional.of(bid));
        
        BidUpdateDTO result = bidService.getBidUpdateDTO(1);
        
        assertNotNull(result);
        assertEquals(new BidUpdateDTO(1, "Account1", "Type1", 10.00), result);
        verify(bidRepository).findById(anyInt());
    }

    @Test
    void getBidUpdateDTONotFound() {
        int nonExistentId = 999;
        when(bidRepository.findById(anyInt())).thenReturn(Optional.empty());
       
        assertThrows(BidNotFoundException.class, () -> bidService.getBidUpdateDTO(nonExistentId));
    }
}

