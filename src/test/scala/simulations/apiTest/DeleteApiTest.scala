package simulations.apiTest

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class DeleteApiTest extends Simulation {

  //protocol
  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://reqres.in/api/users")

  //scenario
  val scn: ScenarioBuilder = scenario("delete user")
    .exec(
      http("delete user request")
        .delete("/2")
        .check(status.is(204))
    )

  //setup
  setUp(scn.inject(atOnceUsers(10)))
    .protocols(httpProtocol)
}