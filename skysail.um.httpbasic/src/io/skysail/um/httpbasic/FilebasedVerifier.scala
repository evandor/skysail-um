package io.skysail.um.httpbasic

import org.restlet.security.SecretVerifier
import org.restlet.security.Verifier
import org.osgi.service.component.annotations._
import io.skysail.core.security.SecurityContextHolder
import io.skysail.core.security.token.UsernamePasswordAuthenticationToken
import io.skysail.core.security.SecurityContext
import org.restlet.Request
import org.restlet.Response
import org.restlet.data.Method
import io.skysail.api.um.UserManagementRepository

//@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
class FilebasedVerifier(userManagementRepository: UserManagementRepository) extends SecretVerifier with Verifier {

//  @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
//  @volatile var userManagementRepository: io.skysail.api.um.UserManagementRepository = null

  override def verify(identifier: String, secret: Array[Char]): Int = {
    SecurityContextHolder.clearContext();
    // identifier = identifier.replace("@", "&#64;"); // NOSONAR
    val user = userManagementRepository.getUser(identifier);
    if (!user.isPresent()) {
      return Verifier.RESULT_INVALID;
    }
    if (SecretVerifier.compare(secret, user.get().getSecret())) {
      val securityContext = new SecurityContext(new UsernamePasswordAuthenticationToken(user.get(), user.get().getSecret()));
      SecurityContextHolder.setContext(securityContext);
      return Verifier.RESULT_VALID;
    } else {
      return Verifier.RESULT_INVALID;
    }
  }

  override def verify(request: Request, response: Response): Int = {
    if (request.getMethod().equals(Method.OPTIONS)) {
      // let OPTION requests (preflight requests when using CORS and http
      // basic auth) pass
      return Verifier.RESULT_VALID;
    }
    return super.verify(request, response);
  }
}