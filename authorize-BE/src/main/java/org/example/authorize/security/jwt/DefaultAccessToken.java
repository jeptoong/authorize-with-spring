package org.example.authorize.security.jwt;

import java.io.Serializable;
import java.util.Date;

/**
 * Default Access Token.
 */
public class DefaultAccessToken implements AccessToken, Serializable {

    private String token;
    private Date expiration;
    private RefreshToken refreshToken;

    public DefaultAccessToken() {
        this(null, null);
    }

    public DefaultAccessToken(String token, Date expiration) {
        this.token = token;
        this.expiration = expiration;
    }

    public DefaultAccessToken(String token, Date expiration, RefreshToken refreshToken) {
        this.token = token;
        this.expiration = expiration;
        this.refreshToken = refreshToken;
    }

    @Override
    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    @Override
    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean isExpired() {
        return null != expiration && expiration.before(new Date());
    }

    @Override
    public Date getExpiration() {
        return expiration;
    }

    @Override
    public int getExpiresIn() {
        return null != expiration ? Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L)
                .intValue() : 0;
    }

    @Override
    public String getToken() {
        return token;
    }
}
