package io.kirmit.transfer.http;

import static akka.http.javadsl.server.PathMatchers.segment;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.JavaPathMatchers;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import io.kirmit.transfer.account.service.SyncAccountService;
import io.kirmit.transfer.http.route.AccountRoute;
import io.kirmit.transfer.http.route.TransferRoute;
import java.util.concurrent.CompletionStage;


public class HttpApp extends AllDirectives {

    private final AccountRoute accountRoute;
    private final TransferRoute transferRoute;

    private HttpApp(AccountRoute accountRoute, TransferRoute transferRoute) {
        this.accountRoute = accountRoute;
        this.transferRoute = transferRoute;
    }

    public static void main(String[] args) throws Exception {

        final ActorSystem system = ActorSystem.create("routes");
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        SyncAccountService service = new SyncAccountService();
        AccountRoute accountRoute = new AccountRoute(service);
        TransferRoute transferRoute = new TransferRoute(service);
        HttpApp httpApp = new HttpApp(accountRoute, transferRoute);

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = httpApp.createRoute().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
            ConnectHttp.toHost("localhost", 4567), materializer);

        system.getWhenTerminated().whenComplete((t, i) -> System.out.println("System terminated"));
    }

    private Route createRoute() {
        return pathPrefix(segment("api").slash(segment("v1")), () ->
            concat(
                accountRoute.route(),
                transferRoute.route()
            )
        );
    }
}
