package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.model.FeatureEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.model.UserFeatureEntity;
import br.com.senai.p2m02.devinsales.model.UserFeatureId;
import br.com.senai.p2m02.devinsales.repository.FeatureEntityRepository;
import br.com.senai.p2m02.devinsales.repository.UserEntityRepository;
import br.com.senai.p2m02.devinsales.repository.UserFeatureEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserEntityService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private FeatureEntityRepository featureEntityRepository;

    @Autowired
    private UserFeatureEntityRepository userFeatureEntityRepository;

    public void patchPermissao(Long idUser, String nomeFeature, String tipoPermissao) {
        UserEntity userEntity = userEntityRepository.findById(idUser).orElseThrow(() ->
                new EntityNotFoundException("Usuário não encontrado."));

        FeatureEntity featureEntity = featureEntityRepository.findFirstByNomeFeature(nomeFeature).orElseThrow(() ->
                new EntityNotFoundException("Feature não encontrada."));

        UserFeatureId idUserFeature = new UserFeatureId(userEntity.getId(), featureEntity.getId());

        Optional<UserFeatureEntity> optionalUserFeature = userFeatureEntityRepository.findById(idUserFeature);
        UserFeatureEntity userFeatureEntity = null;

        if(optionalUserFeature.isEmpty()) {
            userFeatureEntity = new UserFeatureEntity();
            userFeatureEntity.setUser(userEntity);
            userFeatureEntity.setFeature(featureEntity);
            userFeatureEntity.setRead(false);
            userFeatureEntity.setWrite(false);
        } else {
            userFeatureEntity = optionalUserFeature.get();
        }
        if(tipoPermissao.equals("read")) {
            userFeatureEntity.setRead(true);
            userFeatureEntity.setWrite(false);
        } else if (tipoPermissao.equals("write")) {
            userFeatureEntity.setRead(false);
            userFeatureEntity.setWrite(true);
        } else if (tipoPermissao.equals("readwrite")) {
            userFeatureEntity.setRead(true);
            userFeatureEntity.setWrite(true);
        } else {
            throw new IllegalArgumentException("A permissão é ínválida.");
        }
        userFeatureEntityRepository.save(userFeatureEntity);
    }
}
