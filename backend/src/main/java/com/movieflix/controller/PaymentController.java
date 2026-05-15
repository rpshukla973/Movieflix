package com.movieflix.controller;

import com.movieflix.dto.payment.PaymentCreateRequest;
import com.movieflix.dto.payment.PaymentWebhookRequest;
import com.movieflix.entity.Payment;
import com.movieflix.service.PaymentService;
import com.movieflix.service.UserContextService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final UserContextService userContextService;

    @PostMapping
    public Payment create(@Valid @RequestBody PaymentCreateRequest request) {
        return paymentService.createPayment(userContextService.getCurrentUser(), request);
    }

    @PostMapping("/webhook")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void webhook(@Valid @RequestBody PaymentWebhookRequest request) {
        paymentService.processWebhook(request);
    }
}
