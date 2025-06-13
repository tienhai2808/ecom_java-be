package store.ecom.backend.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import store.ecom.backend.security.user.ShopUserDetails;

public class JwtUtils {
  private String jwtSecret;
  private int expirationTime;

  public String generaTokenForUser(Authentication authentication) {
    ShopUserDetails userPrincipal = (ShopUserDetails) authentication.getPrincipal();

    List<String> roles = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

    return Jwts.builder().setSubject(userPrincipal.getEmail()).claim("id", userPrincipal.getId()).claim("roles", roles)
        .setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + expirationTime))
        .signWith(key(), SignatureAlgorithm.HS256).compact();
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parser()
        .setSigningKey(key())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
}
