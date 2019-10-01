package io.kirmit.transfer.account

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class CreateUserSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://127.0.0.1:4567/api/v1")
    .acceptHeader("text/html,application/xhtml+xml,application/json;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn = scenario("Scenario Name")
    .exec(
      http("create_account_to")
        .post("/account")
        .body(StringBody("""{ "balance": 10 }""")).asJson
        .check(status.is(201))
        .check(jsonPath("$.id").exists.saveAs("to"))
    )
    .pause(5)
    .exec(
      http("create_account_from")
        .post("/account")
        .body(StringBody("""{ "balance": 10 }""")).asJson
        .check(status.is(201))
        .check(jsonPath("$.id").exists.saveAs("from"))
    )
    .pause(5)
    .exec(
      http("transfer")
        .post("/transfer")
        .body(StringBody("""{"from": "${from}","to": "${to}","amount": 1}""")).asJson
        .check(status.is(204))
    )
    .pause(5)
    .exec(
      http("get_account_from")
        .get("/account/${from}")
        .check(status.is(200))
        .check(jsonPath("$.balance").ofType[Int].is(9))
    )

  setUp(
    scn
      .inject(
        atOnceUsers(10),
        constantUsersPerSec(20) during (15 seconds),
        rampUsers(100) during (5 seconds),
        constantUsersPerSec(200) during (30 seconds)
      )
      .exponentialPauses
      .protocols(httpProtocol))
}
