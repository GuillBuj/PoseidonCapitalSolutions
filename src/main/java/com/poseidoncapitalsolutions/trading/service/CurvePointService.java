package com.poseidoncapitalsolutions.trading.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import com.poseidoncapitalsolutions.trading.dto.CurvePointAddDTO;
import com.poseidoncapitalsolutions.trading.dto.CurvePointUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.CurvePointListItemDTO;
import com.poseidoncapitalsolutions.trading.exception.BidNotFoundException;
import com.poseidoncapitalsolutions.trading.exception.CurvePointNotFoundException;
import com.poseidoncapitalsolutions.trading.mapper.CurvePointMapper;
import com.poseidoncapitalsolutions.trading.model.Bid;
import com.poseidoncapitalsolutions.trading.model.CurvePoint;
import com.poseidoncapitalsolutions.trading.repository.CurvePointRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Service
@AllArgsConstructor
@Data
@Slf4j
public class CurvePointService {
    private final CurvePointRepository curvePointRepository;
    private final CurvePointMapper mapper;
    
    public List<CurvePointListItemDTO> getAll() {
        return mapper.toListItemDtoList(curvePointRepository.findAll());
    }

    public CurvePoint createCurvePoint(CurvePointAddDTO curvePointAddDTO){
        log.debug("Creating curve point from DTO: {}", curvePointAddDTO);

        CurvePoint newCurvePoint = mapper.toEntity(curvePointAddDTO);
        log.warn("new curvePoint: {}",newCurvePoint);
        newCurvePoint.setCreationDate(new Timestamp(System.currentTimeMillis()));

        return curvePointRepository.save(newCurvePoint);
    }

    public CurvePoint updateCurvePoint(CurvePointUpdateDTO curvePointUpdateDTO){
        log.debug("Updating curve point from DTO: {}", curvePointUpdateDTO);
        
        int id = curvePointUpdateDTO.id();
        CurvePoint curvePoint = curvePointRepository.findById(id)
            .orElseThrow(() -> new CurvePointNotFoundException("Curve point not found with ID: " + id));
        
        mapper.updateEntityFromDto(curvePointUpdateDTO, curvePoint);

        return curvePointRepository.save(curvePoint);
    }

    public void deleteById(int id) {
        log.debug("Deleting curve point with id: {}", id);
    
        CurvePoint curvePointToDelete = curvePointRepository.findById(id)
            .orElseThrow(() -> new CurvePointNotFoundException("Curve point not found with ID: " + id));
        
        curvePointRepository.delete(curvePointToDelete);
        log.info("Curve point successfully deleted with id: {}", id);
    }
    
    public CurvePointUpdateDTO getCurvePointUpdateDTO(int id){
        
        CurvePoint curvePoint = curvePointRepository.findById(id)
            .orElseThrow(() -> new CurvePointNotFoundException("Curve point not found with ID: " + id));

        return mapper.toDTO(curvePoint);
    }
}
