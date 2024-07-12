package com.farmover.server.farmover.services.impl;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmover.server.farmover.entities.Production;
import com.farmover.server.farmover.entities.TransactionType;
import com.farmover.server.farmover.entities.Transactions;
import com.farmover.server.farmover.entities.User;
import com.farmover.server.farmover.exceptions.ResourceNotFoundException;
import com.farmover.server.farmover.repositories.ProductionRepo;
import com.farmover.server.farmover.repositories.UserRepo;

@Service
public class ChartServicesImpl {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductionRepo productionRepo;

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
}
