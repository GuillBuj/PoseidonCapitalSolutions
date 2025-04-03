package poseidoncapitalsolutions.trading.controller;

import com.poseidoncapitalsolutions.trading.TradingApplication;
import com.poseidoncapitalsolutions.trading.model.RuleName;
import com.poseidoncapitalsolutions.trading.repository.RuleNameRepository;

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
@Sql(scripts = "classpath:insert_rule_name_test_values.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class RuleNameControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RuleNameRepository ruleNameRepository;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetAllRuleNames() throws Exception {
        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attribute("ruleNames", hasSize(2)));

        assertThat(ruleNameRepository.findAll())
                .extracting(RuleName::getName)
                .containsExactly("Rule1", "Rule2");
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testShowAddForm() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(model().attributeExists("ruleNameDTO"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testValidateRuleNameOk() throws Exception {
        int initialCount = ruleNameRepository.findAll().size();

        mockMvc.perform(post("/ruleName/validate")
                        .param("name", "Rule3")
                        .param("description", "Description3")
                        .param("json", "{\"param\": 300}")
                        .param("template", "Template3")
                        .param("sqlStr", "SELECT * FROM table3")
                        .param("sqlPart", "WHERE field=3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        List<RuleName> ruleNames = ruleNameRepository.findAll();
        assertThat(ruleNames).hasSize(initialCount + 1);

        RuleName createdRule = ruleNames.getLast();
        assertThat(createdRule.getName()).isEqualTo("Rule3");
        assertThat(createdRule.getDescription()).isEqualTo("Description3");
        assertThat(createdRule.getJson()).isEqualTo("{\"param\": 300}");
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testShowUpdateFormOk() throws Exception {
        RuleName existingRule = ruleNameRepository.findAll().getFirst();

        mockMvc.perform(get("/ruleName/update/" + existingRule.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeExists("ruleNameDTO"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testShowUpdateFormNonExistingId() throws Exception {
        mockMvc.perform(get("/ruleName/update/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testUpdateRuleNameOk() throws Exception {
        RuleName existingRule = ruleNameRepository.findAll().getFirst();

        mockMvc.perform(post("/ruleName/update/" + existingRule.getId())
                        .param("id", Integer.toString(existingRule.getId()))
                        .param("name", "UpdatedRule")
                        .param("description", "UpdatedDescription")
                        .param("json", "{\"param\": 999}")
                        .param("template", "UpdatedTemplate")
                        .param("sqlStr", "UPDATED SQL")
                        .param("sqlPart", "UPDATED PART"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        RuleName updatedRule = ruleNameRepository.findById(existingRule.getId()).orElseThrow();
        assertThat(updatedRule.getName()).isEqualTo("UpdatedRule");
        assertThat(updatedRule.getSqlStr()).isEqualTo("UPDATED SQL");
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteRuleNameOk() throws Exception {
        int initialCount = ruleNameRepository.findAll().size();
        RuleName ruleToDelete = ruleNameRepository.findAll().getFirst();

        mockMvc.perform(get("/ruleName/delete/" + ruleToDelete.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        assertThat(ruleNameRepository.findAll()).hasSize(initialCount - 1);
        assertThat(ruleNameRepository.findById(ruleToDelete.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteRuleNameNonExistingId() throws Exception {
        mockMvc.perform(get("/ruleName/delete/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }
}