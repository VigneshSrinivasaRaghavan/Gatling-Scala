package simulations.feederTest

import io.gatling.core.Predef.*
import io.gatling.core.feeder.{BatchableFeederBuilder, Feeder}
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef.*
import io.gatling.http.protocol.HttpProtocolBuilder

class CustomFeeder extends Simulation {
  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://gorest.co.in/public/v2")

  val idNumbers: Iterator[Int] = (6221791 to 6221794).iterator

  val customFeeder: Iterator[Map[String, Int]] = Iterator.continually(Map("id" -> idNumbers.next()))

  def getSingleStudentDetails(): ChainBuilder = {
    repeat(4){
      feed(customFeeder)
        .exec(http("get single students with ID #{id}")
          .get(s"/users/#{id}")
          .header("accept", "application/json").asJson
          .header("content-type", "application/json").asJson
          // Authorization Header
          .header("Authorization", "Bearer b852c5fd133aa4a95e3e5cc40f257dcd2303cd7a8a25211b7e1da9f9d44c684a")
          .check(
            status.is(200),
            jsonPath("$.id").is("#{id}"),
          ))
    }

  }

  val getStudentScenario: ScenarioBuilder = scenario("Custom Feeder Test")
    .exec(getSingleStudentDetails())

  setUp(getStudentScenario.inject(atOnceUsers(1)))
    .protocols(httpProtocol)
}
