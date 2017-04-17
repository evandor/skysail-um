package io.skysail.um.shiro

import io.skysail.api.um.AuthorizationService
import org.restlet.security.Enroler
import org.restlet.data.ClientInfo
import org.apache.shiro.SecurityUtils
import org.apache.shiro.subject.Subject
import org.slf4j.LoggerFactory
import org.restlet.security.Role
import java.util.Collections
import scala.collection.JavaConverters._
import io.skysail.um.shiro.authorization.SimpleAuthorizingRealm
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher
import org.apache.shiro.realm.Realm

class SimpleAuthorizationService(provider: ShiroBasedUserManagementProvider) extends AuthorizationService with Enroler {

  val log = LoggerFactory.getLogger(this.getClass())
  
 // SkysailHashedCredentialsMatcher hashedCredetialsMatcher = new SkysailHashedCredentialsMatcher();
  //      hashedCredetialsMatcher.setCacheManager(simpleUserManagementProvider.getCacheManager());
  val hashedCredetialsMatcher = new SimpleCredentialsMatcher()
  val authorizingRealm = new SimpleAuthorizingRealm(hashedCredetialsMatcher, provider);

  def enrole(clientInfo: ClientInfo): Unit = {
    val subject = SecurityUtils.getSubject();
    if (subject == null) {
      return ;
    }
    // Find all the inherited groups of this user
    // Set<Group> userGroups = findGroups(user);

    // Add roles specific to this user
    val userRoles = findRoles(subject);

    for (role <- userRoles.asScala) {
      clientInfo.getRoles().add(role);
    }

    // Add roles common to group members
    // Set<Role> groupRoles = findRoles(userGroups);

    // for (Role role : groupRoles) {
    // clientInfo.getRoles().add(role);
    // }
  }

  override def getRolesFor(principal: String): java.util.Set[Role] = {
    if (principal == null) {
      return Collections.emptySet()
    }
    val user = provider.getByPrincipal(principal);
    val roles = user.getRoles
    if (roles == null) {
      log.warn("User '" + principal + "' could not be found in the Repository");
      return Collections.emptySet();
    }
    //return roles.map(this :: getOrCreateRole).collect(Collectors.toSet());
    Collections.emptySet()
  }

  private def findRoles(subject: Subject): java.util.Set[Role] = {
    getRolesFor(subject.getPrincipal().asInstanceOf[String])
  }

  def getEnroler(): Enroler = null
  
   def getRealm(): Realm = authorizingRealm
}