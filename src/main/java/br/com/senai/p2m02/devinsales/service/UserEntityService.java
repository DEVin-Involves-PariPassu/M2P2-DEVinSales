package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.dto.FeatureDTO;
import br.com.senai.p2m02.devinsales.model.FeatureEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.model.UserFeatureEntity;
import br.com.senai.p2m02.devinsales.model.UserFeatureId;
import br.com.senai.p2m02.devinsales.repository.FeatureEntityRepository;
import br.com.senai.p2m02.devinsales.repository.UserEntityRepository;
import br.com.senai.p2m02.devinsales.repository.UserFeatureEntityRepository;
import br.com.senai.p2m02.devinsales.service.exception.RequiredFieldMissingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.senai.p2m02.devinsales.dto.UserDTO;
import br.com.senai.p2m02.devinsales.service.exception.UserIsUnderAgeException;
import jakarta.persistence.EntityExistsException;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

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
        UserFeatureEntity userFeatureEntity;

        if (optionalUserFeature.isEmpty()) {
            userFeatureEntity = new UserFeatureEntity();
            userFeatureEntity.setId(new UserFeatureId(userEntity.getId(), featureEntity.getId()));
            userFeatureEntity.setUser(userEntity);
            userFeatureEntity.setFeature(featureEntity);
            userFeatureEntity.setRead(false);
            userFeatureEntity.setWrite(false);
        } else {
            userFeatureEntity = optionalUserFeature.get();
        }
        if (tipoPermissao.equals("read")) {
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

    public Long salvar(UserDTO user) {
        UserEntity newUser = validateUser(user);
        userEntityRepository.save(newUser);
        return newUser.getId();
    }

    public void atualizar(Long idUser, UserDTO userDTO) {
        UserEntity user = updateUser(idUser, userDTO);

        userEntityRepository.save(user);
    }

    public UserEntity updateUser(Long idUser, UserDTO userDTO) {
        isUniqueNameUser(userDTO);
        isUniqueLoginUser(userDTO);
        LocalDate dtNascimento = verificationDate(userDTO);
        verificationAge(dtNascimento);

        UserEntity user = userEntityRepository.findById(idUser).orElseThrow(
                () -> new EntityNotFoundException("Id de usuário inexistente.")
        );
        user.setLogin(userDTO.getLogin());
        user.setSenha(userDTO.getSenha());
        user.setNome(userDTO.getNome());
        user.setDtNascimento(dtNascimento);

        return user;
    }

    private UserEntity validateUser(UserDTO user) {

        existsNome(user);
        existsLogin(user);
        existsSenha(user);
        existsDtNascimento(user);
        isUniqueNameUser(user);
        isUniqueLoginUser(user);
        LocalDate userAge = verificationDate(user);
        verificationAge(userAge);
        isFeaturesEmpty(user);

        UserEntity newUser = new UserEntity();
        newUser.setLogin(user.getLogin());
        newUser.setSenha(user.getSenha());
        newUser.setNome(user.getNome());
        newUser.setDtNascimento(userAge);
        newUser = userEntityRepository.save(newUser);
        Set<UserFeatureEntity> userFeatureEntities = validateFeatures(newUser, user);
        newUser.setUserFeatureEntities(userFeatureEntities);

        return newUser;
    }

    private void existsNome(UserDTO user) {
        if (user.getNome() == null){
            throw new RequiredFieldMissingException("O campo Nome é obrigatório");
        }
    }
    private void existsLogin(UserDTO user) {
        if (user.getLogin() == null){
            throw new RequiredFieldMissingException("O campo Login é obrigatório");
        }
    }
    private void existsSenha(UserDTO user) {
        if (user.getSenha() == null){
            throw new RequiredFieldMissingException("O campo Senha é obrigatório");
        }
    }
    private void existsDtNascimento(UserDTO user) {
        if (user.getDtNascimento() == null){
            throw new RequiredFieldMissingException("O campo dtNascimento é obrigatório");
        }
    }
    private void verificationAge(LocalDate dtNascimento) {
        Period idade = Period.between(dtNascimento, LocalDate.now());

        if (idade.getYears() < 18) {
            throw new UserIsUnderAgeException("O usuário deve possuir mais de 18 anos.");
        }
    }

    private LocalDate verificationDate(UserDTO userDTO) throws DateTimeParseException {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dtNascimento = LocalDate.parse(userDTO.getDtNascimento(), dateTimeFormatter);

        return dtNascimento;
    }

    private void isUniqueLoginUser(UserDTO user) {
        Optional<UserEntity> optionalUser = userEntityRepository.findUserEntityByLogin(user.getLogin());
        if (optionalUser.isPresent()) {
            throw new EntityExistsException("Já existe um usuário cadastrado com este login: " + user.getLogin());
        }
    }

    private void isUniqueNameUser(UserDTO user) {
        Optional<UserEntity> optionalUser = userEntityRepository.findUserEntityByNome(user.getNome());
        if (optionalUser.isPresent()) {
            throw new EntityExistsException("Já existe este nome de usuário cadastrado: " + user.getNome());
        }
    }

    private void isFeaturesEmpty(UserDTO user) {

        if (user.getFeatures() == null || user.getFeatures().isEmpty()) {
            throw new IllegalArgumentException("A lista de features não existe ou é vazia");
        }
    }

    private Set<UserFeatureEntity> validateFeatures(UserEntity userEntity, UserDTO user) {
        Set<UserFeatureEntity> userFeatureEntities = new HashSet<>();
        boolean existeUmaFeature = false;
        for (FeatureDTO feature : user.getFeatures()) {
            FeatureEntity featureEntity = existsFeature(feature);
            UserFeatureEntity userFeatureEntity = new UserFeatureEntity();
            userFeatureEntity.setId(new UserFeatureId(userEntity.getId(), featureEntity.getId()));
            userFeatureEntity.setUser(userEntity);
            userFeatureEntity.setFeature(featureEntity);
            if (feature.getRead() == null || feature.getRead().equals(false)) {
                userFeatureEntity.setRead(false);
            } else {
                userFeatureEntity.setRead(true);
                existeUmaFeature = true;
            }
            if (feature.getWrite() == null || feature.getWrite().equals(false)) {
                userFeatureEntity.setWrite(false);
            } else {
                userFeatureEntity.setWrite(true);
                existeUmaFeature = true;
            }
            if(existeUmaFeature) {
                userFeatureEntity = userFeatureEntityRepository.save(userFeatureEntity);
                userFeatureEntities.add(userFeatureEntity);
            }

        }
        if (!existeUmaFeature) {
            userEntityRepository.delete(userEntity);
            throw new IllegalArgumentException("O usuário deve possuir ao menos uma permissão.");
        }
        return userFeatureEntities;
    }

    private FeatureEntity existsFeature(FeatureDTO feature) {
        FeatureEntity featureEntity = featureEntityRepository.findFirstByNomeFeature(feature.getFeature()).orElseThrow(
                () -> new IllegalArgumentException("Não existe feature com o nome " + feature.getFeature())
        );
        return featureEntity;
    }


}