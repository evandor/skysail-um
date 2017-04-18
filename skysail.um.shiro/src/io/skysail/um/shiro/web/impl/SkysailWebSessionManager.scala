package io.skysail.um.shiro.web.impl

import org.apache.shiro.session.mgt.DefaultSessionManager
import org.apache.shiro.web.session.mgt.WebSessionManager
import org.slf4j.LoggerFactory
import org.apache.shiro.session.mgt.SessionContext
import org.apache.shiro.session.Session

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
    override protected def onStart( session: Session,  context: SessionContext) = {
        val request = ScalaRestletUtils.getRequest(context);
        val response = ScalaRestletUtils.getResponse(context);

        val sessionId = session.getId();
        storeSessionId(sessionId, request, response);
    }

    override def
    public Serializable getSessionId(SessionKey key) {
        Serializable id = super.getSessionId(key);
        if (id == null && RestletUtils.isRestlet(key)) {
            Request request = RestletUtils.getRequest(key);
            Response response = RestletUtils.getResponse(key);
            id = getSessionId(request, response);
        }
        return id;
    }

    protected Serializable getSessionId(Request request, Response response) {
        return getSessionIdCookieValue(request, response);
    }

    override def
    protected void onExpiration(Session s, ExpiredSessionException ese, SessionKey key) {
        super.onExpiration(s, ese, key);
        onInvalidation(key);
    }

    override def
    protected void onInvalidation(Session session, InvalidSessionException ise, SessionKey key) {
        super.onInvalidation(session, ise, key);
        onInvalidation(key);
    }

    private void onInvalidation(SessionKey key) {
        Request request = RestletUtils.getRequest(key);
        if (request != null) {
            request.getAttributes().remove(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID);
        }
    }

    override def
    protected void onStop(Session session, SessionKey key) {
        super.onStop(session, key);
    }

    private void storeSessionId(Serializable currentId, Request request, Response response) {
        if (currentId == null) {
            String msg = "sessionId cannot be null when persisting for subsequent requests.";
            throw new IllegalArgumentException(msg);
        }

        CookieSetting cookie = createCookie();
        String idString = currentId.toString();
        cookie.setValue(idString);

        response.getCookieSettings().add(cookie);
    }

    private String getSessionIdCookieValue(Request request, Response response) {
        if (!(request instanceof Request)) {
            logger.debug("Current request is not an RestletRequest - cannot get session ID cookie.  Returning null.");
            return null;
        }
        if (request.getCookies().size() == 0) {
            return null;
        }
        org.restlet.data.Cookie sessionCookie = request.getCookies().getFirst(ShiroHttpSession.DEFAULT_SESSION_ID_NAME);
        return sessionCookie != null ? sessionCookie.getValue() : null;
    }

    private CookieSetting createCookie() {
        CookieSetting cookieSetting = new CookieSetting(ShiroHttpSession.DEFAULT_SESSION_ID_NAME, null);
        cookieSetting.setAccessRestricted(true);
        cookieSetting.setPath("/");
        cookieSetting.setComment("Skysail cookie-based authentication");
        cookieSetting.setMaxAge(300);
        return cookieSetting;
    }
  
}