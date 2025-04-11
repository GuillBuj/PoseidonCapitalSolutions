package poseidoncapitalsolutions.trading.controller;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.poseidoncapitalsolutions.trading.TradingApplication;
import com.poseidoncapitalsolutions.trading.model.CurvePoint;
import com.poseidoncapitalsolutions.trading.repository.CurvePointRepository;

import jakarta.transaction.Transactional;

@SpringBootTest(classes = TradingApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "classpath:insert_curve_point_test_values.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class CurvePointControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurvePointRepository curvePointRepository;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testGetAllCurvePoints() throws Exception {
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attribute("curvePoints", hasSize(2)));

        assertThat(curvePointRepository.findAll())
                .extracting(CurvePoint::getTerm)
                .containsExactly(1.0, 2.0);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testShowAddForm() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeExists("curvePointDTO"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testValidateCurvePointOk() throws Exception {
        int initialCount = curvePointRepository.findAll().size();

        mockMvc.perform(post("/curvePoint/validate")
                        .param("curveId", "3")
                        .param("term", "3.0")
                        .param("value", "30.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        List<CurvePoint> curvePoints = curvePointRepository.findAll();
        assertThat(curvePoints).hasSize(initialCount + 1);

        CurvePoint createdCurvePoint = curvePoints.getLast();
        assertThat(createdCurvePoint.getCurveId()).isEqualTo(3);
        assertThat(createdCurvePoint.getTerm()).isEqualTo(3.0);
        assertThat(createdCurvePoint.getValue()).isEqualTo(30.0);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testValidateCurvePointInvalidData() throws Exception {
        int initialCount = curvePointRepository.findAll().size();

        mockMvc.perform(post("/curvePoint/validate")
                        .param("curveId", "0")  // Invalid: @Positive
                        .param("term", "3.0")
                        .param("value", "30.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeHasFieldErrors("curvePointDTO", "curveId"));

        assertThat(curvePointRepository.findAll()).hasSize(initialCount);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testShowUpdateFormOk() throws Exception {
        CurvePoint existingCurvePoint = curvePointRepository.findAll().getFirst();

        mockMvc.perform(get("/curvePoint/update/" + existingCurvePoint.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeExists("curvePointDTO"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testShowUpdateFormNonExistingId() throws Exception {
        mockMvc.perform(get("/curvePoint/update/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testUpdateCurvePointOk() throws Exception {
        CurvePoint existingCurvePoint = curvePointRepository.findAll().getFirst();

        mockMvc.perform(post("/curvePoint/update/" + existingCurvePoint.getId())
                        .param("id", Integer.toString(existingCurvePoint.getId()))
                        .param("curveId", "10")
                        .param("term", "10.0")
                        .param("value", "100.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        CurvePoint updatedCurvePoint = curvePointRepository.findById(existingCurvePoint.getId()).orElseThrow();
        assertThat(updatedCurvePoint.getCurveId()).isEqualTo(10);
        assertThat(updatedCurvePoint.getTerm()).isEqualTo(10.0);
        assertThat(updatedCurvePoint.getValue()).isEqualTo(100.0);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testUpdateCurvePointInvalidData() throws Exception {
        CurvePoint existingCurvePoint = curvePointRepository.findAll().getFirst();
        int originalCurveId = existingCurvePoint.getCurveId();

        mockMvc.perform(post("/curvePoint/update/" + existingCurvePoint.getId())
                        .param("id", Integer.toString(existingCurvePoint.getId()))
                        .param("curveId", "0")  // Invalid: @Positive
                        .param("term", "10.0")
                        .param("value", "100.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeHasFieldErrors("curvePointDTO", "curveId"));

        CurvePoint unchangedCurvePoint = curvePointRepository.findById(existingCurvePoint.getId()).orElseThrow();
        assertThat(unchangedCurvePoint.getCurveId()).isEqualTo(originalCurveId);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testDeleteCurvePointOk() throws Exception {
        int initialCount = curvePointRepository.findAll().size();
        CurvePoint curvePointToDelete = curvePointRepository.findAll().getFirst();

        mockMvc.perform(get("/curvePoint/delete/" + curvePointToDelete.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        assertThat(curvePointRepository.findAll()).hasSize(initialCount - 1);
        assertThat(curvePointRepository.findById(curvePointToDelete.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testDeleteCurvePointNonExistingId() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }
}