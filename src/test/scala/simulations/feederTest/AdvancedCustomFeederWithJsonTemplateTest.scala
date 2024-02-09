package simulations.feederTest

import com.github.javafaker.Faker
import io.gatling.core.Predef.*
import io.gatling.core.feeder.{BatchableFeederBuilder, Feeder}
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef.*
import io.gatling.http.protocol.HttpProtocolBuilder

class AdvancedCustomFeederWithJsonTemplateTest extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://gorest.co.in/public/v2")
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
      // CALLING THE JSON TEMPLATE FILE
      .body(ElFileBody("data/requestTemplate/newStudentRequestBodyTemplate.json")).asJson
      .check(
        status.is(201),
        bodyString.saveAs("responseBody")
      ))
      .exec { session => println(session("responseBody").as[String]); session }
  }

  val createStudentScenario: ScenarioBuilder = scenario("JSON Template Custom Feeder Test")
    .feed(customFeeder)
    .exec(createSingleStudent())

  setUp(createStudentScenario.inject(atOnceUsers(5)))
    .protocols(httpProtocol)

}
