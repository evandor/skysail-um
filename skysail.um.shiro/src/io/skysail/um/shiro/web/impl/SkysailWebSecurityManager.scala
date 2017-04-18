package io.skysail.um.shiro.web.impl

import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.apache.shiro.realm.Realm
import org.apache.shiro.authc.pam.ModularRealmAuthenticator
import org.apache.shiro.authc.AuthenticationListener
import org.apache.shiro.authc.AuthenticationToken
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.subject.PrincipalCollection
import org.apache.shiro.authc.AuthenticationInfo
import org.apache.shiro.subject.SubjectContext
import io.skysail.um.shiro.RestletUtils
import org.apache.shiro.session.mgt.SessionKey
import org.apache.shiro.subject.Subject

class SkysailWebSecurityManager(singleRealm: Realm) extends DefaultWebSecurityManager {

  setSubjectFactory(new SkysailWebSubjectFactory());
  setSessionManager(new SkysailWebSessionManager());
  //        setCacheManager(new MemoryConstrainedCacheManager());
  getAuthenticator.asInstanceOf[ModularRealmAuthenticator].getAuthenticationListeners().add(new AuthenticationListener() {

    override def onSuccess(token: AuthenticationToken, info: AuthenticationInfo) {}

    override def onFailure(token: AuthenticationToken, ae: AuthenticationException) {
      System.out.println(ae);
    }

    override def onLogout(principals: PrincipalCollection) {}
  })
  setRealm(singleRealm)

  override protected def createSubjectContext() = new SkysailWebSubjectContext()

  override protected def copy(subjectContext: SubjectContext): SubjectContext = {
    if (subjectContext.isInstanceOf[RestletSubjectContext]) {
      return new SkysailWebSubjectContext(subjectContext.asInstanceOf[RestletSubjectContext]);
    }
    return super.copy(subjectContext);
  }

  override def createSubject(subjectContext: SubjectContext): Subject = {
    // create a copy so we don't modify the argument's backing map:
    var context = copy(subjectContext);

    // ensure that the context has a SecurityManager instance, and if not,
    // add one:
    context = ensureSecurityManager(context);

    // Resolve an associated Session (usually based on a referenced session
    // ID), and place it in the context before
    // sending to the SubjectFactory. The SubjectFactory should not need to
    // know how to acquire sessions as the
    // process is often environment specific - better to shield the SF from
    // these details:
    context = resolveSession(context);

    // Similarly, the SubjectFactory should not require any concept of
    // RememberMe - translate that here first
    // if possible before handing off to the SubjectFactory:
    context = resolvePrincipals(context);

    val subject = doCreateSubject(context); // NOSONAR

    // save this subject for future reference if necessary:
    // (this is needed here in case rememberMe principals were resolved and
    // they need to be stored in the
    // session, so we don't constantly rehydrate the rememberMe
    // PrincipalCollection on every operation).
    // Added in 1.2:
    // save(subject);

    subject;
  }

  override def getSessionKey(context: SubjectContext): SessionKey = {
    if (RestletUtils.isRestlet(context)) {
      val sessionId = context.getSessionId();
      val request = RestletUtils.getRequest(context);
      val response = RestletUtils.getResponse(context);
      return new RestletSessionKey(sessionId, request, response);
    } else {
      return super.getSessionKey(context);

    }
  }

}