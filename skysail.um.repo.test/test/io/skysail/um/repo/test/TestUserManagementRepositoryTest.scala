package io.skysail.um.repo.test

import org.mockito.Mockito
import org.junit.Test

import org.junit.Assert._
import org.mockito.Mockito._
import org.hamcrest.CoreMatchers._
import org.restlet.data.Form
import org.restlet.util.Series
import org.junit.runner.RunWith
import org.junit._
import org.restlet.security.User

class TestUserManagementRepositoryTest {

  var repo: TestUserManagementRepository = null

  @Before
  def setUp() {
    repo = new TestUserManagementRepository
  }

  @Test
  def repo_returns_user_for_given_name() {
    val aUser = repo.getUser("theUser")
    assertThat(aUser.isPresent(), is(true))
    assertThat(aUser.get.getFirstName, is("firstname"))
    assertThat(aUser.get.getLastName, is("lastname"))
  }

  @Test
  def users_returned_for_identical_username_are_identical() {
    val aUser = repo.getUser("theUser")
    val aUser2 = repo.getUser("theUser")
    assertThat(aUser == aUser2, is(true))
  }
}