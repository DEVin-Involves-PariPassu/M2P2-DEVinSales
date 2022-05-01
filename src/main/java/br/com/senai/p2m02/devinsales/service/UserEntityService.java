package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.dto.FeatureDTO;
import br.com.senai.p2m02.devinsales.model.*;
import br.com.senai.p2m02.devinsales.repository.*;
import br.com.senai.p2m02.devinsales.service.exception.RequiredFieldMissingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    public List<UserEntity> listar(String nome, String dtNascimentoMinStr, String dtNascimentoMaxStr) {
        LocalDate dtNascimentoMin = verificationDate(dtNascimentoMinStr);
        LocalDate dtNascimentoMax = verificationDate(dtNascimentoMaxStr);
        return this.userEntityRepository.findAll(
                Specification.where(
                        SpecificationsUserEntity.nome(nome).and(
                                SpecificationsUserEntity.dtNascimentoMin(dtNascimentoMin).and(
                                        SpecificationsUserEntity.dtNascimentoMax(dtNascimentoMax)
                                )
                        )
                )
        );
    }

    public void patchPermissao(Long idUser, String nomeFeature, String tipoPermissao) {
        UserEntity userEntity = this.userEntityRepository.findById(idUser).orElseThrow(() ->
                new EntityNotFoundException("Usuário não encontrado."));

        FeatureEntity featureEntity = this.featureEntityRepository.findFirstByNomeFeature(nomeFeature).orElseThrow(() ->
                new EntityNotFoundException("Feature não encontrada."));

        UserFeatureId idUserFeature = new UserFeatureId(userEntity.getId(), featureEntity.getId());

        Optional<UserFeatureEntity> optionalUserFeature = this.userFeatureEntityRepository.findById(idUserFeature);
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
        this.userFeatureEntityRepository.save(userFeatureEntity);
    }

    public Long salvar(UserDTO user) {
        UserEntity newUser = validateUser(user);
        this.userEntityRepository.save(newUser);
        return newUser.getId();
    }

    public void atualizar(Long idUser, UserDTO userDTO) {
        UserEntity user = updateUser(idUser, userDTO);

        this.userEntityRepository.save(user);
    }

    public UserEntity updateUser(Long idUser, UserDTO userDTO) {
        isUniqueNameUser(userDTO);
        isUniqueLoginUser(userDTO);
        LocalDate dtNascimento = verificationDate(userDTO);
        verificationAge(dtNascimento);

        UserEntity user = this.userEntityRepository.findById(idUser).orElseThrow(
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

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        UserEntity newUser = new UserEntity();
        newUser.setLogin(user.getLogin());
        newUser.setSenha(encoder.encode(user.getSenha()));
        newUser.setNome(user.getNome());
        newUser.setDtNascimento(userAge);

        Set<UserFeatureEntity> userFeatureEntities = validateFeatures(newUser, user);
        newUser.setUserFeatureEntities(userFeatureEntities);
        this.userEntityRepository.save(newUser);
        return newUser;
    }

    private void existsNome(UserDTO user) {
        if (user.getNome().isBlank()) {
            throw new RequiredFieldMissingException("O campo Nome é obrigatório");
        }
    }

    private void existsLogin(UserDTO user) {
        if (user.getLogin() == null) {
            throw new RequiredFieldMissingException("O campo Login é obrigatório");
        }
    }

    private void existsSenha(UserDTO user) {
        if (user.getSenha() == null) {
            throw new RequiredFieldMissingException("O campo Senha é obrigatório");
        }
    }

    private void existsDtNascimento(UserDTO user) {
        if (user.getDtNascimento() == null) {
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

        return LocalDate.parse(userDTO.getDtNascimento(), dateTimeFormatter);
    }

    private LocalDate verificationDate(String dtNascimentoStr) throws DateTimeParseException {
        if (dtNascimentoStr == null) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return LocalDate.parse(dtNascimentoStr, dateTimeFormatter);
    }

    private void isUniqueLoginUser(UserDTO user) {
        Optional<UserEntity> optionalUser = this.userEntityRepository.findUserEntityByLogin(user.getLogin());
        if (optionalUser.isPresent()) {
            throw new EntityExistsException("Já existe um usuário cadastrado com este login: " + user.getLogin());
        }
    }

    private void isUniqueNameUser(UserDTO user) {
        Optional<UserEntity> optionalUser = this.userEntityRepository.findUserEntityByNome(user.getNome());
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
                userFeatureEntity = this.userFeatureEntityRepository.save(userFeatureEntity);
                userFeatureEntities.add(userFeatureEntity);
            }

        }
        if (!existeUmaFeature) {
            this.userEntityRepository.delete(userEntity);
            throw new IllegalArgumentException("O usuário deve possuir ao menos uma permissão.");
        }
        return userFeatureEntities;
    }

    private FeatureEntity existsFeature(FeatureDTO feature) {
        return this.featureEntityRepository.findFirstByNomeFeature(feature.getFeature()).orElseThrow(
                () -> new IllegalArgumentException("Não existe feature com o nome " + feature.getFeature())
        );
    }

    @Transactional
    public void delete(Long id){
        UserEntity userEntity = this.userEntityRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " was not found."));

        this.userEntityRepository.delete(userEntity);
    }

    @Transactional
    public UserEntity findById(Long id){
        return this.userEntityRepository.findUserEntityById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " was not found." )
        );
    }
}