package com.farmover.server.farmover.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.Services;
import com.farmover.server.farmover.entities.Storage;
import com.farmover.server.farmover.entities.StorageBookings;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.payloads.request.AddProductionToWarehouseDto;
import com.farmover.server.farmover.payloads.request.AddServiceToProductionDto;
import com.farmover.server.farmover.payloads.request.AddServicesToProductionDto;
import com.farmover.server.farmover.payloads.request.CompanyPurchaseDto;
import com.farmover.server.farmover.repositories.ServicesRepo;
import com.farmover.server.farmover.repositories.StorageBookingsRepo;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.repositories.WareHouseRepo;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class PaymentServiceImpl {

        @Value("${stripe.api.key}")
        private String stripeApiKey;

        @Autowired
        UserRepo userRepo;

        @Autowired
        ProductionServiceImpl productionService;

        @Autowired
        private WareHouseRepo wareHouseRepo;

        @Autowired
        private ServicesRepo servicesRepo;

        @Autowired
        private StorageBookingsRepo storageBookingsRepo;

        public PaymentIntent createPaymentIntent(Map<String, Object> paymentParams) throws StripeException {
                return PaymentIntent.create(paymentParams);
        }

        public PaymentIntent confirmPaymentIntent(String paymentIntentId, Map<String, Object> confirmParams)
                        throws StripeException {
                return PaymentIntent.retrieve(paymentIntentId).confirm(confirmParams);
        }

        public Map<String, String> checkoutPayment(AddServiceToProductionDto checkoutRequest) throws StripeException {

                Services service = servicesRepo.findById(checkoutRequest.getServiceId())
                                .orElseThrow(() -> new ResourceNotFoundException("Service", "id",
                                                checkoutRequest.getServiceId().toString()));

                Stripe.apiKey = stripeApiKey;

                SessionCreateParams params = SessionCreateParams.builder()
                                .setMode(SessionCreateParams.Mode.PAYMENT)
                                .setSuccessUrl("http://localhost:5173/farmer/payment/success?session_id={CHECKOUT_SESSION_ID}&type=service&Id="
                                                + checkoutRequest.getServiceId() + "&duration="
                                                + checkoutRequest.getDuration()
                                                + "&productionToken="
                                                + checkoutRequest.getProductionToken())
                                .setCancelUrl("http://localhost:5173/cancel")
                                .setShippingAddressCollection(SessionCreateParams.ShippingAddressCollection.builder()
                                                .addAllowedCountry(
                                                                SessionCreateParams.ShippingAddressCollection.AllowedCountry.IN)
                                                .build())

                                .addLineItem(
                                                SessionCreateParams.LineItem.builder()
                                                                .setQuantity(1l)
                                                                .setPriceData(SessionCreateParams.LineItem.PriceData
                                                                                .builder()
                                                                                .setCurrency("INR")

                                                                                .setUnitAmount((long) (checkoutRequest
                                                                                                .getDuration()
                                                                                                * service.getPricePerDay()
                                                                                                * 100))
                                                                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData
                                                                                                .builder()
                                                                                                .setName(service.getServiceName())
                                                                                                .build())
                                                                                .build())
                                                                .build())
                                .build();

                Session session = Session.create(params);

                Map<String, String> responseData = new HashMap<>();
                responseData.put("sessionId", session.getId());

                return responseData;
        }

        public Map<String, String> checkoutPaymentForServices(AddServicesToProductionDto checkoutRequest)
                        throws StripeException {

                List<Services> services = new ArrayList<>();

                for (Integer serviceId : checkoutRequest.getServiceIds()) {
                        Services service = servicesRepo.findById(serviceId)
                                        .orElseThrow(() -> new ResourceNotFoundException("Service", "id",
                                                        serviceId.toString()));

                        services.add(service);
                }

                String names = services.stream().map(service -> service.getServiceName()).reduce("",
                                (a, b) -> a + ", " + b);

                Long estimatedPrice = 0l;

                for (int i = 0; i < checkoutRequest.getServiceIds().size(); i++) {
                        estimatedPrice += (long) (checkoutRequest.getDurations().get(i)
                                        * services.get(i).getPricePerDay());
                }

                List<String> serviceIdStrings = checkoutRequest.getServiceIds().stream()
                                .map(String::valueOf)
                                .collect(Collectors.toList());
                String serviceIds = String.join(",", serviceIdStrings);
                String durations = checkoutRequest.getDurations().stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(","));

                Stripe.apiKey = stripeApiKey;

                SessionCreateParams params = SessionCreateParams.builder()
                                .setMode(SessionCreateParams.Mode.PAYMENT)
                                .setSuccessUrl("http://localhost:5173/farmer/payment/success?session_id={CHECKOUT_SESSION_ID}&type=services&Id="
                                                + serviceIds + "&duration="
                                                + durations
                                                + "&productionToken="
                                                + checkoutRequest.getProductionToken())
                                .setCancelUrl("http://localhost:5173/cancel")
                                .setShippingAddressCollection(SessionCreateParams.ShippingAddressCollection.builder()
                                                .addAllowedCountry(
                                                                SessionCreateParams.ShippingAddressCollection.AllowedCountry.IN)
                                                .build())

                                .addLineItem(SessionCreateParams.LineItem.builder()
                                                .setQuantity(1l)
                                                .setPriceData(SessionCreateParams.LineItem.PriceData
                                                                .builder()
                                                                .setCurrency("INR")
                                                                .setUnitAmount(estimatedPrice * 100)
                                                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData
                                                                                .builder()
                                                                                .setName(names)
                                                                                .build())
                                                                .build())
                                                .build())
                                .build();

                Session session = Session.create(params);

                Map<String, String> responseData = new HashMap<>();
                responseData.put("sessionId", session.getId());

                return responseData;
        }

        public Map<String, String> checkoutPaymentForWarehouse(AddProductionToWarehouseDto dto) throws StripeException {
                Stripe.apiKey = stripeApiKey;

                Storage storage = wareHouseRepo.findById(dto.getWarehouseId())
                                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", "id",
                                                dto.getWarehouseId().toString()))
                                .getStorages().stream()
                                .filter(s -> s.getStorageType().equals(dto.getStorageType()))
                                .findFirst()
                                .orElseThrow(() -> new ResourceNotFoundException("Storage", "storage type",
                                                dto.getStorageType().toString()));

                SessionCreateParams params = SessionCreateParams.builder()
                                .setMode(SessionCreateParams.Mode.PAYMENT)
                                .setSuccessUrl("http://localhost:5173/farmer/payment/success?session_id={CHECKOUT_SESSION_ID}&type=warehouse&Id="
                                                + dto.getWarehouseId() + "&storageType="
                                                + dto.getStorageType()
                                                + "&weight="
                                                + dto.getWeight()
                                                + "&duration="
                                                + dto.getDuration()
                                                + "&markForSale="
                                                + dto.getMarkForSale()
                                                + "&minimumPrice="
                                                + dto.getMinimumPrice()
                                                + "&minimumUnit="
                                                + dto.getMinimumUnit()
                                                + "&productionToken="
                                                + dto.getProductionToken())
                                .setCancelUrl("http://localhost:5173/cancel")
                                .setShippingAddressCollection(SessionCreateParams.ShippingAddressCollection.builder()
                                                .addAllowedCountry(
                                                                SessionCreateParams.ShippingAddressCollection.AllowedCountry.IN)
                                                .build())

                                .addLineItem(
                                                SessionCreateParams.LineItem.builder()
                                                                .setQuantity(1l)
                                                                .setPriceData(SessionCreateParams.LineItem.PriceData
                                                                                .builder()
                                                                                .setCurrency("INR")

                                                                                .setUnitAmount((long) (dto.getWeight()
                                                                                                * storage.getPricePerKg()
                                                                                                * 100))
                                                                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData
                                                                                                .builder()
                                                                                                .setName(storage.getWarehouse()
                                                                                                                .getName())
                                                                                                .build())
                                                                                .build())
                                                                .build())
                                .build();

                Session session = Session.create(params);

                Map<String, String> responseData = new HashMap<>();
                responseData.put("sessionId", session.getId());

                return responseData;
        }

        public Map<String, String> checkoutPaymentForCompanies(CompanyPurchaseDto dto) throws StripeException {
                Stripe.apiKey = stripeApiKey;

                Long price = 0L;
                String names = "";
                for (Map.Entry<Integer, Double> entry : dto.getProductionTokens().entrySet()) {
                        Integer token = entry.getKey();
                        Double quantity = entry.getValue();

                        StorageBookings booking = storageBookingsRepo.findStorageBookingsByProductionToken(token)
                                        .orElseThrow(() -> new ResourceNotFoundException("Storage Booking",
                                                        "production token", token.toString()));

                        price += (long) (quantity * booking.getItemPrice());
                        names += booking.getCropName() + ", ";
                }

                SessionCreateParams params = SessionCreateParams.builder()
                                .setMode(SessionCreateParams.Mode.PAYMENT)
                                .setSuccessUrl("http://localhost:5173/company/payment/success?session_id={CHECKOUT_SESSION_ID}&type=company&productionTokens="
                                                + dto.getProductionTokens().keySet().stream().map(String::valueOf)
                                                                .collect(Collectors.joining(","))
                                                + "&quantities="
                                                + dto.getProductionTokens().values().stream().map(String::valueOf)
                                                                .collect(Collectors.joining(",")))
                                .setCancelUrl("http://localhost:5173/company/payment/failure")
                                .setShippingAddressCollection(SessionCreateParams.ShippingAddressCollection.builder()
                                                .addAllowedCountry(
                                                                SessionCreateParams.ShippingAddressCollection.AllowedCountry.IN)
                                                .build())

                                .addLineItem(
                                                SessionCreateParams.LineItem.builder()
                                                                .setQuantity(1l)
                                                                .setPriceData(SessionCreateParams.LineItem.PriceData
                                                                                .builder()
                                                                                .setCurrency("INR")

                                                                                .setUnitAmount(price * 100)
                                                                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData
                                                                                                .builder()
                                                                                                .setName(names)
                                                                                                .build())
                                                                                .build())
                                                                .build())
                                .build();

                Session session = Session.create(params);

                Map<String, String> responseData = new HashMap<>();
                responseData.put("sessionId", session.getId());

                return responseData;
        }

        public Map<String, Object> verifyPayment(String sessionId) throws StripeException {
                Stripe.apiKey = stripeApiKey;

                Session session = Session.retrieve(sessionId);

                Map<String, Object> responseData = new HashMap<>();
                responseData.put("paymentStatus", session.getPaymentStatus());
                responseData.put("sessionId", session.getId());
                responseData.put("amountTotal", session.getAmountTotal());
                responseData.put("currency", session.getCurrency());
                responseData.put("customerEmail", session.getCustomerDetails().getEmail());

                return responseData;
        }
}
