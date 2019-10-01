package io.kirmit.transfer.http;

import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.halt;
import static spark.Spark.path;

import io.kirmit.transfer.account.service.SyncAccountService;
import io.kirmit.transfer.http.model.exception.NotFoundException;
import io.kirmit.transfer.http.route.AccountRoute;
import io.kirmit.transfer.http.route.TransferRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpApp.class);

    public static void main(String[] main) {
        SyncAccountService service = new SyncAccountService();
        AccountRoute accountRoute = new AccountRoute(service);
        TransferRoute transferRoute = new TransferRoute(service);

        path("/api/v1", () -> {
            before("/*", (q, a) -> LOGGER.info("Received api call"));
            accountRoute.route();
            transferRoute.route();
        });

        exception(IllegalArgumentException.class, (ex, req, res) -> res.status(400));
        exception(NotFoundException.class, (ex, req, res) -> {
            res.status(404);
            res.body(ex.getMessage());
        });
    }
}
