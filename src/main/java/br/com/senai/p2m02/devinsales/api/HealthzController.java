package br.com.senai.p2m02.devinsales.api;

import br.com.senai.p2m02.devinsales.model.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/loggeduser/{feature}")
    public ResponseEntity<String> getLoggedUserFeaturePermissions(@RequestAttribute("loggedUser") UserEntity loggedUser,
                                                                  @PathVariable String feature) {
        String permissions = loggedUser.getNome() + " for feature = "+feature+":";
        permissions += loggedUser.canRead(feature) ? " Can read!" : " Can't read!";
        permissions += loggedUser.canWrite(feature) ? " Can write!" :  "Can't write!";

        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }

}
