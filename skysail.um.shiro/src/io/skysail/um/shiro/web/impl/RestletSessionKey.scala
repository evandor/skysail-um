package io.skysail.um.shiro.web.impl

import org.apache.shiro.session.mgt.DefaultSessionKey
import org.restlet.Response
import org.restlet.Request

class RestletSessionKey(sessionId: java.io.Serializable = null,
    @transient request: Request,
    @transient response: Response) extends DefaultSessionKey with RestletRequestPairSource {

  require(request != null)
  require(response != null)

  def getRestletRequest() = request
  def getRestletResponse() = response
}