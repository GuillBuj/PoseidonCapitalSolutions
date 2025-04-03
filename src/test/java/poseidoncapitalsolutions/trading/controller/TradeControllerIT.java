package poseidoncapitalsolutions.trading.controller;

import com.poseidoncapitalsolutions.trading.TradingApplication;

import com.poseidoncapitalsolutions.trading.model.Trade;
import com.poseidoncapitalsolutions.trading.repository.TradeRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TradingApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "classpath:insert_trade_test_values.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class TradeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetAllTrades() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attribute("trades", hasSize(2)));

        assertThat(tradeRepository.findAll())
                .extracting(Trade::getAccount)
                .containsExactly("Account1", "Account2");
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testShowAddForm() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeExists("tradeDTO"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testValidateTradeOk() throws Exception {
        int initialCount = tradeRepository.findAll().size();

        mockMvc.perform(post("/trade/validate")
                        .param("account", "Account3")
                        .param("type", "TypeC")
                        .param("buyQuantity", "300.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        List<Trade> trades = tradeRepository.findAll();
        assertThat(trades).hasSize(initialCount + 1);

        Trade createdTrade = trades.getLast();
        assertThat(createdTrade.getAccount()).isEqualTo("Account3");
        assertThat(createdTrade.getType()).isEqualTo("TypeC");
        assertThat(createdTrade.getBuyQuantity()).isEqualTo(300.0);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testShowUpdateFormOk() throws Exception {
        Trade existingTrade = tradeRepository.findAll().getFirst();

        mockMvc.perform(get("/trade/update/" + existingTrade.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeExists("tradeDTO"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testShowUpdateFormNonExistingId() throws Exception {
        mockMvc.perform(get("/trade/update/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testUpdateTradeOk() throws Exception {
        Trade existingTrade = tradeRepository.findAll().getFirst();

        mockMvc.perform(post("/trade/update/" + existingTrade.getId())
                        .param("id", Long.toString(existingTrade.getId()))
                        .param("account", "UpdatedAccount")
                        .param("type", "UpdatedType")
                        .param("buyQuantity", "999.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        Trade updatedTrade = tradeRepository.findById(existingTrade.getId()).orElseThrow();
        assertThat(updatedTrade.getAccount()).isEqualTo("UpdatedAccount");
        assertThat(updatedTrade.getType()).isEqualTo("UpdatedType");
        assertThat(updatedTrade.getBuyQuantity()).isEqualTo(999.0);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteTradeOk() throws Exception {
        int initialCount = tradeRepository.findAll().size();
        Trade tradeToDelete = tradeRepository.findAll().getFirst();

        mockMvc.perform(get("/trade/delete/" + tradeToDelete.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        assertThat(tradeRepository.findAll()).hasSize(initialCount - 1);
        assertThat(tradeRepository.findById(tradeToDelete.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteTradeNonExistingId() throws Exception {
        mockMvc.perform(get("/trade/delete/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }
}