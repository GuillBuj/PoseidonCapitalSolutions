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

import com.poseidoncapitalsolutions.trading.dto.RatingAddDTO;
import com.poseidoncapitalsolutions.trading.dto.RatingUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.RatingListItemDTO;
import com.poseidoncapitalsolutions.trading.exception.RatingNotFoundException;
import com.poseidoncapitalsolutions.trading.mapper.RatingMapper;
import com.poseidoncapitalsolutions.trading.model.Rating;
import com.poseidoncapitalsolutions.trading.repository.RatingRepository;
import com.poseidoncapitalsolutions.trading.service.RatingService;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RatingMapper ratingMapper;

    @InjectMocks
    private RatingService ratingService;

    private Rating rating;

    @BeforeEach
    void setUp() {
        rating = new Rating();
        rating.setId(1);
        rating.setMoodysRating("A");
        rating.setSandPRating("AA");
        rating.setFitchRating("AAA");
        rating.setOrderNumber(10);
    }

    @Test
    void getAllRatingsOk() {
        RatingListItemDTO expectedListItemDTO = new RatingListItemDTO(1, "A", "AA", "AAA", 10);

        when(ratingRepository.findAll()).thenReturn(Arrays.asList(rating));
        when(ratingMapper.toListItemDTOList(anyList())).thenReturn(Arrays.asList(expectedListItemDTO));
        
        List<RatingListItemDTO> result = ratingService.getAll();
        
        assertEquals(1, result.size());
        assertEquals(expectedListItemDTO, result.get(0));
        verify(ratingRepository).findAll();
    }

    @Test
    void createRatingOk() {
        String expectedMoodys = "A";
        String expectedSandP = "AA";
        String expectedFitch = "AAA";
        Integer expectedOrder = 10;
        RatingAddDTO addDTO = new RatingAddDTO(expectedMoodys, expectedSandP, expectedFitch, expectedOrder);
        
        when(ratingMapper.toEntity(any())).thenReturn(rating);
        when(ratingRepository.save(any())).thenReturn(rating);
        
        Rating result = ratingService.createRating(addDTO);
        
        assertNotNull(result);
        assertEquals(expectedMoodys, result.getMoodysRating());
        assertEquals(expectedSandP, result.getSandPRating());
        assertEquals(expectedFitch, result.getFitchRating());
        assertEquals(expectedOrder, result.getOrderNumber());
        verify(ratingRepository).save(rating);
    }

    @Test
    void updateRatingOk() {
        RatingUpdateDTO updateDTO = new RatingUpdateDTO(1, "B", "BBB", "BB", 20);

        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));
        when(ratingRepository.save(rating)).thenReturn(rating);

        Rating result = ratingService.updateRating(updateDTO);

        assertEquals(1, result.getId());
        verify(ratingMapper).updateEntityFromDTO(updateDTO, rating);
        verify(ratingRepository).save(rating);
    }

    @Test
    void updateRatingNotFound() {
        int nonExistentId = 999;
        RatingUpdateDTO updateDTO = new RatingUpdateDTO(nonExistentId, "B", "BBB", "BB", 20);
        
        when(ratingRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(RatingNotFoundException.class, () -> ratingService.updateRating(updateDTO));
        verify(ratingRepository, never()).save(any());
    }

    @Test
    void deleteByIdOk() {
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(rating));
        doNothing().when(ratingRepository).delete(any());
        
        ratingService.deleteById(1);
        
        verify(ratingRepository).delete(any());
    }

    @Test
    void deleteByIdNotFound() {
        int nonExistentId = 999;
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.empty());
       
        assertThrows(RatingNotFoundException.class, () -> ratingService.deleteById(nonExistentId));

        verify(ratingRepository, never()).delete(any());
    }

    @Test
    void getRatingUpdateDTOOk() { 
        RatingUpdateDTO expectedUpdateDTO = new RatingUpdateDTO(1, "A", "AA", "AAA", 10);
        
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(rating));
        when(ratingMapper.toDTO(rating)).thenReturn(expectedUpdateDTO);
        
        RatingUpdateDTO result = ratingService.getRatingUpdateDTO(1);
        
        assertNotNull(result);
        assertEquals(expectedUpdateDTO, result);
        verify(ratingRepository).findById(anyInt());
    }

    @Test
    void getRatingUpdateDTONotFound() {
        int nonExistentId = 999;
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.empty());
       
        assertThrows(RatingNotFoundException.class, () -> ratingService.getRatingUpdateDTO(nonExistentId));
    }
}
