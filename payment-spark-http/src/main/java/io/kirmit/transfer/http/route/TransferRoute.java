package io.kirmit.transfer.http.route;

import static spark.Spark.halt;
import static spark.Spark.post;

import com.google.gson.Gson;
import io.kirmit.transfer.account.service.SyncAccountService;
import io.kirmit.transfer.http.model.TransferRequest;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferRoute {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferRoute.class);

    private final SyncAccountService service;
    private final Gson mapper = new Gson();

    public TransferRoute(SyncAccountService service) {
        this.service = service;
    }

    public void route() {
        post("/transfer", "application/json", (req, res) -> {
            TransferRequest transferRequest = mapper.fromJson(req.body(), TransferRequest.class);
            if (transferRequest.getFrom().isBlank() || transferRequest.getTo().isBlank()
                || transferRequest.getAmount().signum() < 0) {
                halt(400, "Invalid arguments");
            }
            service.transferMoney(UUID.fromString(transferRequest.getFrom()), UUID.fromString(transferRequest.getTo()),
                transferRequest.getAmount());
            LOGGER.debug("Money transferred {}", transferRequest);
            res.status(204);
            return "OK";
        }, mapper::toJson);
    }
}
