Brief overview
---

Thru current implementation I would like to check a few hypothesis and compare Akka HTTP with Spark HTTP.

- Plain synchronisation block
- Synchronisation at repository level
- Lock free transfer

In case of multiply transfers LockFree structure is faster.
Also about Akka HTTP and Spark comparision, Akka can hang more connection and accept more requests/second, 
however less stable and request processing time is also higher than Spark http.

You can build target fat jar from modules `payment-akka-http` or `payment-spark-http` by `gradle shadowJar`
To run load test, move to the `load-test` module and use one of the following commands:
 - `gradle jmh`
 - `gradle jcstress`
 - `gradle gatlingRun` to use gatling needs to run web application first