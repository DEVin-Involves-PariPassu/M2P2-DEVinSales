package br.com.senai.p2m02.devinsales.api;

import br.com.senai.p2m02.devinsales.model.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthz")
public class HealthzController {

    @GetMapping
    public ResponseEntity<String> get() {
        return new ResponseEntity<>("It's all working", HttpStatus.OK);
    }

    @GetMapping("/loggeduser")
    public ResponseEntity<String> getLoggedUserInfo(@RequestAttribute("loggedUser") UserEntity loggedUser) {
        return new ResponseEntity<>("Here is loggedUser infos: "+loggedUser.toString(), HttpStatus.OK);
    }
}
