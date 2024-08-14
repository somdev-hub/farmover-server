package com.farmover.server.farmover.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmover.server.farmover.payloads.request.AddServiceToProductionDto;
import com.farmover.server.farmover.payloads.request.AddServicesToProductionDto;
import com.farmover.server.farmover.services.impl.PaymentServiceImpl;
import com.stripe.exception.StripeException;

@RestController
@CrossOrigin
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentServiceImpl stripeService;

    @PostMapping("/create-session/add-service")
    public Map<String, String> createCheckoutSession(@RequestBody AddServiceToProductionDto checkoutRequest)
            throws StripeException {

        Map<String, String> responseData = stripeService.checkoutPayment(checkoutRequest);
        return responseData;
    }

    @PostMapping("/create-session/add-services")
    public Map<String, String> createCheckoutSessionForServices(@RequestBody AddServicesToProductionDto checkoutRequest)
            throws StripeException {

        Map<String, String> responseData = stripeService.checkoutPaymentForServices(checkoutRequest);
        return responseData;
    }

}
