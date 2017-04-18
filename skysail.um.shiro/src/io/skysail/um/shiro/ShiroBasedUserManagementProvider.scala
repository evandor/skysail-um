package io.skysail.um.shiro

import io.skysail.api.um.UserManagementProvider
import org.osgi.service.component.annotations._
import org.slf4j.LoggerFactory
import io.skysail.api.um.AuthenticationService
import io.skysail.api.um.AuthorizationService
import io.skysail.restlet.app.ApplicationProvider
import org.apache.shiro.SecurityUtils
import io.skysail.core.um.domain.SkysailUser
import io.skysail.um.shiro.web.impl.SkysailWebSecurityManager

@Component(
  immediate = false,
  property = { Array("service.ranking:Integer=100") })
class ShiroBasedUserManagementProvider extends UserManagementProvider {

  val log = LoggerFactory.getLogger(this.getClass())

  var authenticationService: AuthenticationService = null
  def getAuthenticationService(): AuthenticationService = authenticationService

  var authorizationService: ShiroAuthorizationService = null
  def getAuthorizationService(): AuthorizationService = authorizationService

  @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, target = "(name=ShiroUmApplication)")
  @volatile var skysailApplication: ApplicationProvider = null
  def getSkysailApplication() = skysailApplication

  @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
  @volatile var userManagementRepository: io.skysail.api.um.UserManagementRepository = null
  def getUserManagementRepository() = userManagementRepository

  @Activate
  def activate() = {
    log.info(s"USER MANAGEMENT PROVIDER: activating provider '${this.getClass().getName()}'");
    //cacheManager = new MemoryConstrainedCacheManager();
    authenticationService = new ShiroAuthenticationService(this);
    authorizationService = new ShiroAuthorizationService(this);
    SecurityUtils.setSecurityManager(new SkysailWebSecurityManager(authorizationService.getRealm()));
  }

  @Deactivate
  def deactivate() = {
    log.info(s"USER MANAGEMENT PROVIDER: deactivating provider '${this.getClass().getName()}'");
    authenticationService = null;
    authorizationService = null;
    SecurityUtils.setSecurityManager(null);
    //cacheManager = null;
  }

  def getByUsername(username: String): SkysailUser = {
    val user = userManagementRepository.getUser(username);
    if (user.isPresent()) {
      return new SkysailUser(user.get().getIdentifier(), new String(user.get().getSecret()), user.get().getIdentifier());
    } else {
      log.info("user {} was not found in the user repository", username);
      return null;
    }
  }

  def getByPrincipal(username: String): SkysailUser = {
    return getByUsername(username);
  }

}