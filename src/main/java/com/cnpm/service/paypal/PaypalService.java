package com.cnpm.service.paypal;

import com.paypal.sdk.Environment;
import com.paypal.sdk.PaypalServerSdkClient;
import com.paypal.sdk.authentication.ClientCredentialsAuthModel;
import com.paypal.sdk.controllers.OrdersController;
import com.paypal.sdk.exceptions.ApiException;
import com.paypal.sdk.http.response.ApiResponse;
import com.paypal.sdk.models.*;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

@Service
public class PaypalService {
//    @Value("${PAYPAL_CLIENT_ID}")
//    private String PAYPAL_CLIENT_ID;
//
//    @Value("${PAYPAL_CLIENT_SECRET}")
//    private String PAYPAL_CLIENT_SECRET;

    @Autowired
    private PaypalServerSdkClient client;
//    @Autowired
//    public PaypalService(PaypalServerSdkClient client) {
//        this.client = client;
//    }
//    @Autowired
//    public PaypalService(PaypalServerSdkClient client) {
//        this.client = client;
//    }

//    @Bean
//    public PaypalServerSdkClient paypalClient() {
//        return new PaypalServerSdkClient.Builder()
//                .loggingConfig(builder -> builder
//                        .level(Level.DEBUG)
//                        .requestConfig(logConfigBuilder -> logConfigBuilder.body(true))
//                        .responseConfig(logConfigBuilder -> logConfigBuilder.headers(true)))
//                .httpClientConfig(configBuilder -> configBuilder
//                        .timeout(0))
//                .environment(Environment.SANDBOX)
//                .clientCredentialsAuth(new ClientCredentialsAuthModel.Builder(
//                        PAYPAL_CLIENT_ID,
//                        PAYPAL_CLIENT_SECRET)
//                        .build())
//                .build();
//    }
    public Order createOrder(String cart, Double amount) throws IOException, ApiException {
        double calculatedAmount = 0.000039 * amount;
        DecimalFormat df = new DecimalFormat("0.00");
        String formattedAmount = df.format(calculatedAmount);
        OrdersCreateInput ordersCreateInput = new OrdersCreateInput.Builder(
                null,
                new OrderRequest.Builder(
                        CheckoutPaymentIntent.CAPTURE,
                        Arrays.asList(
                                new PurchaseUnitRequest.Builder(
                                        new AmountWithBreakdown.Builder(
                                                "USD",
//													"999.00",
                                                formattedAmount
//                                                ,
//                                                String.valueOf(0.000039*amount)
                                        )
                                                .build())
                                        .build()))
                        .build())
                .build();

        OrdersController ordersController = client.getOrdersController();

        ApiResponse<Order> apiResponse = ordersController.ordersCreate(ordersCreateInput);

        return apiResponse.getResult();
    }

    public Order captureOrders(String orderID) throws IOException, ApiException {
        OrdersCaptureInput ordersCaptureInput = new OrdersCaptureInput.Builder(
                orderID,
                null)
                .build();
        OrdersController ordersController = client.getOrdersController();
        ApiResponse<Order> apiResponse = ordersController.ordersCapture(ordersCaptureInput);
        return apiResponse.getResult();
    }
}
