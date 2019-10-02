package io.kirmit.transfer.http.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

public class TransferRequest {
    private UUID from;
    private UUID to;
    private BigDecimal amount;

    @JsonCreator
    public TransferRequest(@JsonProperty("from") UUID from, @JsonProperty("to") UUID to, @JsonProperty("amount") BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public UUID getFrom() {
        return from;
    }

    public void setFrom(UUID from) {
        this.from = from;
    }

    public UUID getTo() {
        return to;
    }

    public void setTo(UUID to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
