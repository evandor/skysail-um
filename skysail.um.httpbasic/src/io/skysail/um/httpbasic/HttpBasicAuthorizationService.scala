package io.skysail.um.httpbasic

import io.skysail.api.um.AuthorizationService
import org.restlet.security.Enroler
import org.restlet.security.Role

class HttpBasicAuthorizationService(userManagementProvider: HttpBasicUserManagementProvider) extends AuthorizationService {
  def getEnroler(): Enroler = {
    ???
  }

  def getRolesFor(x$1: String): java.util.Set[Role] = {
    ???
  }
}