package app.example.store.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
public class JwtUtil {
    
    
    final private String privateKey  = "hhKkqjjqopamqhduooeppam";
    final private String refreshKey = "mppaoppappammqllk" ;
    
    final private int refresh_token_duration = 24 * 60 * 60 * 1000;
    final private int access_token_duration = 60 * 60 * 1000;
    
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token,Claims::getSubject);
    }
    
    public Date getIssuedAtDateFromToken(String token){
        return getClaimFromToken(token,Claims::getIssuedAt);
    }
    
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token,Claims::getExpiration);
    }
    
    public <T> T getClaimFromToken(String token,Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    public Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token).getBody();
    }
    
    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    public JwtTokens generateTokens(UserDetails userDetails){
        String accessToken = generateAccessToken(userDetails);
        String refreshToken = generateRefreshToken(userDetails);
        return new JwtTokens(accessToken,refreshToken);
    }
    
    public String generateAccessToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + access_token_duration))
                .signWith(SignatureAlgorithm.HS512, privateKey)
                .compact();
    }

   public String generateRefreshToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername() + refreshKey)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refresh_token_duration))
                .signWith(SignatureAlgorithm.HS512, privateKey)
                .compact();
    }
   
   public Boolean validateAccessToken(String token, UserDetails userDetails){
       final String subject = getUsernameFromToken(token);
       return (subject.equals(userDetails.getUsername()) && !isTokenExpired(token));
   }

   public Boolean validateRefreshToken(String token, UserDetails userDetails){
       final String subject = getUsernameFromToken(token);
       return (subject.equals(userDetails.getUsername() + refreshKey) && !isTokenExpired(token));
   }

   public Boolean accessTokenIsValidAndExpired(String token, String username){
        final String subject = getUsernameFromToken(token);
        return (subject.equals(username) && isTokenExpired(token));
   }
   /**
    *   Access token and refresh token can be refreshed if
    *   access subject == username && refresh subject == username + refresh key
    *   access token is expired && refresh token is not expired
    *   
    */
   public Boolean canBeRefreshed(String accessToken,String refreshToken, UserDetails userDetails){
       String accessSubject = getUsernameFromToken(accessToken);
       String refreshSubject = getUsernameFromToken(refreshToken);
       return ( accessSubject.equals(userDetails.getUsername()) && 
                refreshSubject.equals(userDetails.getUsername() + refreshKey) && 
                isTokenExpired(accessToken) &&
                !isTokenExpired(refreshToken)
              );
   }

}
