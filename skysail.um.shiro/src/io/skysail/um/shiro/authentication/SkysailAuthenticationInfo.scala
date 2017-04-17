package io.skysail.um.shiro.authentication

import org.apache.shiro.authc.AuthenticationInfo
import io.skysail.core.um.domain.SkysailUser
import org.apache.shiro.subject.SimplePrincipalCollection
import org.apache.shiro.authc.SimpleAuthenticationInfo
import org.apache.shiro.subject.PrincipalCollection

class SkysailAuthenticationInfo(user: SkysailUser) extends AuthenticationInfo {

  val principals = new SimplePrincipalCollection();
  principals.add(user.id, "internalRealm");
  principals.add(user.username, "internalRealm");

  val simpleAuthenticationInfo = new SimpleAuthenticationInfo(principals, user.password.toCharArray());

  override def getPrincipals(): PrincipalCollection = simpleAuthenticationInfo.getPrincipals()

  override def getCredentials(): java.lang.Object = simpleAuthenticationInfo.getCredentials()

}