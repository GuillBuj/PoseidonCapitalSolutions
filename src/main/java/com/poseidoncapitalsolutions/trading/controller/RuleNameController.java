package com.poseidoncapitalsolutions.trading.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class RuleNameController {
    
    @Autowired
    private RuleNameService ruleNameService;

    @RequestMapping("/ruleName/list")
    public String home(Model model){
        log.debug("GET - /ruleName/list");
        
        model.addAttribute("ruleNames", ruleNameService.getAll());
        
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(Model model) {
        log.debug("GET - /ruleName/add");

        model.addAttribute("ruleNameDTO", new RuleNameAddDTO(null, null, null, null, null, null));

        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid @ModelAttribute("ruleNameDTO") RuleNameAddDTO ruleNameDTO, BindingResult result, Model model) {
        log.info("POST - /rating/validate : {}", ruleNameDTO);

        if(result.hasErrors()){
            log.warn("Validation error");
            model.addAttribute("ruleNameDTO", ruleNameDTO);
            return "ruleName/add";
        }

        RuleName newRuleName = ruleNameService.createRuleName(ruleNameDTO);

        log.info("Rule name successfully created with ID[{}]", newRuleName.getId());
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        log.debug("GET - /ruleName/update/{}", id);

        model.addAttribute("ruleNameDTO", ruleNameService.getRuleNameUpdateDTO(id));

        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@Valid @ModelAttribute("ruleNameDTO") RuleNameUpdateDTO ruleNameDTO, @Valid RuleName ruleName,
                             BindingResult result, Model model) {
        log.info("POST - /rating/update/{}", ruleNameDTO.id());

        if(result.hasErrors()){
            log.warn("Validation error");
            model.addAttribute("ruleNameDTO", ruleNameDTO);
            return "ruleName/add";
        }

        RuleName updatedRuleName = ruleNameService.updateRuleName(ruleNameDTO);

        log.info("Rule name successfully updated with ID[{}]", updatedRuleName.getId());
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        log.info("GET - /rating/delete/{}", id);

        ruleNameService.deleteById(id);

        return "redirect:/ruleName/list";
    }

}
