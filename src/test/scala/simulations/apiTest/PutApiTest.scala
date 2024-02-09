package simulations.apiTest

import io.gatling.core.Predef.*
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.*
import io.gatling.http.protocol.HttpProtocolBuilder

class PutApiTest extends Simulation {

  //protocol
  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://reqres.in/api/users")

  //scenario
  val scn: ScenarioBuilder = scenario("update user")
    .exec(
      http("update user request")
        .put("/2")
        .header("content-type","application/json").asJson
        .body(RawFileBody("data/requestData/updateUser.json")).asJson
        .check(status.is(200))
    )

  //setup
  setUp(scn.inject(atOnceUsers(10)))
    .protocols(httpProtocol)
}