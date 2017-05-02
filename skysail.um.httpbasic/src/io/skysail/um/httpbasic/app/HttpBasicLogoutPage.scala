package io.skysail.um.httpbasic.app

import io.skysail.restlet.resources._
import io.skysail.core.um.domain.Credentials

class HttpBasicLogoutPage extends EntityServerResource[Credentials] {
  override def getEntity() = new Credentials()
}