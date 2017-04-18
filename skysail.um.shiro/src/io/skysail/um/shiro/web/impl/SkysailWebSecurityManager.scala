package io.skysail.um.shiro.web.impl

import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.apache.shiro.realm.Realm
import org.apache.shiro.authc.pam.ModularRealmAuthenticator
import org.apache.shiro.authc.AuthenticationListener
import org.apache.shiro.authc.AuthenticationToken
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.subject.PrincipalCollection
import org.apache.shiro.authc.AuthenticationInfo

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
}