package poseidoncapitalsolutions.trading.controller;


import java.util.Optional;
import java.util.List;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;

import com.poseidoncapitalsolutions.trading.TradingApplication;
import com.poseidoncapitalsolutions.trading.model.Bid;
import com.poseidoncapitalsolutions.trading.repository.BidRepository;

import jakarta.transaction.Transactional;

@SpringBootTest(classes = TradingApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "classpath:insert_bid_test_values.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class BidControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BidRepository bidRepository;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetAllBids() throws Exception {
        mockMvc.perform(get("/bid/list"))
               .andExpect(status().isOk())
               .andExpect(view().name("bid/list"))
               .andExpect(model().attribute("bids", hasSize(2)));

        assertThat(bidRepository.findAll())
            .extracting(Bid::getAccount)
            .containsExactly("Account1", "Account2");
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testShowAddForm() throws Exception {
        mockMvc.perform(get("/bid/add"))
               .andExpect(status().isOk())
               .andExpect(view().name("bid/add"))
               .andExpect(model().attributeExists("bidDTO"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testValidateBidOk() throws Exception {
        int initialCount = bidRepository.findAll().size();

        mockMvc.perform(post("/bid/validate")
               .param("account", "Account3")
               .param("type", "TypeC")
               .param("bidQuantity", "300.0"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/bid/list"));

        List<Bid> bids = bidRepository.findAll();
        assertThat(bids).hasSize(initialCount + 1);
        
        Bid createdBid = bids.get(bids.size() - 1);
        assertThat(createdBid.getAccount()).isEqualTo("Account3");
        assertThat(createdBid.getType()).isEqualTo("TypeC");
        assertThat(createdBid.getBidQuantity()).isEqualTo(300.0);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testValidateBidInvalidData() throws Exception {
        int initialCount = bidRepository.findAll().size();

        mockMvc.perform(post("/bid/validate")
               .param("account", "")
               .param("type", "TypeA")
               .param("bidQuantity", "100.0"))
               .andExpect(status().isOk())
               .andExpect(view().name("bid/add"))
               .andExpect(model().attributeHasFieldErrors("bidDTO", "account"));

        assertThat(bidRepository.findAll()).hasSize(initialCount);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testShowUpdateFormOk() throws Exception {
        Bid existingBid = bidRepository.findAll().get(0);

        mockMvc.perform(get("/bid/update/" + existingBid.getId()))
               .andExpect(status().isOk())
               .andExpect(view().name("bid/update"))
               .andExpect(model().attributeExists("bidDTO"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testShowUpdateFormNonExistingId() throws Exception {
        mockMvc.perform(get("/bid/update/999"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/bid/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testUpdateBidOk() throws Exception {
        Bid existingBid = bidRepository.findAll().get(0);

        mockMvc.perform(post("/bid/update/" + existingBid.getId())
               .param("id", Integer.toString(existingBid.getId()))
               .param("account", "UpdatedAccount")
               .param("type", "UpdatedType")
               .param("bidQuantity", "999.0"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/bid/list"));

        Bid updatedBid = bidRepository.findById(existingBid.getId()).orElseThrow();
        assertThat(updatedBid.getAccount()).isEqualTo("UpdatedAccount");
        assertThat(updatedBid.getType()).isEqualTo("UpdatedType");
        assertThat(updatedBid.getBidQuantity()).isEqualTo(999.0);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testUpdateBidInvalidData() throws Exception {
        Bid existingBid = bidRepository.findAll().get(0);
        String originalAccount = existingBid.getAccount();

        mockMvc.perform(post("/bid/update/" + existingBid.getId())
               .param("id", Integer.toString(existingBid.getId()))
               .param("account", "")
               .param("type", "UpdatedType")
               .param("bidQuantity", "999.0"))
               .andExpect(status().isOk())
               .andExpect(view().name("bid/add"))
               .andExpect(model().attributeHasFieldErrors("bidDTO", "account"));

        Bid unchangedBid = bidRepository.findById(existingBid.getId()).orElseThrow();
        assertThat(unchangedBid.getAccount()).isEqualTo(originalAccount);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteBidOk() throws Exception {
        int initialCount = bidRepository.findAll().size();
        Bid bidToDelete = bidRepository.findAll().get(0);

        mockMvc.perform(get("/bid/delete/" + bidToDelete.getId()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/bid/list"));

        assertThat(bidRepository.findAll()).hasSize(initialCount - 1);
        assertThat(bidRepository.findById(bidToDelete.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteBidNonExistingId() throws Exception {
        mockMvc.perform(get("/bid/delete/999"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/bid/list"));
    }
}