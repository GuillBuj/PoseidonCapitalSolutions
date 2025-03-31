package poseidoncapitalsolutions.trading.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.poseidoncapitalsolutions.trading.dto.TradeAddDTO;
import com.poseidoncapitalsolutions.trading.dto.TradeUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.TradeListItemDTO;
import com.poseidoncapitalsolutions.trading.exception.TradeNotFoundException;
import com.poseidoncapitalsolutions.trading.mapper.TradeMapper;
import com.poseidoncapitalsolutions.trading.model.Trade;
import com.poseidoncapitalsolutions.trading.repository.TradeRepository;
import com.poseidoncapitalsolutions.trading.service.TradeService;

@ExtendWith(MockitoExtension.class)
class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private TradeMapper tradeMapper;

    @InjectMocks
    private TradeService tradeService;

    private Trade trade;

    @BeforeEach
    void setUp() {
        trade = new Trade();
        trade.setId(1);
        trade.setAccount("Account1");
        trade.setType("Type1");
        trade.setBuyQuantity(100.0);
    }

    @Test
    void getAllTradesOk() {
        TradeListItemDTO expectedListItemDTO = new TradeListItemDTO(1, "Account1", "Type1", 100.00);

        when(tradeRepository.findAll()).thenReturn(Arrays.asList(trade));
        when(tradeMapper.toListItemDTOList(anyList())).thenReturn(Arrays.asList(expectedListItemDTO));
        
        List<TradeListItemDTO> result = tradeService.getAllTrades();
        
        assertEquals(1, result.size());
        assertEquals(expectedListItemDTO, result.get(0));
        verify(tradeRepository).findAll();
    }

    @Test
    void createTradeOk() {
        String expectedAccount = "Account1";
        String expectedType = "Type1";
        Double expectedBuyQuantity = 100.00;
        TradeAddDTO addDTO = new TradeAddDTO(expectedAccount, expectedType, expectedBuyQuantity);
        
        when(tradeMapper.toEntity(any())).thenReturn(trade);
        when(tradeRepository.save(any())).thenReturn(trade);
        
        Trade result = tradeService.createTrade(addDTO);
        
        assertNotNull(result);
        assertEquals(expectedAccount, result.getAccount());
        assertEquals(expectedType, result.getType());
        assertEquals(expectedBuyQuantity, result.getBuyQuantity());
        verify(tradeRepository).save(trade);
    }

    @Test
    void updateTradeOk() {
        TradeUpdateDTO updateDTO = new TradeUpdateDTO(1, "updatedAccount", "updatedType", 200.0);

        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));
        when(tradeRepository.save(trade)).thenReturn(trade);

        Trade result = tradeService.updateTrade(updateDTO);

        assertEquals(1, result.getId());
        verify(tradeMapper).updateTradeFromDTO(updateDTO, trade);
        verify(tradeRepository).save(trade);
    }

    @Test
    void updateTradeNotFound() {
        int nonExistentId = 999;
        TradeUpdateDTO updateDTO = new TradeUpdateDTO(nonExistentId, "updatedAccount", "updatedType", 50.0);
        
        when(tradeRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(TradeNotFoundException.class, () -> tradeService.updateTrade(updateDTO));
        verify(tradeRepository, never()).save(any());
    }

    @Test
    void deleteByIdOk() {
        when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(trade));
        doNothing().when(tradeRepository).delete(any());
        
        tradeService.deleteById(1);
        
        verify(tradeRepository).delete(any());
    }

    @Test
    void deleteByIdNotFound() {
        int nonExistentId = 999;
        when(tradeRepository.findById(anyInt())).thenReturn(Optional.empty());
       
        assertThrows(TradeNotFoundException.class, () -> tradeService.deleteById(nonExistentId));

        verify(tradeRepository, never()).delete(any());
    }

    @Test
    void getTradeUpdateDTOOk() { 
        TradeUpdateDTO expectedUpdateDTO = new TradeUpdateDTO(1, "Account1", "Type1", 100.0);
        
        when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(trade));
        when(tradeMapper.toDTO(trade)).thenReturn(expectedUpdateDTO);
        
        TradeUpdateDTO result = tradeService.getTradeUpdateDTO(1);
        
        assertNotNull(result);
        assertEquals(expectedUpdateDTO, result);
        verify(tradeRepository).findById(anyInt());
    }

    @Test
    void getTradeUpdateDTONotFound() {
        int nonExistentId = 999;
        when(tradeRepository.findById(anyInt())).thenReturn(Optional.empty());
       
        assertThrows(TradeNotFoundException.class, () -> tradeService.getTradeUpdateDTO(nonExistentId));
    }
}
