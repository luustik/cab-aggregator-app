package cab.aggregator.app.passengerservice.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cab.aggregator.app.passengerservice.utility.KeycloakConstants.AZP_CLAIM;
import static cab.aggregator.app.passengerservice.utility.KeycloakConstants.AZP_CLAIM_VALUE;
import static cab.aggregator.app.passengerservice.utility.KeycloakConstants.REALM_ACCESS_CLAIM;
import static cab.aggregator.app.passengerservice.utility.KeycloakConstants.REALM_ACCESS_CLAIM_VALUE;
import static cab.aggregator.app.passengerservice.utility.KeycloakConstants.ROLE_ADMIN;
import static cab.aggregator.app.passengerservice.utility.KeycloakConstants.ROLE_PREFIX;


public class KeycloakJwtTokenConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        if(jwt.getClaim(AZP_CLAIM).equals(AZP_CLAIM_VALUE)) {
            return Collections.singletonList(new SimpleGrantedAuthority(ROLE_ADMIN));
        }
        var realmAccessMap = jwt.getClaimAsMap(REALM_ACCESS_CLAIM);
        Object rolesObject = realmAccessMap.get(REALM_ACCESS_CLAIM_VALUE);

        if (rolesObject instanceof List<?> tmpRoles) {
            List<String> realmAccess = tmpRoles.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .toList();

            return realmAccess.stream()
                    .map(role -> ROLE_PREFIX + role)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

}
