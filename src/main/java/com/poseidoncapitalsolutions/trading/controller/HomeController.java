package com.poseidoncapitalsolutions.trading.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller handling requests for the home page.
 */
@Controller
public class HomeController {

	/**
	 * Displays the home page.
	 */
	@RequestMapping("/")
	public String home(Model model) {
		
		return "home";
	}

}
