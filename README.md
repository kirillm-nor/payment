Brief overview
---

Thru current implementation I would like to check a few hypothesis and compare Akka HTTP with Spark HTTP.

- Plain synchronisation block
- Synchronisation at repository level
- Lock free transfer

In case of multiply transfers LockFree structure is faster.
Also about Akka HTTP and Spark comparision, Akka can hang more connection and accept more requests/second, 
however less stable and request processing time is also higher than Spark http.   