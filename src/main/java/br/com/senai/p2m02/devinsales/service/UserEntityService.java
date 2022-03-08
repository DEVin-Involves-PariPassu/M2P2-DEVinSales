package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.repository.UserEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserEntityService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    public void patchPermissao(Long idUser, String nomeFeature, String tipoPermissao) {
        UserEntity userEntity = userEntityRepository.findById(idUser).orElseThrow(() ->
                new EntityNotFoundException("Usuário não encontrado."));
    }
}
