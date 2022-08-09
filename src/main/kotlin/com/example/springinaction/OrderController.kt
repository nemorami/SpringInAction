package com.example.springinaction;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.bind.support.SessionStatus
import javax.validation.Valid


@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
class OrderController(val orderRepo: OrderRepository) {
    private val log = org.slf4j.LoggerFactory.getLogger(DesignTacoController::class.java)

    @GetMapping("/current")
    fun orderForm(tacoOrder: TacoOrder): String {
        log.info("Processing tacoOrder: ${tacoOrder}")
        return "orderForm"
    }
    @PostMapping
    fun processOrder(@Valid order: TacoOrder, errors: Errors, sessionStatus: SessionStatus): String {
        log.info("Order submitted: ${order}")
        log.info("taco in Order: ${order.tacos}")
        if(errors.hasErrors())
            return "orderForm"
        orderRepo.save(order)
        sessionStatus.setComplete()
        return "redirect:/"
    }
}
