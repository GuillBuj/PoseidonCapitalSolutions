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

import com.poseidoncapitalsolutions.trading.dto.RuleNameAddDTO;
import com.poseidoncapitalsolutions.trading.dto.RuleNameUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.RuleNameListItemDTO;
import com.poseidoncapitalsolutions.trading.exception.RuleNameNotFoundException;
import com.poseidoncapitalsolutions.trading.mapper.RuleNameMapper;
import com.poseidoncapitalsolutions.trading.model.RuleName;
import com.poseidoncapitalsolutions.trading.repository.RuleNameRepository;
import com.poseidoncapitalsolutions.trading.service.RuleNameService;

@ExtendWith(MockitoExtension.class)
class RuleNameServiceTest {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @Mock
    private RuleNameMapper ruleNameMapper;

    @InjectMocks
    private RuleNameService ruleNameService;

    private RuleName ruleName;

    @BeforeEach
    void setUp() {
        ruleName = new RuleName();
        ruleName.setId(1);
        ruleName.setName("Rule1");
        ruleName.setDescription("Test rule");
        ruleName.setJson("{}");
        ruleName.setTemplate("Template1");
        ruleName.setSqlStr("SELECT * FROM rules");
        ruleName.setSqlPart("WHERE id = 1");
    }

    @Test
    void getAllRuleNamesOk() {
        RuleNameListItemDTO expectedListItemDTO = new RuleNameListItemDTO(1, "Rule1", "Test rule", "{}", "Template1", "SELECT * FROM rules", "WHERE id = 1");

        when(ruleNameRepository.findAll()).thenReturn(Arrays.asList(ruleName));
        when(ruleNameMapper.toListItemDTOList(anyList())).thenReturn(Arrays.asList(expectedListItemDTO));
        
        List<RuleNameListItemDTO> result = ruleNameService.getAll();
        
        assertEquals(1, result.size());
        assertEquals(expectedListItemDTO, result.get(0));
        verify(ruleNameRepository).findAll();
    }

    @Test
    void createRuleNameOk() {
        RuleNameAddDTO addDTO = new RuleNameAddDTO("Rule1", "Test rule", "{}", "Template1", "SELECT * FROM rules", "WHERE id = 1");
        
        when(ruleNameMapper.toEntity(any())).thenReturn(ruleName);
        when(ruleNameRepository.save(any())).thenReturn(ruleName);
        
        RuleName result = ruleNameService.createRuleName(addDTO);
        
        assertNotNull(result);
        assertEquals("Rule1", result.getName());
        assertEquals("Test rule", result.getDescription());
        assertEquals("{}", result.getJson());
        assertEquals("Template1", result.getTemplate());
        assertEquals("SELECT * FROM rules", result.getSqlStr());
        assertEquals("WHERE id = 1", result.getSqlPart());
        verify(ruleNameRepository).save(ruleName);
    }

    @Test
    void updateRuleNameOk() {
        RuleNameUpdateDTO updateDTO = new RuleNameUpdateDTO(1, "UpdatedRule", "Updated description", "{updated}", "UpdatedTemplate", "SELECT * FROM updated_rules", "WHERE id = 2");

        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName));
        when(ruleNameRepository.save(ruleName)).thenReturn(ruleName);

        RuleName result = ruleNameService.updateRuleName(updateDTO);

        assertEquals(1, result.getId());
        verify(ruleNameMapper).updateEntityFromDTO(updateDTO, ruleName);
        verify(ruleNameRepository).save(ruleName);
    }

    @Test
    void updateRuleNameNotFound() {
        int nonExistentId = 999;
        RuleNameUpdateDTO updateDTO = new RuleNameUpdateDTO(nonExistentId, "UpdatedRule", "Updated description", "{updated}", "UpdatedTemplate", "SELECT * FROM updated_rules", "WHERE id = 2");
        
        when(ruleNameRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(RuleNameNotFoundException.class, () -> ruleNameService.updateRuleName(updateDTO));
        verify(ruleNameRepository, never()).save(any());
    }

    @Test
    void deleteByIdOk() {
        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(ruleName));
        doNothing().when(ruleNameRepository).delete(any());
        
        ruleNameService.deleteById(1);
        
        verify(ruleNameRepository).delete(any());
    }

    @Test
    void deleteByIdNotFound() {
        int nonExistentId = 999;
        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.empty());
       
        assertThrows(RuleNameNotFoundException.class, () -> ruleNameService.deleteById(nonExistentId));

        verify(ruleNameRepository, never()).delete(any());
    }

    @Test
    void getRuleNameUpdateDTOOk() { 
        RuleNameUpdateDTO expectedUpdateDTO = new RuleNameUpdateDTO(1, "Rule1", "Test rule", "{}", "Template1", "SELECT * FROM rules", "WHERE id = 1");
        
        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.of(ruleName));
        when(ruleNameMapper.toDTO(ruleName)).thenReturn(expectedUpdateDTO);
        
        RuleNameUpdateDTO result = ruleNameService.getRuleNameUpdateDTO(1);
        
        assertNotNull(result);
        assertEquals(expectedUpdateDTO, result);
        verify(ruleNameRepository).findById(anyInt());
    }

    @Test
    void getRuleNameUpdateDTONotFound() {
        int nonExistentId = 999;
        when(ruleNameRepository.findById(anyInt())).thenReturn(Optional.empty());
       
        assertThrows(RuleNameNotFoundException.class, () -> ruleNameService.getRuleNameUpdateDTO(nonExistentId));
    }
}
