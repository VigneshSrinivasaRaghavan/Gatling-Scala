package simulations.injection

import io.gatling.core.Predef._
import io.gatling.core.feeder.{BatchableFeederBuilder, Feeder}
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class OpenInjectionWithConstantUser extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://reqres.in/api/users")

  //scenario
  val scn: ScenarioBuilder = scenario("get user")
    .exec(
      http("get single user")
        .get("/2")
        .check(status.is(200))
    )

  setUp(scn.inject(
    //    constantUsersPerSec(20).during(15),
    constantUsersPerSec(20).during(15).randomized
  ))
    .protocols(httpProtocol)
}
