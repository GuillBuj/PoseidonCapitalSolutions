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


/**
 * Service for managing ratings.
 */
@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    /**
     * Retrieves all ratings.
     *
     * @return list of RatingListItemDTO
     */
    public List<RatingListItemDTO> getAll() {
        return ratingMapper.toListItemDTOList(ratingRepository.findAll());
    }

    /**
     * Creates a new rating.
     *
     * @param ratingAddDTO the rating data
     * @return created Rating entity
     */
    public Rating createRating(RatingAddDTO ratingAddDTO) {
        log.debug("Creating rating from DTO: {}", ratingAddDTO);
        return ratingRepository.save(ratingMapper.toEntity(ratingAddDTO));
    }

    /**
     * Updates a rating.
     *
     * @param ratingUpdateDTO updated rating data
     * @return updated Rating entity
     * @throws RatingNotFoundException if not found
     */
    public Rating updateRating(RatingUpdateDTO ratingUpdateDTO) {
        log.debug("Updating rating from DTO: {}", ratingUpdateDTO);
        Rating updatedRating = ratingRepository.findById(ratingUpdateDTO.id())
                .orElseThrow(() -> new RatingNotFoundException("Rating point not found with ID: " + ratingUpdateDTO.id()));
        ratingMapper.updateEntityFromDTO(ratingUpdateDTO, updatedRating);
        return ratingRepository.save(updatedRating);
    }

    /**
     * Deletes a rating.
     *
     * @param id rating ID
     * @throws RatingNotFoundException if not found
     */
    public void deleteById(int id) {
        log.debug("Deleting rating with id: {}", id);
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RatingNotFoundException("Rating not found with ID: " + id));
        ratingRepository.delete(rating);
        log.info("Rating successfully deleted with id: {}", id);
    }

    /**
     * Retrieves update DTO for a rating.
     *
     * @param id rating ID
     * @return RatingUpdateDTO
     * @throws RatingNotFoundException if not found
     */
    public RatingUpdateDTO getRatingUpdateDTO(int id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new RatingNotFoundException("Rating not found with ID: " + id));
        return ratingMapper.toDTO(rating);
    }
}