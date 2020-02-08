package app.example.store.jwt;

import java.util.List;

public interface JwtCanceledAccessTokenService {

    JwtCanceledAccessToken create(JwtCanceledAccessToken token);
    JwtCanceledAccessToken read(Long id);
    JwtCanceledAccessToken update(JwtCanceledAccessToken token);
    void delete(Long id);
    List<JwtCanceledAccessToken> list();
    
}