package io.kirmit.transfer.http.route;

import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import io.kirmit.transfer.account.service.SyncAccountService;
import io.kirmit.transfer.http.model.TransferRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class TransferRoute extends AllDirectives {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferRoute.class);

    private final SyncAccountService service;

    public TransferRoute(SyncAccountService service) {
        this.service = service;
    }

    public Route route() {
        return path("transfer", () ->
                post(() ->
                        entity(Jackson.unmarshaller(TransferRequest.class), transfer -> {
                            if (transfer.getFrom() == null || transfer.getTo() == null
                                    || transfer.getAmount().signum() < 0) {
                                return complete(StatusCodes.BAD_REQUEST, "Invalid arguments");
                            }
                            CompletableFuture<Void> cf = CompletableFuture.runAsync(() ->
                                    service.transferMoney(transfer.getFrom(), transfer.getTo(), transfer.getAmount()));
                            return onSuccess(cf, v -> complete(StatusCodes.OK));
                        })
                )
        );
    }
}
