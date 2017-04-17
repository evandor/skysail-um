package io.skysail.um.shiro.authentication

import org.restlet.security.SecretVerifier
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.UsernamePasswordToken
import org.slf4j.LoggerFactory
import org.restlet.security.Verifier
import org.apache.shiro.authc.UnknownAccountException
import org.apache.shiro.authc.IncorrectCredentialsException
import org.apache.shiro.authc.LockedAccountException
import org.apache.shiro.authc.AuthenticationException

class SimpleDelegatingVerifier extends SecretVerifier {

  val log = LoggerFactory.getLogger(this.getClass())

  override def verify(identifier: String, secret: Array[Char]): Int = {
    val ident = identifier.replace("@", "&#64;");
    val currentUser = SecurityUtils.getSubject();
    val token = new UsernamePasswordToken(identifier, new String(secret));
    log.debug("login event for user '{}'", identifier);
    try {
      currentUser.login(token);
      log.debug("login event for user '{}' successful", identifier);
      return Verifier.RESULT_VALID;
    } catch {
      case uae: UnknownAccountException => log.debug("UnknownAccountException '" + uae.getMessage() + "' when login in " + identifier);
      case ice: IncorrectCredentialsException => log.debug("IncorrectCredentialsException '" + ice.getMessage() + "' when login in " + identifier);
      case lae: LockedAccountException => log.info("LockedAccountException '" + lae.getMessage() + "' when login in " + identifier, lae);
      case ae: AuthenticationException => log.error("AuthenticationException '" + ae.getMessage() + "' when login in " + identifier, ae);
    }
    return Verifier.RESULT_INVALID;
  }

}