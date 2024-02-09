package simulations.basics

import io.gatling.core.Predef.*
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.*
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration.Duration

class AssertResponseStatusCode extends Simulation{

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://reqres.in/api")

  val readCreateUpdateDeleteScenario: ScenarioBuilder = scenario("get user")
    .exec(
      http("get single user")
        .get("/users/2")
        .check(status is 200)
    )
    .exec(
      http("create user request")
        .post("/users")
        .header("content-type", "application/json").asJson
        .body(RawFileBody("data/requestData/createUser.json")).asJson
        .check(status.in(200 to 205))
    )
    .exec(
      http("update user request")
        .put("/users/2")
        .header("content-type", "application/json").asJson
        .body(RawFileBody("data/requestData/updateUser.json")).asJson
        .check(status.not(404),status.is(200))
    )
    .exec(
      http("delete user request")
        .delete("/2")
        .check(status.is(204))
    )

  setUp(readCreateUpdateDeleteScenario.inject(atOnceUsers(10)))
    .protocols(httpProtocol)
}
