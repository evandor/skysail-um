package io.skysail.um.shiro.web.impl

import org.apache.shiro.session.mgt.DefaultSessionManager
import org.apache.shiro.web.session.mgt.WebSessionManager
import org.slf4j.LoggerFactory
import org.apache.shiro.session.mgt.SessionContext
import org.apache.shiro.session.Session
import io.skysail.um.shiro.RestletUtils
import org.apache.shiro.session.mgt.SessionKey
import org.restlet.Request
import org.restlet.Response
import org.apache.shiro.session.ExpiredSessionException
import org.apache.shiro.session.InvalidSessionException
import org.apache.shiro.web.servlet.ShiroHttpServletRequest
import org.restlet.data.CookieSetting
import org.apache.shiro.web.servlet.ShiroHttpSession

class SkysailWebSessionManager extends DefaultSessionManager with WebSessionManager {

  val log = LoggerFactory.getLogger(this.getClass())

  override def isServletContainerSessions() = false

  /**
   * Stores the Session's ID, usually as a Cookie, to associate with future
   * requests.
   *
   * @param session
   *            the session that was just {@link #createSession created}.
   */
  override protected def onStart(session: Session, context: SessionContext) = {
    val request = RestletUtils.getRequest(context);
    val response = RestletUtils.getResponse(context);

    val sessionId = session.getId();
    storeSessionId(sessionId, request, response);
  }

  override def getSessionId(key: SessionKey): java.io.Serializable = {
    var id = super.getSessionId(key);
    if (id == null && RestletUtils.isRestlet(key)) {
      val request = RestletUtils.getRequest(key);
      val response = RestletUtils.getResponse(key);
      id = getSessionId(request, response);
    }
    return id;
  }

  protected def getSessionId(request: Request, response: Response): java.io.Serializable = {
    getSessionIdCookieValue(request, response);
  }

  override protected def onExpiration(s: Session, ese: ExpiredSessionException, key: SessionKey) = {
    super.onExpiration(s, ese, key);
    onInvalidation(key);
  }

  override protected def onInvalidation(session: Session, ise: InvalidSessionException, key: SessionKey) = {
    super.onInvalidation(session, ise, key);
    onInvalidation(key);
  }

  private def onInvalidation(key: SessionKey) = {
    val request = RestletUtils.getRequest(key);
    if (request != null) {
      request.getAttributes().remove(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID);
    }
  }

  override protected def onStop(session: Session, key: SessionKey) = super.onStop(session, key)

  private def storeSessionId(currentId: java.io.Serializable, request: Request, response: Response) = {
    if (currentId == null) {
      val msg = "sessionId cannot be null when persisting for subsequent requests.";
      throw new IllegalArgumentException(msg);
    }

    val cookie = createCookie();
    val idString = currentId.toString();
    cookie.setValue(idString);

    response.getCookieSettings().add(cookie);
  }

  private def getSessionIdCookieValue(request: Request, response: Response): String = {
    if (!(request.isInstanceOf[Request])) {
      log.debug("Current request is not an RestletRequest - cannot get session ID cookie.  Returning null.");
      return null;
    }
    if (request.getCookies().size() == 0) {
      return null;
    }
    val sessionCookie = request.getCookies().getFirst(ShiroHttpSession.DEFAULT_SESSION_ID_NAME);
    if (sessionCookie != null) sessionCookie.getValue() else null
  }

  private def createCookie(): CookieSetting = {
    val cookieSetting = new CookieSetting(ShiroHttpSession.DEFAULT_SESSION_ID_NAME, null);
    cookieSetting.setAccessRestricted(true);
    cookieSetting.setPath("/");
    cookieSetting.setComment("Skysail cookie-based authentication");
    cookieSetting.setMaxAge(300);
    return cookieSetting;
  }

}