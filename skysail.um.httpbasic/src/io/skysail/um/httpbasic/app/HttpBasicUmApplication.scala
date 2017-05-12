package io.skysail.um.httpbasic.app

import org.osgi.service.component.annotations._
import io.skysail.core.app._
import io.skysail.core.security.config.SecurityConfigBuilder
import io.skysail.restlet.RouteBuilder
import io.skysail.core.app.SkysailRootApplication

@Component(property = Array("name=HttpBasicUmApplication"), service=Array(classOf[ApplicationProvider]))
class HttpBasicUmApplication extends SkysailApplication(classOf[HttpBasicUmApplication].getName()) {

	override def defineSecurityConfig(securityConfigBuilder: SecurityConfigBuilder ) = 
		securityConfigBuilder.authorizeRequests().startsWithMatcher("").authenticated()
	

	override def attach() = {
    router.attach(new RouteBuilder(SkysailRootApplication.LOGIN_PATH, classOf[HttpBasicLoginPage]))
    router.attach(new RouteBuilder(SkysailRootApplication.LOGOUT_PATH, classOf[HttpBasicLogoutPage]))
	}

}
