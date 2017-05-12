package io.skysail.um.httpbasic

import io.skysail.api.um.UserManagementProvider
import io.skysail.api.um.AuthenticationService
import io.skysail.api.um.AuthorizationService
import io.skysail.api.um.UserManagementRepository
import org.osgi.service.component.annotations._
import org.osgi.service.component._
import org.slf4j.LoggerFactory
import io.skysail.core.app.ApplicationProvider
import org.restlet.security.Verifier

@Component(
  immediate = false,
  configurationPolicy = ConfigurationPolicy.OPTIONAL,
  property = { Array("service.ranking:Integer=10") })
class HttpBasicUserManagementProvider extends UserManagementProvider {

  val log = LoggerFactory.getLogger(this.getClass())

  var authenticationService: AuthenticationService = null
  def getAuthenticationService(): AuthenticationService = authenticationService

  var authorizationService: AuthorizationService = null
  def getAuthorizationService(): AuthorizationService = authorizationService

  @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY, target = "(name=HttpBasicUmApplication)")
  @volatile var skysailApplication: ApplicationProvider = null
  def getSkysailApplication() = skysailApplication

  @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
  @volatile var userManagementRepository: io.skysail.api.um.UserManagementRepository = null
  def getUserManagementRepository() = userManagementRepository

 // @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.AT_LEAST_ONE)
 //@volatile var verifiers = Set[Verifier]()
 // def getVerifiers() = verifiers
  
  var verifier:Verifier = null
  def getVerifier() = verifier

  @Activate
  def activate() = {
    log.info(s"USER MANAGEMENT PROVIDER: activating provider '${this.getClass().getName()}'");
    try {
      authenticationService = new HttpBasicAuthenticationService(this);
      authorizationService = new HttpBasicAuthorizationService(this);
      verifier = new FilebasedVerifier(userManagementRepository)
    } catch {
      case e: Throwable => {
        log.error(e.getMessage(), e);
        log.error("===============================================");
        log.error("installation cannot run without usermanagement!");
        log.error("===============================================");
        throw e;
      }
    }
  }

  @Deactivate
  def deactivate() = {
    log.info(s"USER MANAGEMENT PROVIDER: deactivating provider '${this.getClass().getName()}'");
    authenticationService = null;
    authorizationService = null;
    verifier = null
  }

}