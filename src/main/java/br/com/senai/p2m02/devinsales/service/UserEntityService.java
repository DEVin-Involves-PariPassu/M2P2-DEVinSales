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
import br.com.senai.p2m02.devinsales.dto.UserDTO;
import br.com.senai.p2m02.devinsales.service.exception.UserIsUnderAgeException;
import jakarta.persistence.EntityExistsException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        UserFeatureEntity userFeatureEntity;

        if(optionalUserFeature.isEmpty()) {
            userFeatureEntity = new UserFeatureEntity();
            userFeatureEntity.setId(new UserFeatureId(userEntity.getId(), featureEntity.getId()));
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

    public Long salvar(UserDTO user){
        UserEntity newUser = validationsUser(user);
        userEntityRepository.save(newUser);
        return newUser.getId();
    }

    public void atualizar(Long idUser, UserDTO userDTO){
        UserEntity user = updateUser(idUser, userDTO);

        userEntityRepository.save(user);
    }

    public UserEntity updateUser(Long idUser, UserDTO userDTO){
        isUniqueNameUser(userDTO);
        isUniqueLoginUser(userDTO);
        LocalDate dtNascimento = verificationDate(userDTO);
        verificationAge(dtNascimento);

        UserEntity user = userEntityRepository.findById(idUser).orElseThrow(
                ()-> new EntityNotFoundException("Id de usuário inexistente.")
        );
        user.setLogin(userDTO.getLogin());
        user.setSenha(userDTO.getSenha());
        user.setNome(userDTO.getNome());
        user.setDtNascimento(dtNascimento);

        return user;
    }

    private UserEntity validationsUser(UserDTO user) {

        isUniqueNameUser(user);
        isUniqueLoginUser(user);
        LocalDate userAge = verificationDate(user);
        verificationAge(userAge);

        UserEntity newUser = new UserEntity();
        newUser.setLogin(user.getLogin());
        newUser.setSenha(user.getSenha());
        newUser.setNome(user.getNome());
        newUser.setDtNascimento(userAge);

        return newUser;
    }

    private void verificationAge(LocalDate dtNascimento) {
        Period idade = Period.between(dtNascimento, LocalDate.now());

        if(idade.getYears() < 18) {
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
        if(optionalUser.isPresent()){
            throw new EntityExistsException("Já existe um usuário cadastrado com este login: " + user.getLogin());
        }
    }

    private void isUniqueNameUser(UserDTO user) {
        Optional<UserEntity> optionalUser = userEntityRepository.findUserEntityByNome(user.getNome());
        if(optionalUser.isPresent()){
            throw new EntityExistsException("Já existe este nome de usuário cadastrado: " + user.getNome());
        }
    }

    private void validationFeature(UserEntity user) { }
}