package org.example.authorize.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.authorize.entity.Account;
import org.example.authorize.entity.AuthMethod;
import org.example.authorize.entity.Principal;
import org.example.authorize.enums.AuthType;
import org.example.authorize.exception.AccountInvalidException;
import org.example.authorize.utils.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private String id;

    private String principalId;

    private String firstName;

    private String lastName;

    private String username;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private AuthType authType;

    private boolean enabled;

    public UserPrincipal(String id, String principalId, String firstName, String lastName, String username, String password, Collection<? extends GrantedAuthority> authorities, AuthType authType, boolean enabled) {
        this.id = id;
        this.principalId = principalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.authType = authType;
        this.enabled = enabled;
    }

    public static UserPrincipal create(Account account) {
        if (null == account) {
            throw new UsernameNotFoundException("Can't fine the account");
        }
        if (!SecurityUtils.hasPrincipal(account)) {
            throw new AccountInvalidException("Account don't have principal information");
        }
        if (!SecurityUtils.hasAuthMethod(account)) {
            throw new AccountInvalidException("Account don't have any authentication method");
        }
        if (!SecurityUtils.hasRole(account)) {
            throw new AccountInvalidException("Account don't have any role");
        }

        // Get principal
        Principal principal = account.getPrincipal();

        // Get list granted authority
        List<GrantedAuthority> grantedAuthorities = principal.getRoles()
                .stream().map(BGrantedAuthority::create).collect(Collectors.toList());

        // List auth method
        List<AuthMethod> authMethods = principal.getAuthMethods();
        AuthMethod usernameAndPassAuthMethod = authMethods.stream()
                .filter(authMethod -> authMethod.getAuthType().equals(AuthType.USERNAME_PASSWORD)).findFirst().orElse(null);

        if (null != usernameAndPassAuthMethod) {
            return new UserPrincipal(account.getId(), principal.getId(), account.getFirstName(), account.getLastName(),
                    usernameAndPassAuthMethod.getAuthData1(), usernameAndPassAuthMethod.getAuthData2(), grantedAuthorities,
                    usernameAndPassAuthMethod.getAuthType(), !principal.isDisabled());
        }
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getId() {
        return id;
    }

    public String getPrincipalId() {
        return principalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public AuthType getAuthType() {
        return authType;
    }
}
