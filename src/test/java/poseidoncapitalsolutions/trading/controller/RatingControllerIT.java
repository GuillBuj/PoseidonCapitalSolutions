package poseidoncapitalsolutions.trading.controller;

import com.poseidoncapitalsolutions.trading.TradingApplication;

import com.poseidoncapitalsolutions.trading.model.Rating;
import com.poseidoncapitalsolutions.trading.repository.RatingRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(classes = TradingApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "classpath:insert_rating_test_values.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class RatingControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RatingRepository ratingRepository;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetAllRatings() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attribute("ratings", hasSize(2)));

        assertThat(ratingRepository.findAll())
                .extracting(Rating::getOrderNumber)
                .containsExactly(1, 2);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testShowAddForm() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().attributeExists("ratingDTO"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testValidateRatingOk() throws Exception {
        int initialCount = ratingRepository.findAll().size();

        mockMvc.perform(post("/rating/validate")
                        .param("moodysRating", "A2")
                        .param("sandPRating", "A+")
                        .param("fitchRating", "A")
                        .param("orderNumber", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        List<Rating> ratings = ratingRepository.findAll();
        assertThat(ratings).hasSize(initialCount + 1);

        Rating createdRating = ratings.getLast();
        assertThat(createdRating.getMoodysRating()).isEqualTo("A2");
        assertThat(createdRating.getSandPRating()).isEqualTo("A+");
        assertThat(createdRating.getFitchRating()).isEqualTo("A");
        assertThat(createdRating.getOrderNumber()).isEqualTo(3);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testShowUpdateFormOk() throws Exception {
        Rating existingRating = ratingRepository.findAll().getFirst();

        mockMvc.perform(get("/rating/update/" + existingRating.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attributeExists("ratingDTO"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testShowUpdateFormNonExistingId() throws Exception {
        mockMvc.perform(get("/rating/update/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testUpdateRatingOk() throws Exception {
        Rating existingRating = ratingRepository.findAll().getFirst();

        mockMvc.perform(post("/rating/update/" + existingRating.getId())
                        .param("id", Long.toString(existingRating.getId()))
                        .param("moodysRating", "A3")
                        .param("sandPRating", "BBB+")
                        .param("fitchRating", "BBB")
                        .param("orderNumber", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        Rating updatedRating = ratingRepository.findById(existingRating.getId()).orElseThrow();
        assertThat(updatedRating.getMoodysRating()).isEqualTo("A3");
        assertThat(updatedRating.getSandPRating()).isEqualTo("BBB+");
        assertThat(updatedRating.getFitchRating()).isEqualTo("BBB");
        assertThat(updatedRating.getOrderNumber()).isEqualTo(10);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteRatingOk() throws Exception {
        int initialCount = ratingRepository.findAll().size();
        Rating ratingToDelete = ratingRepository.findAll().getFirst();

        mockMvc.perform(get("/rating/delete/" + ratingToDelete.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        assertThat(ratingRepository.findAll()).hasSize(initialCount - 1);
        assertThat(ratingRepository.findById(ratingToDelete.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteRatingNonExistingId() throws Exception {
        mockMvc.perform(get("/rating/delete/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }
}