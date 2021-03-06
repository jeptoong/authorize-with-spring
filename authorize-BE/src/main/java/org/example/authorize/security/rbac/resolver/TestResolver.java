package org.example.authorize.security.rbac.resolver;

import org.example.authorize.security.rbac.AbstractRequestResolver;
import org.springframework.security.core.Authentication;

public class TestResolver extends AbstractRequestResolver<Boolean> {

    public TestResolver(Authentication authentication, Object[] arguments) {
        super(authentication, arguments);
    }

    @Override
    public Boolean resolve() {
        return true;
    }
}
