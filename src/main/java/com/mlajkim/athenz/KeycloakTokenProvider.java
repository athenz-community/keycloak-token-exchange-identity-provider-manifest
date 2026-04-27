package com.mlajkim.athenz;

import com.yahoo.athenz.auth.TokenExchangeIdentityProvider;
import com.yahoo.athenz.auth.token.OAuth2Token;
import java.util.List;
import java.util.Collections;

public class KeycloakTokenProvider implements TokenExchangeIdentityProvider {

    @Override
    public String getTokenIdentity(OAuth2Token token) {
        System.out.println(">>> [Keycloak-Plugin] Extracting identity from token...");
        Object preferredUsername = token.getClaim("preferred_username");
        if (preferredUsername != null) {
            return "user." + preferredUsername.toString(); 
        }
        
        Object sub = token.getClaim("sub");
        return sub != null ? "user." + sub.toString() : null;
    }

    @Override
    public String getTokenAudience(OAuth2Token token) {
        System.out.println(">>> [Keycloak-Plugin] Extracting audience...");
        Object aud = token.getClaim("aud");
        if (aud instanceof List && !((List<?>) aud).isEmpty()) {
            return ((List<?>) aud).get(0).toString();
        } else if (aud != null) {
            return aud.toString();
        }
        return "local-open-webui"; 
    }

    @Override
    public List<String> getTokenExchangeClaims() {
        return Collections.emptyList(); 
    }
}