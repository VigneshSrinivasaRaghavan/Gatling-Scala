package simulations.advanced

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import simulations.utils.BaseTest

import scala.concurrent.duration.Duration

class repeatTest extends Simulation with BaseTest {

  val httpProtocol: HttpProtocolBuilder = http.baseUrl("https://reqres.in/api")

  val readCreateUpdateDeleteScenario: ScenarioBuilder = scenario("get user")
    .exec(getSingleUser(2,200))
    .pause(1)
    .exec(createUser("data/requestData/createUser.json",201,true,3))
    .pause(1)
    .exec(updateUser(2, "data/requestData/updateUser.json",200,true,3))
    .pause(1)
    .exec(deleteUser(2,204))

  setUp(readCreateUpdateDeleteScenario.inject(atOnceUsers(1)))
    .protocols(httpProtocol)
}
