package simulations.apiTest

import io.gatling.core.Predef.*
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.*
import io.gatling.http.protocol.HttpProtocolBuilder

class PostApiTest extends Simulation {

  //protocol
  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://reqres.in/api")

  //scenario
  val scn: ScenarioBuilder = scenario("create user")
    .exec(
      http("create user request")
        .post("/users")
        .header("content-type","application/json").asJson
        .body(RawFileBody("data/requestData/createUser.json")).asJson
        .check(status.is(201))
    )

  //setup
  setUp(scn.inject(atOnceUsers(10)))
    .protocols(httpProtocol)
}