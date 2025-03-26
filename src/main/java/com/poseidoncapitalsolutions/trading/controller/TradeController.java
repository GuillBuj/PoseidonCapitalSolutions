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

import com.poseidoncapitalsolutions.trading.dto.TradeAddDTO;
import com.poseidoncapitalsolutions.trading.dto.TradeUpdateDTO;
import com.poseidoncapitalsolutions.trading.model.Trade;
import com.poseidoncapitalsolutions.trading.service.TradeService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class TradeController {
    
    @Autowired
    private TradeService tradeService;

    @RequestMapping("/trade/list")
    public String home(Model model) {
        log.debug("GET - /trade/list");
        
        model.addAttribute("trades", tradeService.getAll());

        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addTrade(Model model) {
        log.debug("GET - /trade/add");

        model.addAttribute("tradeDTO", new TradeAddDTO(null, null, null));

        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid @ModelAttribute("tradeDTO")TradeAddDTO tradeDTO, BindingResult result, Model model) {
        log.info("POST - /trade/validate : {}", tradeDTO);

        if (result.hasErrors()) {
            log.warn("Validation error");
            model.addAttribute("tradeDTO", tradeDTO);
            return "trade/add";           
        }

        Trade newTrade = tradeService.createTrade(tradeDTO);

        log.info("Trade successfully created with ID[{}]", newTrade.getId());
        return "trade/list";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.debug("GET - /trade/update/{}", id);

        model.addAttribute("tradeDTO", tradeService.getTradeUpdateDTO(id));

        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid @ModelAttribute("tradeDTO")TradeUpdateDTO tradeDTO,
                             BindingResult result, Model model) {
        log.info("POST - /trade/update/{} : {}", tradeDTO.id());
        
        if (result.hasErrors()) {
            log.warn("Validation error");
            model.addAttribute("tradeDTO", tradeDTO);
            return "trade/update";           
        }

        Trade updatedTrade = tradeService.updateTrade(tradeDTO);

        log.info("Trade successfully updated with ID[{}]", updatedTrade.getId());
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        log.info("POST - /trade/delete/{} : {}", id);
        
        tradeService.deleteById(id);
        
        return "redirect:/trade/list";
    }  

}
