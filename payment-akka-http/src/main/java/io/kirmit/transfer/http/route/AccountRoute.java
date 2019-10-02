package io.kirmit.transfer.http.route;

import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import io.kirmit.transfer.account.model.Account;
import io.kirmit.transfer.account.service.SyncAccountService;
import io.kirmit.transfer.http.model.CreateAccountRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static akka.http.javadsl.server.PathMatchers.uuidSegment;

public class AccountRoute extends AllDirectives {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRoute.class);

    private final SyncAccountService service;

    public AccountRoute(SyncAccountService service) {
        this.service = service;
    }

    public Route route() {
        return path("account",
                () ->
                        concat(
                                get(() ->
                                        path(uuidSegment(), (UUID id) -> {
                                            final CompletableFuture<Optional<Account>> cfAccount = CompletableFuture.supplyAsync(() ->
                                                    service.findAccount(id));
                                            return onSuccess(cfAccount, account -> account.map(item -> completeOK(item, Jackson.marshaller()))
                                                    .orElseGet(() -> complete(StatusCodes.NOT_FOUND, "Not Found")));
                                        })
                                ),
                                post(() ->
                                        entity(Jackson.unmarshaller(CreateAccountRequest.class), account -> {
                                            CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> {
                                                Account acc = new Account(UUID.randomUUID(), account.getBalance());
                                                service.addAccount(acc);
                                                LOGGER.debug("Account created {}", acc);
                                            });
                                            return onSuccess(cf, v -> complete(StatusCodes.CREATED));
                                        })
                                )
                        )
        );
    }

}
