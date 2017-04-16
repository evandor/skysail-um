package io.skysail.um.repo.test

import collection.mutable.Stack
import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner]) 
class TestUserManagementRepository2Spec extends FlatSpec with BeforeAndAfterEach {

  var repo: TestUserManagementRepository = null
  
  override def beforeEach() {
    repo = new TestUserManagementRepository
  }

  "The TestUserManagementRepository" should "return a valid user for any non-empty username" in {
    val aUser = repo.getUser("theUser")
    assert(aUser.isPresent() === true)
    assert(aUser.get.getFirstName === "firstname")
    assert(aUser.get.getLastName === "lastname")
  }

  it should "return identical user objects for the same username" in {
    assert(repo.getUser("theUser").get === repo.getUser("theUser").get)
  }
  
  it should "contain a user which was created using her username in the users list" in {
    val aUser = repo.getUser("aNewUser")
    assert(repo.users.contains("aNewUser"))
    assert(repo.users.get("aNewUser").get === aUser.get)
  }

}