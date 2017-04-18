package io.skysail.um.shiro.authorization

import org.apache.shiro.realm.AuthorizingRealm
import io.skysail.um.shiro.ShiroBasedUserManagementProvider
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher
import org.apache.shiro.authc.AuthenticationToken
import org.apache.shiro.subject.PrincipalCollection
import org.apache.shiro.authz.AuthorizationInfo
import io.skysail.core.um.domain.SkysailUser
import org.apache.shiro.authc.UsernamePasswordToken
import io.skysail.um.shiro.authentication.SkysailAuthenticationInfo
import org.apache.shiro.authc.AuthenticationInfo

class ShiroAuthorizingRealm(
    hashedCredetialsMatcher: SimpleCredentialsMatcher,
    simpleUserManagementProvider: ShiroBasedUserManagementProvider) extends AuthorizingRealm {

  setCredentialsMatcher(hashedCredetialsMatcher);
  //        setAuthenticationCachingEnabled(true);
  //        Cache<Object, AuthenticationInfo> authenticationCache = new MapCache<>("credentialsCache",
  //                new ConcurrentHashMap<Object, AuthenticationInfo>());
  //        setAuthenticationCache(authenticationCache);

  override def getName() = this.getClass().getSimpleName()

  override def doGetAuthenticationInfo(token: AuthenticationToken): AuthenticationInfo = {
    val upToken = token.asInstanceOf[UsernamePasswordToken]
    val username = upToken.getUsername();
    if (username == null) {
      throw new IllegalArgumentException("Null usernames are not allowed by this realm.");
    }
    val user = getUser(username);
    if (user != null) {

      // TODO lock account, credentials expired ...
      // if (user.isLocked()) {
      // throw new LockedAccountException("Account [" + account +
      // "] is locked.");
      // }
      // if (user.isCredentialsExpired()) {
      // String msg = "The credentials for account [" + account +
      // "] are expired";
      // throw new ExpiredCredentialsException(msg);
      // }
    }

    return new SkysailAuthenticationInfo(user);

  }

  override def doGetAuthorizationInfo(principals: PrincipalCollection): AuthorizationInfo = {
    val username = getUsername(principals);
    val user = getUserByPrincipal(username);

    return new SkysailAuthorizationInfo(user);
  }

  protected def getUsername(principals: PrincipalCollection): String = {
    return getAvailablePrincipal(principals).toString();
  }

  private def getUser(username: String): SkysailUser = {
    return simpleUserManagementProvider.getByUsername(username);
  }

  private def getUserByPrincipal(principal: String): SkysailUser = {
    return simpleUserManagementProvider.getByPrincipal(principal);
  }

  override def clearCache(principals: PrincipalCollection) = {
    super.clearCache(principals);
  }
}