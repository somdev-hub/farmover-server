package com.farmover.server.farmover.payloads;

import java.sql.Date;

import com.farmover.server.farmover.entities.TransactionType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionsDto {

    private Integer id;

    private Double amount;

    private String buyer;

    private String seller;

    private String item;

    private Date date;

    private String type;

    private TransactionType transactionType;

}
