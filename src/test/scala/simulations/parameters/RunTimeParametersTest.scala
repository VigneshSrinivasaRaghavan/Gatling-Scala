package simulations.parameters

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class RunTimeParametersTest extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://reqres.in/api/users")

  def userMin: Int = System.getProperty("userMin", "10").toInt

  def userMax: Int = System.getProperty("userMax", "20").toInt

  def duration: Int = System.getProperty("duration", "10").toInt

  before {
    println(s"User Count Minimum = ${userMin}")
    println(s"User Count Maximum = ${userMax}")
    println(s"Duration = ${duration} seconds")
  }

  //scenario
  val scn: ScenarioBuilder = scenario("get user")
    .exec(
      http("get single user")
        .get("/2")
        .check(status.is(200))
    )

  setUp(scn.inject(
    rampConcurrentUsers(userMin).to(userMax).during(duration)
  ))
    .protocols(httpProtocol)
}
