package com.poseidoncapitalsolutions.trading.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poseidoncapitalsolutions.trading.model.Bid;

import jakarta.validation.Valid;



@Controller
public class BidController {
    // TODO: Inject Bid service

    @RequestMapping("/bid/list")
    public String home(Model model)
    {
        // TODO: call service find all bids to show to the view
        return "bid/list";
    }

    @GetMapping("/bid/add")
    public String addBidForm(Bid bid) {
        return "bid/add";
    }

    @PostMapping("/bid/validate")
    public String validate(@Valid Bid bid, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return bid list
        return "bid/add";
    }

    @GetMapping("/bid/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get Bid by Id and to model then show to the form
        return "bid/update";
    }

    @PostMapping("/bid/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid Bid bid,
                             BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Bid and return list Bid
        return "redirect:/bid/list";
    }

    @GetMapping("/bid/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Bid by Id and delete the bid, return to Bid list
        return "redirect:/bid/list";
    }
}
