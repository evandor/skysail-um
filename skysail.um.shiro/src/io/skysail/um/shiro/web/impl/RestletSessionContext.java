package io.skysail.um.shiro.web.impl;

import org.apache.shiro.session.mgt.SessionContext;
import org.restlet.Request;
import org.restlet.Response;

public interface RestletSessionContext extends SessionContext, RestletRequestPairSource {

   // @Override
    Request getRestletRequest();

    void setRequest(Request request);

    @Override
    Response getRestletResponse();

    void setResponse(Response response);
}
