package io.skysail.um.shiro.web.impl

import org.restlet.Response
import org.restlet.Request
import org.apache.shiro.subject.SubjectContext

trait RestletSubjectContext extends SubjectContext with RestletRequestPairSource {
  def resolveRequest(): Request
  def resolveResponse(): Response
  def setRequest(request: Request): Unit
  def setResponse(response: Response): Unit
}