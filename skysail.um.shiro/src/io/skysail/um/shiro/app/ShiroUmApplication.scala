package io.skysail.um.shiro.app

import org.osgi.service.component.annotations._
import io.skysail.restlet.app._
import io.skysail.restlet.RouteBuilder
import io.skysail.core.security.config.SecurityConfigBuilder
import io.skysail.core.app.SkysailRootApplication
import io.skysail.um.shiro.app.resources.ShiroLoginResource

@Component(property = Array("name=ShiroUmApplication"), service=Array(classOf[ApplicationProvider]))
class ShiroUmApplication extends SkysailApplication(classOf[ShiroUmApplication].getName()) {

	override def defineSecurityConfig(securityConfigBuilder: SecurityConfigBuilder ) = 
    securityConfigBuilder.authorizeRequests().startsWithMatcher(SkysailRootApplication.LOGIN_PATH).permitAll();
	

	override def attach() = {
    router.attach(new RouteBuilder(SkysailRootApplication.LOGIN_PATH, classOf[ShiroLoginResource]))
    //router.attach(new RouteBuilder(SkysailRootApplication.LOGOUT_PATH, classOf[LogoutResource]))
   // router.attach(new RouteBuilder("/currentUser", classOf[CurrentUserResource]);
	}

}
