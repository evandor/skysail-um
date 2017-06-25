package io.skysail.um.shiro.app

import org.osgi.service.component.annotations._
import io.skysail.core.app._
import io.skysail.core.restlet.RouteBuilder
import io.skysail.core.security.config.SecurityConfigBuilder
import io.skysail.core.app.SkysailRootApplication
import io.skysail.um.shiro.app.resources.ShiroLoginResource
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes

@Component(property = Array("name=ShiroUmApplication"), 
    service=Array(classOf[ApplicationProvider],classOf[ApplicationRoutesProvider]))
class ShiroUmApplication extends SkysailApplication(classOf[ShiroUmApplication].getName()) {

	override def defineSecurityConfig(securityConfigBuilder: SecurityConfigBuilder ) = 
    securityConfigBuilder.authorizeRequests().startsWithMatcher(SkysailRootApplication.LOGIN_PATH).permitAll()	  

	override def attach() = {
    router.attach(new RouteBuilder(SkysailRootApplication.LOGIN_PATH, classOf[ShiroLoginResource]))
    //router.attach(new RouteBuilder(SkysailRootApplication.LOGOUT_PATH, classOf[LogoutResource]))
   // router.attach(new RouteBuilder("/currentUser", classOf[CurrentUserResource]);
	}
	
	override def routes() = {
	  val route = path("auction2") {
      get {
        //parameter("bid".as[Int], "user") { (bid, user) =>
          //auction ! Bid(user, bid)
          complete((StatusCodes.Accepted, "bid placed"))
        //}
      }
    }
    List(route)
	}

}
