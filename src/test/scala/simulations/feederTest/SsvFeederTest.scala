package simulations.feederTest

import io.gatling.core.Predef._
import io.gatling.core.feeder.{BatchableFeederBuilder, Feeder}
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class SsvFeederTest extends Simulation {
  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://gorest.co.in/public/v2")

  /*
  * Queue : use an Iterator on the underlying sequence
  * Random : Randomly pick an entry in the sequence
  * Shuffle: shuffle entries, then behave like queue
  * Circular: Go back to the top of the sequence once the end is reached
  */
  val ssvFeeder: Feeder[Any] = ssv("data/feeders/studentDetails.ssv").circular()

  def getSingleStudentDetails(): ChainBuilder = {
    repeat(3){
      feed(ssvFeeder)
        .exec(http("get single students with ID #{id}")
          .get(s"/users/#{id}")
          .header("accept", "application/json").asJson
          .header("content-type", "application/json").asJson
          // Authorization Header
          .header("Authorization", "Bearer b852c5fd133aa4a95e3e5cc40f257dcd2303cd7a8a25211b7e1da9f9d44c684a")
          .check(
            status.is(200),
            jsonPath("$.id").is("#{id}"),
            jsonPath("$.name").is("#{name}")
          ))
    }

  }

  val getStudentScenario: ScenarioBuilder = scenario("SSV Feeder Test")
    .exec(getSingleStudentDetails())

  setUp(getStudentScenario.inject(atOnceUsers(1)))
    .protocols(httpProtocol)
}
