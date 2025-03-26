package com.poseidoncapitalsolutions.trading.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BidNotFoundException.class)
    public String handleBidNotFoundException(BidNotFoundException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/bid/list";
    }

    @ExceptionHandler(CurvePointNotFoundException.class)
    public String handleCurvePointNotFoundException(CurvePointNotFoundException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/curvePoint/list";
    }

    @ExceptionHandler(RatingNotFoundException.class)
    public String handleRatingNotFoundException(RatingNotFoundException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/rating/list";
    }

    @ExceptionHandler(RuleNameNotFoundException.class)
    public String handleRuleNameNotFoundException(RuleNameNotFoundException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/ruleName/list";
    }

    @ExceptionHandler(TradeNotFoundException.class)
    public String handleTradeNotFoundException(TradeNotFoundException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/trade/list";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/user/list";
    }
}
