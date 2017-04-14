package io.skysail.um.httpbasic.app

import org.osgi.service.component.annotations._
import io.skysail.restlet.app.ApplicationProvider
import io.skysail.restlet.app.SkysailApplication
import io.skysail.core.security.config.SecurityConfigBuilder
import io.skysail.restlet.ScalaRouteBuilder
import io.skysail.core.app.SkysailRootApplication

@Component(property = Array("name=HttpBasicUmApplication"), service=Array(classOf[ApplicationProvider]))
class HttpBasicUmApplication extends SkysailApplication(classOf[HttpBasicUmApplication].getName()) {

	override def defineSecurityConfig(securityConfigBuilder: SecurityConfigBuilder ) = 
		securityConfigBuilder.authorizeRequests().startsWithMatcher("").authenticated()
	

	override def attach() = {
    router.attach(new ScalaRouteBuilder(SkysailRootApplication.LOGIN_PATH, classOf[HttpBasicLoginPage]))
    router.attach(new ScalaRouteBuilder(SkysailRootApplication.LOGOUT_PATH, classOf[HttpBasicLogoutPage]))
	}

}
