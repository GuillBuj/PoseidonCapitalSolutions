package com.poseidoncapitalsolutions.trading.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poseidoncapitalsolutions.trading.dto.CurvePointAddDTO;
import com.poseidoncapitalsolutions.trading.dto.CurvePointUpdateDTO;
import com.poseidoncapitalsolutions.trading.model.CurvePoint;
import com.poseidoncapitalsolutions.trading.service.CurvePointService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Controller handling CurvePoint-related requests.
 */
@Controller
@AllArgsConstructor
@Slf4j
public class CurvePointController {

    private final CurvePointService curvePointService;

    /**
     * Displays the list of curve points.
     */
    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        log.debug("GET - /curvePoint/list");
        model.addAttribute("curvePoints", curvePointService.getAll());
        return "curvePoint/list";
    }

    /**
     * Shows the form to add a new curve point.
     */
    @GetMapping("/curvePoint/add")
    public String addBidForm(Model model) {
        log.debug("GET - /curvePoint/add");
        model.addAttribute("curvePointDTO", new CurvePointAddDTO(null, null, null));
        return "curvePoint/add";
    }

    /**
     * Validates and creates a new curve point.
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid @ModelAttribute("curvePointDTO") CurvePointAddDTO curvePointDTO, BindingResult result, Model model) {
        log.info("POST - /curvePoint/validate : {}", curvePointDTO);

        if (result.hasErrors()) {
            log.warn("Validation error");
            model.addAttribute("curvePointDTO", curvePointDTO);
            return "curvePoint/add";
        }

        CurvePoint newCurvePoint = curvePointService.createCurvePoint(curvePointDTO);
        log.info("CurvePoint successfully created with ID[{}]", newCurvePoint.getId());

        return "redirect:/curvePoint/list";
    }

    /**
     * Shows the form to update an existing curve point.
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("curvePointDTO", curvePointService.getCurvePointUpdateDTO(id));
        return "curvePoint/update";
    }

    /**
     * Updates an existing curve point.
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@Valid @ModelAttribute("curvePointDTO") CurvePointUpdateDTO curvePointDTO, BindingResult result, Model model) {
        log.info("POST - /curvePoint/update/{id} : {}", curvePointDTO.id());

        if (result.hasErrors()) {
            log.warn("Validation error");
            model.addAttribute("curvePointDTO", curvePointDTO);
            return "curvePoint/add";
        }

        CurvePoint updatedCurvePoint = curvePointService.updateCurvePoint(curvePointDTO);
        log.info("CurvePoint successfully updated with ID[{}]", updatedCurvePoint.getId());
        return "redirect:/curvePoint/list";
    }

    /**
     * Deletes a curve point by ID.
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        log.info("GET - /curvePoint/delete/{}", id);
        curvePointService.deleteById(id);
        return "redirect:/curvePoint/list";
    }
}
