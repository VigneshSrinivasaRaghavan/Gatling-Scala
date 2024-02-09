package simulations.apiTest

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class ReqResCrudTest extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://reqres.in/api")

  val getUserScenario: ScenarioBuilder = scenario("get user")
    .exec(
      http("get single user")
        .get("/users/2")
        .check(status.is(200))
    )

  val createUserScenario: ScenarioBuilder = scenario("create user")
    .exec(
      http("create user request")
        .post("/users")
        .header("content-type", "application/json").asJson
        .body(RawFileBody("data/requestData/createUser.json")).asJson
        .check(status.is(201))
    )

  val updateUserScenario: ScenarioBuilder = scenario("update user")
    .exec(
      http("update user request")
        .put("/users/2")
        .header("content-type", "application/json").asJson
        .body(RawFileBody("data/requestData/updateUser.json")).asJson
        .check(status.is(200))
    )

  val deleteUserScenario: ScenarioBuilder = scenario("delete user")
    .exec(
      http("delete user request")
        .delete("/2")
        .check(status.is(204))
    )

  setUp(getUserScenario.inject(atOnceUsers(10)),
    createUserScenario.inject(atOnceUsers(10)),
    updateUserScenario.inject(atOnceUsers(10)),
    deleteUserScenario.inject(atOnceUsers(10)))
    .protocols(httpProtocol)
}
