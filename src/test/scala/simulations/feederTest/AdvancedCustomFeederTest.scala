package simulations.feederTest

import io.gatling.core.Predef.*
import io.gatling.core.feeder.{BatchableFeederBuilder, Feeder}
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef.*
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.util.Random

class AdvancedCustomFeederTest extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://gorest.co.in/public/v2")

  val random = new Random()

  def randomString(length: Int): String = {
    random.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  //  Use below methods incase your applications needs to feed some number or Date
  //
  //  def randomNumber(length: Int): Int = {
  //    random.nextInt(math.pow(10, length).toInt)
  //  }
  //
  //  def randomDate(): String = {
  //    LocalDate.now().minusDays(random.nextInt(30)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
  //  }

  val customFeeder: Iterator[Map[String, _]] = Iterator.continually(Map(
    "name" -> s"${randomString(7)}",
    "email" -> s"${randomString(3)}@gmail.com",
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
