package simulations.advanced

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration.Duration

class ReuseMethods extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://reqres.in/api")

  def getSingleUser(userId:Int): ChainBuilder = {
    exec(http("get single user")
      .get(s"/users/$userId")
      .check(status is 200))
  }

  def createUser(): ChainBuilder = {
    exec(http("create user request")
      .post("/users")
      .header("content-type", "application/json").asJson
      .body(RawFileBody("data/requestData/createUser.json")).asJson
      .check(status.in(200 to 205)))
  }

  def updateUser(): ChainBuilder = {
    exec(http("update user request")
      .put("/users/2")
      .header("content-type", "application/json").asJson
      .body(RawFileBody("data/requestData/updateUser.json")).asJson
      .check(status.not(404), status.is(200))
    )
  }

  def deleteUser(userId:Int): ChainBuilder = {
    exec(http("delete user request")
      .delete(s"/$userId")
      .check(status.is(204)))
  }

  val readCreateUpdateDeleteScenario: ScenarioBuilder = scenario("get user")
    .exec(getSingleUser(2))
    .pause(1)
    .exec(createUser())
    .pause(1)
    .exec(updateUser())
    .pause(1)
    .exec(deleteUser(2))

  setUp(readCreateUpdateDeleteScenario.inject(atOnceUsers(1)))
    .protocols(httpProtocol)
}
