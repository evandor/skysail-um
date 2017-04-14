package io.skysail.um.httpbasic

import io.skysail.api.um.AuthenticationService
import org.restlet.Context
import io.skysail.api.um.AuthenticationMode
import org.restlet.security.Authenticator
import org.restlet.Request
import java.security.Principal
import org.restlet.Response
import io.skysail.restlet.utils.ScalaLinkUtils
import io.skysail.core.app.SkysailRootApplication
import io.skysail.um.httpbasic.app.HttpBasicUmApplication
import io.skysail.um.httpbasic.app.HttpBasicLoginPage
import org.restlet.security.ChallengeAuthenticator
import org.restlet.data.ChallengeScheme
import io.skysail.api.um.NeverAuthenticatedAuthenticator
import io.skysail.api.um.AlwaysAuthenticatedAuthenticator

class HttpBasicAuthenticationService(userManagementProvider: HttpBasicUserManagementProvider) extends AuthenticationService {

  def getApplicationAuthenticator(context: Context, authMode: AuthenticationMode) = {
    new Authenticator(context) { override protected def authenticate(request: Request, response: Response) = true }
  }

  def getLoginPath(): String = {
    try {
      val httpBasicLoginPageLink = ScalaLinkUtils.fromResource(
        userManagementProvider.getSkysailApplication().getSkysailApplication(), classOf[HttpBasicLoginPage]);
      return httpBasicLoginPageLink.getUri();
    } catch {
      case e: Throwable => return "/" + classOf[HttpBasicUmApplication].getSimpleName() + "/v1" + SkysailRootApplication.LOGIN_PATH;
    }
  }

  def getLogoutPath(): String = {
    ???
  }

  def getPrincipal(request: Request): Principal = {
    ???
  }

  def getResourceAuthenticator(context: Context, authMode: AuthenticationMode): Authenticator = {
    authMode match {
      case AuthenticationMode.DENY_ALL => return new NeverAuthenticatedAuthenticator(context)
      case AuthenticationMode.PERMIT_ALL => return new AlwaysAuthenticatedAuthenticator(context)
      case _ =>
    }

    val challengeAuthenticator = new ChallengeAuthenticator(context, ChallengeScheme.HTTP_BASIC,
      "Skysail Realm");
    challengeAuthenticator.setVerifier(userManagementProvider.getVerifiers().head)
    return challengeAuthenticator;
  }

  def isAuthenticated(request: Request): Boolean = {
    ???
  }
}