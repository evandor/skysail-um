package io.skysail.um.shiro

import io.skysail.api.um._
import io.skysail.restlet.utils.ScalaLinkUtils
import io.skysail.core.app.resources.LoginResource
import io.skysail.um.shiro.authentication.SkysailCookieAuthenticator
import io.skysail.um.shiro.app.resources.ShiroLoginResource
import java.security.Principal
import org.restlet._
import org.restlet.security.Authenticator
import org.apache.shiro.SecurityUtils

class ShiroAuthenticationService(provider: ShiroBasedUserManagementProvider) extends AuthenticationService {

  def getApplicationAuthenticator(context: Context, authMode: AuthenticationMode): Authenticator = getResourceAuthenticator(context, authMode)

  def getLoginPath(): String = {
    val loginLink = ScalaLinkUtils.fromResource(provider.getSkysailApplication().getSkysailApplication(),
      classOf[ShiroLoginResource])
    return loginLink.getUri()
  }

  def getLogoutPath(): String = null

  def getPrincipal(request: Request): Principal = {
    val principal = SecurityUtils.getSubject().getPrincipal()
    new Principal() { override def getName() = if (principal != null) principal.toString() else "anonymous" }
  }

  def getResourceAuthenticator(context: Context, authMode: AuthenticationMode): Authenticator = {
    authMode match {
      case AuthenticationMode.DENY_ALL => return new NeverAuthenticatedAuthenticator(context)
      case AuthenticationMode.PERMIT_ALL => return new AlwaysAuthenticatedAuthenticator(context)
      case _ =>
    }

    //        CacheManager cacheManager = null
    //        if (provider != null) {
    //            cacheManager = this.provider.getCacheManager()
    //        } else {
    //            log.info("no cacheManager available in {}", this.getClass().getName())
    //        }
    val allowAnonymous = if (authMode.equals(AuthenticationMode.ANONYMOUS)) true else false

    // https://github.com/qwerky/DataVault/blob/master/src/qwerky/tools/datavault/DataVault.java
    new SkysailCookieAuthenticator(context, "SKYSAIL_SHIRO_DB_REALM", "thisHasToBecomeM".getBytes(), allowAnonymous,
      /*cacheManager*/ null)
  }

  def isAuthenticated(request: Request): Boolean = SecurityUtils.getSubject().isAuthenticated()

}