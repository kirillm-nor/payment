package io.kirmit.transfer.http.route;

import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;

import com.google.gson.Gson;
import io.kirmit.transfer.account.model.Account;
import io.kirmit.transfer.account.service.SyncAccountService;
import io.kirmit.transfer.http.model.CreateAccountRequest;
import io.kirmit.transfer.http.model.exception.NotFoundException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountRoute {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRoute.class);

    private final SyncAccountService service;
    private final Gson mapper = new Gson();

    public AccountRoute(SyncAccountService service) {
        this.service = service;
    }

    public void route() {
        get("/account/:id",(req, res) -> {
            String id = req.params("id");
            if (id.isBlank()) {
                halt(400, "Invalid id");
            }
            Account account = service.findAccount(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException("Account not found"));
            LOGGER.debug("Found account {}", account);
            res.type("application/json");
            return account;
        }, mapper::toJson);
        post("/account", "application/json", (req, res) -> {
            CreateAccountRequest createAccount = mapper.fromJson(req.body(), CreateAccountRequest.class);
            if (createAccount.getBalance().signum() < 0) {
                halt(400, "Invalid balance");
            }
            Account account = new Account(UUID.randomUUID(), createAccount.getBalance());
            service.addAccount(account);
            LOGGER.debug("Account created {}", account);
            res.status(201);
            res.type("application/json");
            return account;
        }, mapper::toJson);
    }
}
