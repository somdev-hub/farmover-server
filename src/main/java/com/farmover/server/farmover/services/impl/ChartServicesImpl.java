package com.farmover.server.farmover.services.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.Production;
import com.farmover.server.farmover.entities.Storage;
import com.farmover.server.farmover.entities.TransactionType;
import com.farmover.server.farmover.entities.Transactions;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.entities.Warehouse;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.repositories.ProductionRepo;
import com.farmover.server.farmover.repositories.UserRepo;
import com.farmover.server.farmover.repositories.WareHouseRepo;

@Service
public class ChartServicesImpl {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductionRepo productionRepo;

    @Autowired
    private WareHouseRepo warehouseRepo;

    private final String[] MONTHS = { "January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December" };

    public Map<String, Double> getExpenses(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        List<Transactions> userTransactions = user.getTransactions();

        Map<String, Double> monthWiseExpenses = new HashMap<>();

        userTransactions.forEach(transaction -> {

            if (transaction.getTransactionType().equals(TransactionType.DEBIT)) {
                Date date = transaction.getDate();
                int month = date.toLocalDate().getMonthValue();
                String monthName = MONTHS[month - 1];
                Double amount = transaction.getAmount();

                monthWiseExpenses.merge(monthName, amount, Double::sum);
            }
        });

        return monthWiseExpenses;
    }

    public Map<String, Double> getRevenue(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        List<Transactions> userTransactions = user.getTransactions();

        Map<String, Double> monthWiseIncome = new HashMap<>();

        userTransactions.forEach(transaction -> {

            if (transaction.getTransactionType().equals(TransactionType.CREDIT)) {
                Date date = transaction.getDate();
                int month = date.toLocalDate().getMonthValue();
                String monthName = MONTHS[month - 1];
                Double amount = transaction.getAmount();

                monthWiseIncome.merge(monthName, amount, Double::sum);
            }
        });

        return monthWiseIncome;
    }

    public Map<String, Long> getMonthlyProductionTally(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        List<Production> userProductions = productionRepo.findByFarmer(user)
                .orElseThrow(() -> new ResourceNotFoundException("Production", "farmer", user.getEmail()));

        Map<String, Long> monthlyProductionTally = new HashMap<>();

        userProductions.forEach(production -> {
            // Assuming Production has a getDate() method returning a Date object
            int month = production.getDate().getMonthValue();
            String monthName = MONTHS[month - 1];
            Long quantity = production.getQuantity();

            monthlyProductionTally.merge(monthName, quantity, Long::sum);
        });

        return monthlyProductionTally;
    }

    public Map<String, Double> getWarehouseUsageChart(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Warehouse warehouse = warehouseRepo.findByOwner(user);

        Map<String, Double> usageMap = new HashMap<>();

        YearMonth currentMonth = YearMonth.now();

        warehouse.getStorages().forEach(storage -> {
            storage.getStorageBookings().forEach(booking -> {
                if (booking.getBookingDate().getMonth().equals(currentMonth.getMonth())) {
                    usageMap.merge(storage.getStorageType().toString(), booking.getBookedWeight(), Double::sum);
                }
            });
        });

        return usageMap;
    }

    public Map<String, Double> getWarehouseRevenueFromBookings(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Warehouse warehouse = warehouseRepo.findByOwner(user);

        Map<String, Double> revenueMap = new HashMap<>();

        YearMonth currentMonth = YearMonth.now();

        warehouse.getStorages().forEach(storage -> {
            storage.getStorageBookings().forEach(booking -> {
                if (booking.getBookingDate().getMonth().equals(currentMonth.getMonth())) {
                    revenueMap.merge(storage.getStorageType().toString(), booking.getBookedPrice(),
                            Double::sum);
                }
            });
        });

        return revenueMap;
    }

    public Map<String, Double> getWarehouseRevenueFromSales(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Warehouse warehouse = warehouseRepo.findByOwner(user);

        Map<String, Double> revenueMap = new HashMap<>();

        YearMonth currentMonth = YearMonth.now();

        warehouse.getWarehouseSales().forEach(sale -> {
            if (sale.getDate().toLocalDate().getMonth().equals(currentMonth.getMonth())) {
                revenueMap.merge(sale.getStorageType().toString(), sale.getPrice(), Double::sum);
            }
        });

        return revenueMap;
    }
}
