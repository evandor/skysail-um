package io.skysail.um.httpbasic.app

import io.skysail.restlet.resources.EntityServerResource2
import io.skysail.core.um.domain.Credentials

class HttpBasicLogoutPage extends EntityServerResource2[Credentials] {
  override def getEntity() = new Credentials()
}