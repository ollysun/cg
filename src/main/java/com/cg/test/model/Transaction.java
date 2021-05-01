package com.cg.test.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;


public class Transaction implements Serializable {

    @NotEmpty(message = "Please provide the amount")
    private String amount;

    @NotNull(message = "Please provide the timestamp")
    @FutureOrPresent(message = "Please provide future or present date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "GMT+1")
    private Timestamp timestamp;

    public Transaction() {
    }

    public Transaction(String amount, Timestamp timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount='" + amount + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
