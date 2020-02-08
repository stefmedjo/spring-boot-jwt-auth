package app.example.store.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtCanceledAccessTokenRepository extends JpaRepository<JwtCanceledAccessToken, Long> {}