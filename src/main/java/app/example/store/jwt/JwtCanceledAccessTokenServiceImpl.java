package app.example.store.jwt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtCanceledAccessTokenServiceImpl implements JwtCanceledAccessTokenService {

    @Autowired
    JwtCanceledAccessTokenRepository repository;

    @Override
    public JwtCanceledAccessToken create(JwtCanceledAccessToken token) {
        return repository.save(token);
    }

    @Override
    public JwtCanceledAccessToken read(Long id) {
        return repository.getOne(id);
    }

    @Override
    public JwtCanceledAccessToken update(JwtCanceledAccessToken token) {
        return repository.save(token);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<JwtCanceledAccessToken> list() {
        return repository.findAll();
    }

    
}