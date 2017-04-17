package io.skysail.um.shiro.web.impl

import org.apache.shiro.web.mgt.DefaultWebSubjectFactory
import org.apache.shiro.subject.SubjectContext
import org.apache.shiro.subject.Subject

class SkysailWebSubjectFactory extends DefaultWebSubjectFactory {

  override def createSubject(context: SubjectContext): Subject = {
    if (!(context.isInstanceOf[RestletSubjectContext])) {
      return super.createSubject(context);
    }
    val rsc = context.asInstanceOf[RestletSubjectContext]
    val securityManager = rsc.resolveSecurityManager();
    val session = rsc.resolveSession();
    val sessionEnabled = rsc.isSessionCreationEnabled();
    val principals = rsc.resolvePrincipals();
    val authenticated = rsc.resolveAuthenticated();
    val host = rsc.resolveHost();
    val request = rsc.resolveRequest();
    val response = rsc.resolveResponse();

    new RestletDelegatingSubject(principals, authenticated, host, session, sessionEnabled, request,
      response, securityManager);

  }

}
