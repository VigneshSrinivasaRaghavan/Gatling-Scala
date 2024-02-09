package simulations.basics

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration.Duration

class AddPauseTest extends Simulation{

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://reqres.in/api")

  val readCreateUpdateDeleteScenario: ScenarioBuilder = scenario("get user")
    .exec(
      http("get single user")
        .get("/users/2")
        .check(status.is(200))
    )
    // Fixed Pause
    .pause(1)

    .exec(
      http("create user request")
        .post("/users")
        .header("content-type", "application/json").asJson
        .body(RawFileBody("data/requestData/createUser.json")).asJson
        .check(status.is(201))
    )
    // Random Pause
    .pause(1,3)

    // Setting a Static Pause Duration in the Session:
    // This line sets a static pause duration of 2 seconds in the Gatling session.
    .exec(session => session.set("pause", 2))
    .exec(
      http("update user request")
        .put("/users/2")
        .header("content-type", "application/json").asJson
        .body(RawFileBody("data/requestData/updateUser.json")).asJson
        .check(status.is(200))
    )
    // Dynamic Pause Duration Using Gatling Expression Language (EL):
    .pause("#{pause}")

    .exec(
      http("delete user request")
        .delete("/2")
        .check(status.is(204))
    )

  setUp(readCreateUpdateDeleteScenario.inject(atOnceUsers(10)))
    .protocols(httpProtocol)
}
