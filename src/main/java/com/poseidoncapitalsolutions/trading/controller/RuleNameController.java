package com.poseidoncapitalsolutions.trading.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poseidoncapitalsolutions.trading.dto.RuleNameAddDTO;
import com.poseidoncapitalsolutions.trading.dto.RuleNameUpdateDTO;
import com.poseidoncapitalsolutions.trading.model.RuleName;
import com.poseidoncapitalsolutions.trading.service.RuleNameService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Controller handling RuleName-related requests.
 */
@Controller
@AllArgsConstructor
@Slf4j
public class RuleNameController {

    private final RuleNameService ruleNameService;

    /**
     * Displays the list of rule names.
     */
    @GetMapping("/ruleName/list")
    public String home(Model model) {
        log.debug("GET - /ruleName/list");
        model.addAttribute("ruleNames", ruleNameService.getAll());
        return "ruleName/list";
    }

    /**
     * Shows the form to add a new rule name.
     */
    @GetMapping("/ruleName/add")
    public String addRuleNameForm(Model model) {
        log.debug("GET - /ruleName/add");
        model.addAttribute("ruleNameDTO", new RuleNameAddDTO(null, null, null, null, null, null));
        return "ruleName/add";
    }

    /**
     * Validates and creates a new rule name.
     */
    @PostMapping("/ruleName/validate")
    public String validate(@Valid @ModelAttribute("ruleNameDTO") RuleNameAddDTO ruleNameDTO, BindingResult result, Model model) {
        log.info("POST - /ruleName/validate : {}", ruleNameDTO);

        if (result.hasErrors()) {
            log.warn("Validation error");
            model.addAttribute("ruleNameDTO", ruleNameDTO);
            return "ruleName/add";
        }

        RuleName newRuleName = ruleNameService.createRuleName(ruleNameDTO);
        log.info("RuleName successfully created with ID[{}]", newRuleName.getId());

        return "redirect:/ruleName/list";
    }

    /**
     * Shows the form to update an existing rule name.
     */
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("ruleNameDTO", ruleNameService.getRuleNameUpdateDTO(id));
        return "ruleName/update";
    }

    /**
     * Updates an existing rule name.
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid @ModelAttribute("ruleNameDTO") RuleNameUpdateDTO ruleNameDTO, BindingResult result, Model model) {
        log.info("POST - /ruleName/update/{} : {}", id, ruleNameDTO);

        if (result.hasErrors()) {
            log.warn("Validation error");
            model.addAttribute("ruleNameDTO", ruleNameDTO);
            return "ruleName/update";
        }

        RuleName updatedRuleName = ruleNameService.updateRuleName(ruleNameDTO);
        log.info("RuleName successfully updated with ID[{}]", updatedRuleName.getId());
        return "redirect:/ruleName/list";
    }

    /**
     * Deletes a rule name by ID.
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id) {
        log.info("GET - /ruleName/delete/{}", id);
        ruleNameService.deleteById(id);
        return "redirect:/ruleName/list";
    }
}
