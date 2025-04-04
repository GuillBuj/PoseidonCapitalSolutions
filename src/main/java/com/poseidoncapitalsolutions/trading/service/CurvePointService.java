package com.poseidoncapitalsolutions.trading.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poseidoncapitalsolutions.trading.dto.CurvePointAddDTO;
import com.poseidoncapitalsolutions.trading.dto.CurvePointUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.CurvePointListItemDTO;
import com.poseidoncapitalsolutions.trading.exception.CurvePointNotFoundException;
import com.poseidoncapitalsolutions.trading.mapper.CurvePointMapper;
import com.poseidoncapitalsolutions.trading.model.CurvePoint;
import com.poseidoncapitalsolutions.trading.repository.CurvePointRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CurvePointService {
    
    private final CurvePointRepository curvePointRepository;
    private final CurvePointMapper curvePointMapper;
    
    public List<CurvePointListItemDTO> getAll() {
        return curvePointMapper.toListItemDtoList(curvePointRepository.findAll());
    }
        
    @Transactional
    public CurvePoint createCurvePoint(CurvePointAddDTO curvePointAddDTO){
        log.debug("Creating curve point from DTO: {}", curvePointAddDTO);

        CurvePoint newCurvePoint = curvePointMapper.toEntity(curvePointAddDTO);
        newCurvePoint.setCreationDate(new Timestamp(System.currentTimeMillis()));

        return curvePointRepository.save(newCurvePoint);
    }
    
    @Transactional
    public CurvePoint updateCurvePoint(CurvePointUpdateDTO curvePointUpdateDTO){
        log.debug("Updating curve point from DTO: {}", curvePointUpdateDTO);
        
        CurvePoint curvePoint = curvePointRepository.findById(curvePointUpdateDTO.id())
            .orElseThrow(() -> new CurvePointNotFoundException("Curve point not found with ID: " + curvePointUpdateDTO.id()));
        
        curvePointMapper.updateEntityFromDto(curvePointUpdateDTO, curvePoint);

        return curvePointRepository.save(curvePoint);
    }

    @Transactional
    public void deleteById(int id) {
        log.debug("Deleting curve point with id: {}", id);
    
        CurvePoint curvePoint = curvePointRepository.findById(id)
            .orElseThrow(() -> new CurvePointNotFoundException("Curve point not found with ID: " + id));
        
        curvePointRepository.delete(curvePoint);
        log.info("Curve point successfully deleted with id: {}", id);
    }
    
    @Transactional(readOnly = true)
    public CurvePointUpdateDTO getCurvePointUpdateDTO(int id){
        
        CurvePoint curvePoint = curvePointRepository.findById(id)
            .orElseThrow(() -> new CurvePointNotFoundException("Curve point not found with ID: " + id));

        return curvePointMapper.toDTO(curvePoint);
    }
}