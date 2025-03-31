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

import com.poseidoncapitalsolutions.trading.dto.CurvePointAddDTO;
import com.poseidoncapitalsolutions.trading.dto.CurvePointUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.CurvePointListItemDTO;
import com.poseidoncapitalsolutions.trading.exception.CurvePointNotFoundException;
import com.poseidoncapitalsolutions.trading.mapper.CurvePointMapper;
import com.poseidoncapitalsolutions.trading.model.CurvePoint;
import com.poseidoncapitalsolutions.trading.repository.CurvePointRepository;
import com.poseidoncapitalsolutions.trading.service.CurvePointService;

@ExtendWith(MockitoExtension.class)
class CurvePointServiceTest {

    @Mock
    private CurvePointRepository curvePointRepository;

    @Mock
    private CurvePointMapper curvePointMapper;

    @InjectMocks
    private CurvePointService curvePointService;

    private CurvePoint curvePoint;

    @BeforeEach
    void setUp() {
        curvePoint = new CurvePoint();
        curvePoint.setId(1);
        curvePoint.setCurveId(10);
        curvePoint.setTerm(2.5);
        curvePoint.setValue(3.8);
    }

    @Test
    void getAllCurvePointsOk() {
        CurvePointListItemDTO expectedListItemDTO = new CurvePointListItemDTO(1, 10, 2.5, 3.8);

        when(curvePointRepository.findAll()).thenReturn(Arrays.asList(curvePoint));
        when(curvePointMapper.toListItemDtoList(anyList())).thenReturn(Arrays.asList(expectedListItemDTO));
        
        List<CurvePointListItemDTO> result = curvePointService.getAll();
        
        assertEquals(1, result.size());
        assertEquals(expectedListItemDTO, result.get(0));
        verify(curvePointRepository).findAll();
    }

    @Test
    void createCurvePointOk() {
        Integer expectedCurveId = 10;
        Double expectedTerm = 2.5;
        Double expectedValue = 3.8;
        CurvePointAddDTO addDTO = new CurvePointAddDTO(expectedCurveId, expectedTerm, expectedValue);
        
        when(curvePointMapper.toEntity(any())).thenReturn(curvePoint);
        when(curvePointRepository.save(any())).thenReturn(curvePoint);
        
        CurvePoint result = curvePointService.createCurvePoint(addDTO);
        
        assertNotNull(result);
        assertEquals(expectedCurveId, result.getCurveId());
        assertEquals(expectedTerm, result.getTerm());
        assertEquals(expectedValue, result.getValue());
        assertNotNull(result.getCreationDate());
        verify(curvePointRepository).save(curvePoint);
    }

    @Test
    void updateCurvePointOk() {
        CurvePointUpdateDTO updateDTO = new CurvePointUpdateDTO(1, 20, 3.0, 4.2);

        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint));
        when(curvePointRepository.save(curvePoint)).thenReturn(curvePoint);

        CurvePoint result = curvePointService.updateCurvePoint(updateDTO);

        assertEquals(1, result.getId());
        verify(curvePointMapper).updateEntityFromDto(updateDTO, curvePoint);
        verify(curvePointRepository).save(curvePoint);
    }

    @Test
    void updateCurvePointNotFound() {
        int nonExistentId = 999;
        CurvePointUpdateDTO updateDTO = new CurvePointUpdateDTO(nonExistentId, 20, 3.0, 4.2);
        
        when(curvePointRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(CurvePointNotFoundException.class, () -> curvePointService.updateCurvePoint(updateDTO));
        verify(curvePointRepository, never()).save(any());
    }

    @Test
    void deleteByIdOk() {
        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(curvePoint));
        doNothing().when(curvePointRepository).delete(any());
        
        curvePointService.deleteById(1);
        
        verify(curvePointRepository).delete(any());
    }

    @Test
    void deleteByIdNotFound() {
        int nonExistentId = 999;
        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.empty());
       
        assertThrows(CurvePointNotFoundException.class, () -> curvePointService.deleteById(nonExistentId));

        verify(curvePointRepository, never()).delete(any());
    }

    @Test
    void getCurvePointUpdateDTOOk() { 
        CurvePointUpdateDTO expectedUpdateDTO = new CurvePointUpdateDTO(1, 10, 2.5, 3.8);
        
        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(curvePoint));
        when(curvePointMapper.toDTO(curvePoint)).thenReturn(expectedUpdateDTO);
        
        CurvePointUpdateDTO result = curvePointService.getCurvePointUpdateDTO(1);
        
        assertNotNull(result);
        assertEquals(expectedUpdateDTO, result);
        verify(curvePointRepository).findById(anyInt());
    }

    @Test
    void getCurvePointUpdateDTONotFound() {
        int nonExistentId = 999;
        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.empty());
       
        assertThrows(CurvePointNotFoundException.class, () -> curvePointService.getCurvePointUpdateDTO(nonExistentId));
    }
}
