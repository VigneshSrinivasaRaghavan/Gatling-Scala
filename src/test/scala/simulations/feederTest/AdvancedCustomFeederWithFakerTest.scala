package simulations.feederTest

import com.github.javafaker.Faker
import io.gatling.core.Predef.*
import io.gatling.core.feeder.{BatchableFeederBuilder, Feeder}
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef.*
import io.gatling.http.protocol.HttpProtocolBuilder

class AdvancedCustomFeederWithFakerTest extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://gorest.co.in/public/v2")

//  Create Object for Faker Class
  val faker = new Faker()

  val customFeeder: Iterator[Map[String, _]] = Iterator.continually(Map(
    "name" -> s"${faker.name().fullName()}",
    "email" -> s"${faker.internet().emailAddress()}",
    "gender" -> "male",
    "status" -> "active"
  ))

  def createSingleStudent(): ChainBuilder = {
    exec(http("Create Single Student")
      .post("/users")
      .header("accept", "application/json").asJson
      .header("content-type", "application/json").asJson
      .header("Authorization", "Bearer b852c5fd133aa4a95e3e5cc40f257dcd2303cd7a8a25211b7e1da9f9d44c684a")
      .body(StringBody(session =>
        s"""{
           |  "name": "${session("name").as[String]}",
           |  "email": "${session("email").as[String]}",
           |  "gender": "${session("gender").as[String]}",
           |  "status": "${session("status").as[String]}"
           |}""".stripMargin)).asJson
      .check(
        status.is(201),
        bodyString.saveAs("responseBody")
      ))
      .exec { session => println(session("responseBody").as[String]); session }
  }

  val createStudentScenario: ScenarioBuilder = scenario("Advanced Custom Feeder Test")
    .feed(customFeeder)
    .exec(createSingleStudent())

  setUp(createStudentScenario.inject(atOnceUsers(5)))
    .protocols(httpProtocol)

}
