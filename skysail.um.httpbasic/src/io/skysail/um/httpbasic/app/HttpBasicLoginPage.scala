package io.skysail.um.httpbasic.app

import io.skysail.core.restlet.resources._
import io.skysail.core.um.domain.Credentials
import org.restlet.resource.Get
import io.skysail.core.restlet.responses.FormResponse
import org.restlet.data.Form
import io.skysail.core.app.SkysailRootApplication

class HttpBasicLoginPage extends PostEntityServerResource[Credentials] {

  var app: HttpBasicUmApplication = null

  override protected def doInit() = { app = getApplication().asInstanceOf[HttpBasicUmApplication] }

  @Get("htmlform")
  def createForm(): FormResponse[Credentials] = {
    return new FormResponse(getResponse(), getEntity(), SkysailRootApplication.LOGIN_PATH);
  }

  override def getEntity() = new Credentials(None,null,null)

  def getData(form: Form): Credentials = {
    if (form == null) {
      return new Credentials(None,null,null);
    }
    return new Credentials(None, form.getFirstValue("username"), form.getFirstValue("password"));
  }

  override def createEntityTemplate() = new Credentials(None, null,null)

  override def addEntity(entity: Credentials):Credentials = {null}

  override def redirectTo(): String = {
    if (app.isAuthenticated(getRequest())) {
      return "/";
    }
    return SkysailRootApplication.LOGIN_PATH;
  }

}