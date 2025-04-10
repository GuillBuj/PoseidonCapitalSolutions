package com.poseidoncapitalsolutions.trading.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poseidoncapitalsolutions.trading.dto.UserCreateDTO;
import com.poseidoncapitalsolutions.trading.dto.UserUpdateDTO;
import com.poseidoncapitalsolutions.trading.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller handling User-related requests.
 */
@Controller
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * Displays the list of users.
     */
    @RequestMapping("/user/list")
    public String home(Model model) {
        log.debug("GET - /user/list");

        model.addAttribute("users", userService.getAllUsers());

        return "user/list";
    }

    /**
     * Shows the form to add a new user.
     */
    @GetMapping("/user/add")
    public String addUser(Model model) {
        log.debug("GET - /user/add");

        model.addAttribute("userDTO", new UserCreateDTO("", "", "", "USER"));

        return "user/add";
    }

    /**
     * Validates and creates a new user.
     */
    @PostMapping("/user/validate")
    public String validate(@Valid @ModelAttribute("userDTO") UserCreateDTO userDTO, BindingResult result, Model model) {
        log.info("POST - /user/validate : {}", userDTO);

        if (result.hasErrors()) {
            model.addAttribute("userDTO", userDTO);
            return "user/add";
        }

        userService.createUser(userDTO);

        return "redirect:/user/list";
    }

    /**
     * Shows the form to update an existing user.
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        log.debug("GET - /user/update/{}", id);

        model.addAttribute("userDTO", userService.getUserUpdateDTO(id));

        return "user/update";
    }

    /**
     * Updates an existing user.
     */
    @PostMapping("/user/update")
    public String updateUser(@Valid @ModelAttribute("userDTO") UserUpdateDTO userDTO, BindingResult result, Model model) {
        log.info("POST - /user/update : {}", userDTO);

        if (result.hasErrors()) {
            model.addAttribute("userDTO", userDTO);
            return "user/update";
        }

        userService.updateUser(userDTO);

        return "redirect:/user/list";
    }

    /**
     * Deletes a user by ID.
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        log.info("GET - /user/delete/{}", id);

        userService.deleteById(id);

        return "redirect:/user/list";
    }
}
