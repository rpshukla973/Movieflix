package com.movieflix.service.impl;

import com.movieflix.dto.payment.PaymentCreateRequest;
import com.movieflix.dto.payment.PaymentWebhookRequest;
import com.movieflix.entity.*;
import com.movieflix.exception.ApiException;
import com.movieflix.repository.PaymentRepository;
import com.movieflix.repository.SubscriptionRepository;
import com.movieflix.service.PaymentService;
import com.movieflix.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionService subscriptionService;

    @Override
    public Payment createPayment(User user, PaymentCreateRequest request) {
        Subscription subscription = subscriptionRepository.findById(request.subscriptionId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Subscription not found"));
        if (!subscription.getUser().getId().equals(user.getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Unauthorized subscription payment");
        }

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setSubscription(subscription);
        payment.setAmount(priceFor(subscription.getPlan()));
        payment.setPaymentMethod(request.paymentMethod());
        payment.setTransactionId("txn_" + UUID.randomUUID());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public void processWebhook(PaymentWebhookRequest request) {
        Payment payment = paymentRepository.findByTransactionId(request.transactionId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Transaction not found"));
        payment.setPaymentStatus(request.status());
        payment.setGatewayPayload(request.payload());
        paymentRepository.save(payment);

        if (request.status() == PaymentStatus.SUCCESS) {
            subscriptionService.activate(payment.getSubscription().getId());
        }
    }

    private BigDecimal priceFor(SubscriptionPlan plan) {
        return switch (plan) {
            case BASIC -> BigDecimal.valueOf(199);
            case STANDARD -> BigDecimal.valueOf(399);
            case PREMIUM -> BigDecimal.valueOf(699);
        };
    }
}
