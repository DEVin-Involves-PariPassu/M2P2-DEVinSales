package br.com.senai.p2m02.devinsales.api.v1;

import br.com.senai.p2m02.devinsales.configuration.TokenService;
import br.com.senai.p2m02.devinsales.dto.LoginDTO;
import br.com.senai.p2m02.devinsales.dto.TokenDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AutenticacaoController {
    private final AuthenticationManager authManager;
    private final TokenService tokenService;

    public AutenticacaoController(AuthenticationManager authManager, TokenService tokenService) {
        this.authManager = authManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> autenticar(@RequestBody @Valid LoginDTO loginDto) {
        UsernamePasswordAuthenticationToken login = loginDto.converter();
        try {
            Authentication authentication = authManager.authenticate(login);
            String token = tokenService.gerarToken(authentication);
            return ResponseEntity.ok(new TokenDTO(token, "Bearer"));
        } catch (AuthenticationException e) {
            return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
        }
    }
}
