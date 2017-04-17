package io.skysail.um.shiro.authentication

import org.restlet.ext.crypto.CookieAuthenticator
import org.restlet.Context
import org.apache.shiro.cache.CacheManager
import io.skysail.core.app.SkysailRootApplication
import org.restlet.Request
import org.restlet.Response
import org.apache.shiro.util.ThreadContext
import org.slf4j.LoggerFactory
import org.restlet.routing.Filter
import org.apache.shiro.SecurityUtils
import org.restlet.data.CookieSetting

class SkysailCookieAuthenticator(
    context: Context,
    realm: String,
    encryptSecretKey: Array[Byte],
    optional: Boolean,
    cacheManager: CacheManager) extends CookieAuthenticator(context, realm, encryptSecretKey) {

  val log = LoggerFactory.getLogger(this.getClass())

  setIdentifierFormName("username")
  setSecretFormName("password")
  setLoginFormPath("/v1/_login")
  setLoginPath("/v1/_login")
  setLogoutPath(SkysailRootApplication.LOGOUT_PATH)
  // set to false, see https://github.com/evandor/skysail/issues/13
  setOptional(optional) // we want anonymous users too?
  setVerifier(new SimpleDelegatingVerifier())

  override protected def afterHandle(request: Request, response: Response) = {
    ThreadContext.remove();
    super.afterHandle(request, response);
  }

  override protected def getCredentialsCookie(request: Request, response: Response): CookieSetting = {
    val credentialsCookie = super.getCredentialsCookie(request, response);
    credentialsCookie.setPath("/");
    return credentialsCookie;
  }

  override protected def authenticate(request: Request, response: Response): Boolean = {
    // Restore credentials from the cookie
    log.debug("getting cookie with name {}", getCookieName());
    val credentialsCookie = request.getCookies().getFirst(getCookieName());
    if (credentialsCookie == null || credentialsCookie.getValue() == null) {
      return super.authenticate(request, response);
    }
    val cookieValue = credentialsCookie.getValue();
    if ("".equals(cookieValue.trim())) {
      log.warn("provided authentication cookie with value of '{}'", cookieValue);
      return false; //super.authenticate(request, response);
    }
    if (byPassIfPublicUrl(request)) {
      return false;
    }
    request.setChallengeResponse(parseCredentials(cookieValue));
    return super.authenticate(request, response);
  }

  private def byPassIfPublicUrl(request: Request) = {
    "anonymous".equals(request.getOriginalRef().getQueryAsForm().getFirstValue("_asUser"));
  }

  override protected def logout(request: Request, response: Response): Int = {
    val result = super.logout(request, response);
    //        if (cacheManager != null) {
    //            Cache<Object, Object> cache = cacheManager.getCache(SkysailHashedCredentialsMatcher.CREDENTIALS_CACHE);
    //            // need to find the current value:
    //            // cache.remove(value); // NOSONAR
    //            // instead: remove all for now
    //            cache.clear();
    //        }
    if (Filter.STOP == result) {
      val subject = SecurityUtils.getSubject();
      subject.logout();
      response.redirectSeeOther("/");
    }
    return result;
  }
}