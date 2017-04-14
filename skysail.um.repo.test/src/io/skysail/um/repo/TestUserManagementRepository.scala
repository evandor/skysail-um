package io.skysail.um.repo

import io.skysail.api.um.UserManagementRepository
import org.osgi.service.component.annotations.Component
import java.util.Optional
import org.restlet.security.User
import org.restlet.security.Role
import org.slf4j.LoggerFactory
import scala.collection.JavaConverters._

@Component(immediate = true)
class TestUserManagementRepository extends UserManagementRepository {

  var log = LoggerFactory.getLogger(this.getClass())

  val users = scala.collection.mutable.Map[String, User]()
  def getUsers() = users.asJava
  
  val roles = scala.collection.mutable.Set[Role]()
  def getRoles() = roles.asJava
  
  val usersRoles = scala.collection.mutable.Map[User, java.util.Set[Role]]()
  def getUsersRoles = usersRoles.asJava

  def getUser(username: String): Optional[User] = {
    if (users.get(username) == null) {
      log.warn("running fixed UserManagement Repository, i.e. creating dummy user for username '{}'", username)
      users += username -> new User(username, username.toCharArray(), "firstname", "lastname", "some@email.io")
    }
    var fixedRoles = new java.util.HashSet[Role]()

    val userRole = new Role("user");
    val roleNamedLikeUsername = new Role(username);

    roles.add(userRole);
    roles.add(roleNamedLikeUsername);

    fixedRoles.add(userRole);
    fixedRoles.add(roleNamedLikeUsername);

    usersRoles.put(users.get(username).get, fixedRoles);

    return Optional.of(users.get(username).get);
  }

}