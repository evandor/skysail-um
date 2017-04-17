package io.skysail.um.shiro.app

import org.osgi.service.component.annotations._
import io.skysail.restlet.app.ApplicationProvider
import io.skysail.restlet.app.SkysailApplication
import io.skysail.core.security.config.SecurityConfigBuilder
import io.skysail.restlet.ScalaRouteBuilder
import io.skysail.um.shiro.app.resources.LoginResource
import io.skysail.core.app.SkysailRootApplication

@Component(property = Array("name=ShiroUmApplication"), service=Array(classOf[ApplicationProvider]))
class ShiroUmApplication extends SkysailApplication(classOf[ShiroUmApplication].getName()) {

	override def defineSecurityConfig(securityConfigBuilder: SecurityConfigBuilder ) = 
    securityConfigBuilder.authorizeRequests().startsWithMatcher(SkysailRootApplication.LOGIN_PATH).permitAll();
	

	override def attach() = {
    router.attach(new ScalaRouteBuilder(SkysailRootApplication.LOGIN_PATH, classOf[LoginResource]))
    //router.attach(new ScalaRouteBuilder(SkysailRootApplication.LOGOUT_PATH, classOf[LogoutResource]))
   // router.attach(new ScalaRouteBuilder("/currentUser", classOf[CurrentUserResource]);
	}

}
