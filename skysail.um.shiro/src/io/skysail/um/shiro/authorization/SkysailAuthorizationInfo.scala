package io.skysail.um.shiro.authorization

import org.apache.shiro.authz.AuthorizationInfo
import io.skysail.core.um.domain.SkysailUser
import org.apache.shiro.authz.SimpleAuthorizationInfo
import java.util.Collection
import org.apache.shiro.authz.Permission
import scala.collection.JavaConverters._

class SkysailAuthorizationInfo(user: SkysailUser) extends AuthorizationInfo {

  val authInfo = new SimpleAuthorizationInfo();
  val roles = scala.collection.mutable.Set[String]()
  
  user.getRoles.foreach(r => roles.add(r.name));
  authInfo.setRoles(roles.asJava);

  override def getStringPermissions(): Collection[String] = {
    return authInfo.getStringPermissions();
  }

  override def getRoles(): Collection[String] = {
    return authInfo.getRoles();
  }

  override def getObjectPermissions(): Collection[Permission] = {
    return authInfo.getObjectPermissions();
  }
}