package br.com.senai.p2m02.devinsales.configuration;

import br.com.senai.p2m02.devinsales.repository.UserEntityRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {
    private final UserEntityRepository repository;

    public AutenticacaoService(UserEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findUserEntityByLogin(username).orElseThrow();
    }
}
