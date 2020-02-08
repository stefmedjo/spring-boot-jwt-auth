package app.example.store.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokens {
    
    public String accessToken;
    public String refreshToken;
    
}