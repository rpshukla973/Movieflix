package com.movieflix.service;

import com.movieflix.dto.payment.PaymentCreateRequest;
import com.movieflix.dto.payment.PaymentWebhookRequest;
import com.movieflix.entity.Payment;
import com.movieflix.entity.User;

public interface PaymentService {
    Payment createPayment(User user, PaymentCreateRequest request);
    void processWebhook(PaymentWebhookRequest request);
}
