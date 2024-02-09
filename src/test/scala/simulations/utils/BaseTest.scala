package simulations.utils

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

trait BaseTest {
  private def buildRequest(request: HttpRequestBuilder, needRepeating: Boolean, repeaterCount: Int): ChainBuilder = {
    if (needRepeating) {
      // In Gatling's repeat block, using a counter name is necessary to access the loop index.
      // The counterName parameter, "repeaterIndex" in this case, enables referencing the current iteration's index.
      // Without the counter name, repeat block alone does not directly return a ChainBuilder.
      // Adding repeaterIndex allows using the loop index inside the exec block, making it work correctly in Gatling DSL.
      repeat(repeaterCount, "repeaterIndex") {
        exec(request)
      }
    } else {
      exec(request)
    }
  }

  def getSingleUser(userId: Int, statusCode: Int, needRepeating: Boolean = false, repeaterCount: Int = 0): ChainBuilder = {
    val request = http("get single user")
      .get(s"/users/$userId")
      .check(status.is(statusCode))

    buildRequest(request, needRepeating, repeaterCount)
  }

  def createUser(requestJsonPath: String, statusCode: Int, needRepeating: Boolean = false, repeaterCount: Int = 0): ChainBuilder = {
    val request = http("create user request")
      .post("/users")
      .header("content-type", "application/json").asJson
      .body(RawFileBody(requestJsonPath)).asJson
      .check(status.is(statusCode))

    buildRequest(request, needRepeating, repeaterCount)
  }

  def updateUser(userId: Int, requestJsonPath: String, statusCode: Int, needRepeating: Boolean = false, repeaterCount: Int = 0): ChainBuilder = {
    val request = http("update user request")
      .put(s"/users/$userId")
      .header("content-type", "application/json").asJson
      .body(RawFileBody(requestJsonPath)).asJson
      .check(status.is(statusCode))

    buildRequest(request, needRepeating, repeaterCount)
  }

  def deleteUser(userId: Int, statusCode: Int, needRepeating: Boolean = false, repeaterCount: Int = 0): ChainBuilder = {
    val request = http("delete user request")
      .delete(s"/$userId")
      .check(status.is(statusCode))

    buildRequest(request, needRepeating, repeaterCount)
  }
}
