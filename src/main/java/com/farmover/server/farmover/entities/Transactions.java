package com.farmover.server.farmover.entities;

import java.sql.Date;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Nonnull
    private Double amount;

    @Nonnull
    private String buyer;

    @Nonnull
    private String seller;

    private String item;

    @Nonnull
    private Date date;

    private String type;

    @Enumerated(EnumType.STRING)
    @Nonnull
    private TransactionType transactionType;

    @ManyToOne
    private User user;
}
