package simulations.basics

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration.Duration

class ExtractResponseBody extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://reqres.in/api")

  val extractResponseDataScenario: ScenarioBuilder = scenario("Extract Response Data")
    .exec(
      http("get all users")
        .get("/users?page=2")
        .check(
          status is 200,
          jsonPath("$.data[1].id").saveAs("userId"),
          jsonPath("$.data[1].first_name").saveAs("userFirstName"),
          jsonPath("$.data[1].last_name").saveAs("userLastName")
        )
    )
    .pause(1)
    .exec(
      http("get single user")
        .get("/users/#{userId}")
        .check(
          status is 200,
          jsonPath("$.data.id").is("#{userId}"),
          jsonPath("$.data.first_name").is("#{userFirstName}"),
          jsonPath("$.data.last_name").is("#{userLastName}")
        )
    )
  setUp(extractResponseDataScenario.inject(atOnceUsers(1)))
    .protocols(httpProtocol)
}
