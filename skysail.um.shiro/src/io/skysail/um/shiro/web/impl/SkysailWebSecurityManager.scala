package io.skysail.um.shiro.web.impl

import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.apache.shiro.realm.Realm

class SkysailWebSecurityManager(singleRealm: Realm) extends DefaultWebSecurityManager {
  
  setSubjectFactory(new SkysailWebSubjectFactory());
        setSessionManager(new SkysailWebSessionManager());
//        setCacheManager(new MemoryConstrainedCacheManager());
//        ((ModularRealmAuthenticator)getAuthenticator()).getAuthenticationListeners().add(new AuthenticationListener(){
//
//            @Override
//            public void onSuccess(AuthenticationToken token, AuthenticationInfo info) {
//            }
//
//            @Override
//            public void onFailure(AuthenticationToken token, AuthenticationException ae) {
//                System.out.println(ae);
//            }
//
//            @Override
//            public void onLogout(PrincipalCollection principals) {
//            }});
//        setRealm(singleRealm)
}