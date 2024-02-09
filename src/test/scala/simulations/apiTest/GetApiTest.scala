package simulations.apiTest

import io.gatling.core.Predef.*
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.*
import io.gatling.http.protocol.HttpProtocolBuilder

class GetApiTest extends Simulation {

  //protocol
  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://reqres.in/api/users")

  //scenario
  val scn: ScenarioBuilder = scenario("get user")
    .exec(
      http("get single user")
        .get("/2")
        .check(status.is(200))
    )


  //setup
  setUp(scn.inject(atOnceUsers(10)))
    .protocols(httpProtocol)
}