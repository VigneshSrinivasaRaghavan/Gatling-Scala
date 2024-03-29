package simulations.basics

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration.Duration

class ClearCacheAndCookie extends Simulation{

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://reqres.in/api")

  val readCreateUpdateDeleteScenario: ScenarioBuilder = scenario("get user")
    .exec(
      http("get single user")
        .get("/users/2")
        .check(status is 200)
        .check(jsonPath("$.data.first_name").is("Janet"))
    )

    .exec(flushHttpCache)
    .exec(flushCookieJar)
    .exec(
      http("create user request")
        .post("/users")
        .header("content-type", "application/json").asJson
        .body(RawFileBody("data/requestData/createUser.json")).asJson
        //We can club multiple check in to one
        .check(
          status.in(200 to 205),
          jsonPath("$.name").is("morpheus")
        )
    )

    // Clear cookies and cache with single exec
    .exec(flushHttpCache)
    .exec(flushCookieJar)
    .exec(
      http("update user request")
        .put("/users/2")
        .header("content-type", "application/json").asJson
        .body(RawFileBody("data/requestData/updateUser.json")).asJson
        .check(
          status.not(404),status.is(200),
          jsonPath("$.job").is("zion resident")
        )
    )

    .exec(flushHttpCache)
    .exec(flushCookieJar)
    .exec(
      http("delete user request")
        .delete("/2")
        .check(status.is(204))
    )

  setUp(readCreateUpdateDeleteScenario.inject(atOnceUsers(4))
    .protocols(httpProtocol))
}
