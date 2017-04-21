package io.skysail.um.httpbasic

import io.skysail.api.um.AuthenticationService
import org.restlet.Context
import io.skysail.api.um.AuthenticationMode
import org.restlet.security.Authenticator
import org.restlet.Request
import java.security.Principal
import org.restlet.Response
import io.skysail.restlet.utils.LinkUtils
import io.skysail.core.app.SkysailRootApplication
import io.skysail.um.httpbasic.app.HttpBasicUmApplication
import io.skysail.um.httpbasic.app.HttpBasicLoginPage
import org.restlet.security.ChallengeAuthenticator
import org.restlet.data.ChallengeScheme
import io.skysail.api.um.NeverAuthenticatedAuthenticator
import io.skysail.api.um.AlwaysAuthenticatedAuthenticator
import org.restlet.security.User
import java.util.Base64
import java.nio.charset.Charset

object HttpBasicAuthenticationService {
  val ANONYMOUS = "anonymous"
}

class HttpBasicAuthenticationService(userManagementProvider: HttpBasicUserManagementProvider) extends AuthenticationService {

  def getApplicationAuthenticator(context: Context, authMode: AuthenticationMode) = {
    new Authenticator(context) { override protected def authenticate(request: Request, response: Response) = true }
  }

  def getLoginPath(): String = {
    try {
      val httpBasicLoginPageLink = LinkUtils.fromResource(
        userManagementProvider.getSkysailApplication().getSkysailApplication(), classOf[HttpBasicLoginPage]);
      return httpBasicLoginPageLink.uri;
    } catch {
      case e: Throwable => return "/" + classOf[HttpBasicUmApplication].getSimpleName() + "/v1" + SkysailRootApplication.LOGIN_PATH;
    }
  }

  def getLogoutPath(): String = null

  def getPrincipal(request: Request): Principal = {
    val authorization = request.getHeaders().getFirstValue("Authorization");
    if (authorization != null && authorization.startsWith("Basic")) {
      val base64Credentials = authorization.substring("Basic".length()).trim();
      val credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
      val split = credentials.split(":", 2);
      return new User(split(0), split(1));
    }
    new User(HttpBasicAuthenticationService.ANONYMOUS);
  }

  def getResourceAuthenticator(context: Context, authMode: AuthenticationMode): Authenticator = {
    authMode match {
      case AuthenticationMode.DENY_ALL => return new NeverAuthenticatedAuthenticator(context)
      case AuthenticationMode.PERMIT_ALL => return new AlwaysAuthenticatedAuthenticator(context)
      case _ =>
    }

    val challengeAuthenticator = new ChallengeAuthenticator(context, ChallengeScheme.HTTP_BASIC,
      "Skysail Realm");
    challengeAuthenticator.setVerifier(userManagementProvider.getVerifier())
    return challengeAuthenticator;
  }

  def isAuthenticated(request: Request) = !getPrincipal(request).getName().equals(HttpBasicAuthenticationService.ANONYMOUS)

}