package io.skysail.um.shiro.web.impl

import org.restlet.Response
import org.restlet.Request

trait RestletRequestPairSource {
  def getRestletRequest(): Request
  def getRestletResponse(): Response
}