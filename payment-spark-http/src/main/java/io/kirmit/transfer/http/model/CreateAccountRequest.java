package io.kirmit.transfer.http.model;

import java.math.BigDecimal;

public class CreateAccountRequest {

    private BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
