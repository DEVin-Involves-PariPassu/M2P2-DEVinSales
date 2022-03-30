package br.com.senai.p2m02.devinsales.interceptor;

import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class AuthService {

    @Autowired
    private UserEntityRepository repository;

    public UserEntity tryToAuthenticate(String authentication) {
        System.out.println("Authentication: " + authentication);
        if (authentication == null)
            return null;
        if (!authentication.startsWith("Basic"))
            return null;
        String credencialBase64 = authentication.substring(("Basic".length())).trim();
        byte[] bytesBase64Decodificados = Base64.getDecoder().decode(credencialBase64);
        String credencials = new String(bytesBase64Decodificados, StandardCharsets.UTF_8);

        String[] split = credencials.split(":");

        String username = split[0];
        String password = split[1];

        return repository.findUserEntityByLoginAndSenha(username, password).orElse(null);    }
}
