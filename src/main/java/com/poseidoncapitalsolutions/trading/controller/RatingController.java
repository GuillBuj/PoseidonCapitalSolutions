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

import com.poseidoncapitalsolutions.trading.dto.CurvePointAddDTO;
import com.poseidoncapitalsolutions.trading.dto.RatingAddDTO;
import com.poseidoncapitalsolutions.trading.dto.RatingUpdateDTO;
import com.poseidoncapitalsolutions.trading.model.CurvePoint;
import com.poseidoncapitalsolutions.trading.model.Rating;
import com.poseidoncapitalsolutions.trading.service.RatingService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class RatingController {
    @Autowired
    private RatingService ratingService;

    @RequestMapping("/rating/list")
    public String home(Model model) {
        log.debug("GET - /rating/list");

        model.addAttribute("ratings", ratingService.getAll());

        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Model model) {
        log.debug("GET - /rating/add");

        model.addAttribute("ratingDTO", new RatingAddDTO(null, null, null, null));
        
        return "rating/add";
    }

    //TODO : voir types
    @PostMapping("/rating/validate")
    public String validate(@Valid @ModelAttribute("ratingDTO") RatingAddDTO ratingDTO, BindingResult result, Model model) {
        log.info("POST - /rating/validate : {}", ratingDTO);

        if(result.hasErrors()){
            log.warn("Validation error");
            model.addAttribute("ratingDTO", ratingDTO);
            return "rating/add";
        }

        Rating newRating = ratingService.createRating(ratingDTO);
        log.info("Rating successfully created with ID[{}]", newRating.getId());

        return "redirect:/rating/list";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.debug("GET - /rating/update");

        model.addAttribute("ratingDTO", ratingService.getRatingUpdateDTO(id));
        
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@Valid @ModelAttribute("ratingDTO") RatingUpdateDTO ratingDTO, BindingResult result, Model model) {
        log.info("POST - /rating/update/{}", ratingDTO.id());

        if(result.hasErrors()){
            log.warn("Validation error");
            model.addAttribute("ratingDTO", ratingDTO);
            return "rating/update";
        }

        Rating updatedRating = ratingService.updateRating(ratingDTO);
        log.info("Rating successfully updated with ID[{}]", updatedRating.getId());

        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        log.info("GET - /rating/delete/{}", id);

        ratingService.deleteById(id);

        return "redirect:/rating/list";
    }
}
