package com.poseidoncapitalsolutions.trading.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poseidoncapitalsolutions.trading.dto.RatingAddDTO;
import com.poseidoncapitalsolutions.trading.dto.RatingUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.RatingListItemDTO;
import com.poseidoncapitalsolutions.trading.exception.RatingNotFoundException;
import com.poseidoncapitalsolutions.trading.mapper.RatingMapper;
import com.poseidoncapitalsolutions.trading.model.Rating;
import com.poseidoncapitalsolutions.trading.repository.RatingRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class RatingService {
    
    private final RatingRepository ratingRepository;
    private final RatingMapper mapper;

    public List<RatingListItemDTO> getAll(){
        return mapper.toListItemDTOList(ratingRepository.findAll());
    }
        
    @Transactional
    public Rating createRating(RatingAddDTO ratingAddDTO){
        log.debug("Creating rating from DTO: {}", ratingAddDTO);

        return ratingRepository.save(mapper.toEntity(ratingAddDTO));
    }

    
    @Transactional
    public Rating updateRating(RatingUpdateDTO ratingUpdateDTO){
        log.debug("Updating rating from DTO: {}", ratingUpdateDTO);

        Rating rating = ratingRepository.findById(ratingUpdateDTO.id())
            .orElseThrow(()-> new RatingNotFoundException("Rating point not found with ID: " + ratingUpdateDTO.id()));

        mapper.updateEntityFromDTO(ratingUpdateDTO, rating);

        return ratingRepository.save(rating);
    }
    
    @Transactional
    public void deleteById(int id){
        log.debug("Deleting rating with id: {}", id);

        Rating rating = ratingRepository.findById(id)
            .orElseThrow(()-> new RatingNotFoundException("Rating not found with ID: " + id));

        ratingRepository.delete(rating);
        log.info("Rating successfully deleted with id: {}", id);
    }

    @Transactional(readOnly = true)
    public RatingUpdateDTO getRatingUpdateDTO(int id){

        Rating rating = ratingRepository.findById(id)
            .orElseThrow(()-> new RatingNotFoundException("Rating not found with ID: " + id));

        return mapper.toDTO(rating);
    }
}