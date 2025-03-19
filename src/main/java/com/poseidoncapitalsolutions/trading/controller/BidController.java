package com.poseidoncapitalsolutions.trading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.poseidoncapitalsolutions.trading.dto.BidAddDTO;
import com.poseidoncapitalsolutions.trading.model.Bid;
import com.poseidoncapitalsolutions.trading.service.BidService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;



@Controller
@Slf4j
public class BidController {

    @Autowired
    private BidService bidService; 
    
    
    @GetMapping("/bid/list")
    public String home(Model model) {
        log.debug("GET - /bid/list");
        // TODO: call service find all bids to show to the view
        model.addAttribute("bids", bidService.getAllBids());
        
        return "bid/list";
    }

    
    @GetMapping("/bid/add")
    public String addBidForm(Model model) {
        log.debug("GET - /bid/add");

        model.addAttribute("bidDTO", new BidAddDTO(null, null, 0));
        return "bid/add";
    }

    @PostMapping("/bid/validate")
    public String validate(@Valid @ModelAttribute("bidDTO") BidAddDTO bidDTO, BindingResult result, Model model) {
        log.info("POST - /bid/validate : {}", bidDTO);

        if (result.hasErrors()) {
                log.warn("Validation error");
                model.addAttribute("bidDTO", bidDTO);
                return "bid/add";
        }

        Bid newBid=bidService.createBid(bidDTO);
        log.info("Bid created successfully with ID[{}]", newBid.getId());

        return "bid/list";
    }

    // @GetMapping("/bid/update/{id}")
    // public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
    //     // TODO: get Bid by Id and to model then show to the form
    //     return "bid/update";
    // }

    // @PostMapping("/bid/update/{id}")
    // public String updateBid(@PathVariable("id") Integer id, @Valid Bid bid,
    //                          BindingResult result, Model model) {
    //     // TODO: check required fields, if valid call service to update Bid and return list Bid
    //     return "redirect:/bid/list";
    // }

    // @GetMapping("/bid/delete/{id}")
    // public String deleteBid(@PathVariable("id") Integer id, Model model) {
    //     // TODO: Find Bid by Id and delete the bid, return to Bid list
    //     return "redirect:/bid/list";
    // }
}
