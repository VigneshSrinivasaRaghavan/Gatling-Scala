package simulations.injection

import io.gatling.core.Predef.*
import io.gatling.core.feeder.{BatchableFeederBuilder, Feeder}
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef.*
import io.gatling.http.protocol.HttpProtocolBuilder

class ClosedInjectionTest extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://reqres.in/api/users")

  //scenario
  val scn: ScenarioBuilder = scenario("get user")
    .exec(
      http("get single user")
        .get("/2")
        .check(status.is(200))
    )

  setUp(scn.inject(
//    constantConcurrentUsers(10).during(10),
    rampConcurrentUsers(10).to(20).during(10)
  ))
    .protocols(httpProtocol)
}
