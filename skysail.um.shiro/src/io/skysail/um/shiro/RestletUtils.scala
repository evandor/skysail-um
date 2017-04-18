package io.skysail.um.shiro

import org.restlet.Request
import io.skysail.um.shiro.web.impl.RestletRequestPairSource
import org.restlet.Response

object RestletUtils {

  def getRequest(requestPairSource: Any): Request = {
    if (requestPairSource.isInstanceOf[RestletRequestPairSource]) {
      return requestPairSource.asInstanceOf[RestletRequestPairSource].getRestletRequest();
    }
    return null;
  }

  def getResponse(requestPairSource: Any): Response = {
    if (requestPairSource.isInstanceOf[RestletRequestPairSource]) {
      return requestPairSource.asInstanceOf[RestletRequestPairSource].getRestletResponse();
    }
    return null;
  }

  def isRestlet(requestPairSource: Any): Boolean = {
     requestPairSource.isInstanceOf[RestletRequestPairSource] && isRestlet(requestPairSource.asInstanceOf[RestletRequestPairSource])
  }

  private def isRestlet(source: RestletRequestPairSource): Boolean = {
    val request = source.getRestletRequest();
    val response = source.getRestletResponse();
    return request != null && response != null;
  }
}