package simulations.advanced

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class AuthenticationTest extends Simulation {
  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://gorest.co.in/public/v2")
//   This is the place where we feed the Authorization Token
//   User your own Token as I will regenerate this token after this lecture.
//   Add Authorization here if you want to use it for all API's
//  .header("Authorization", "Bearer b852c5fd133aa4a95e3e5cc40f257dcd2303cd7a8a25211b7e1da9f9d44c684a")

  def getAllStudentDetails(): ChainBuilder = {
    exec(http("get all students")
      .get("/users")
      .header("accept", "application/json").asJson
      .header("content-type", "application/json").asJson
      // Authorization Header
      .header("Authorization", "Bearer b852c5fd133aa4a95e3e5cc40f257dcd2303cd7a8a25211b7e1da9f9d44c684a")
      .check(
        status.is(200),
        jsonPath("$[0].id").saveAs("userId"),
        jsonPath("$[0].name").saveAs("userName")
      ))
  }

  def getSingleStudentDetails(): ChainBuilder = {
    exec(http("get single students")
      .get("/users/#{userId}")
      .header("accept", "application/json").asJson
      .header("content-type", "application/json").asJson
      // Authorization Header
      .header("Authorization", "Bearer b852c5fd133aa4a95e3e5cc40f257dcd2303cd7a8a25211b7e1da9f9d44c684a")
      .check(
        status.is(200),
        jsonPath("$.id").is("#{userId}"),
        jsonPath("$.name").saveAs("#{userName}")
      ))
  }

  val createVideoGameScenario: ScenarioBuilder = scenario("Get All Students")
    .exec(getAllStudentDetails())
    .pause(2)
    .exec(getSingleStudentDetails())

  setUp(createVideoGameScenario.inject(atOnceUsers(1)))
    .protocols(httpProtocol)
}
