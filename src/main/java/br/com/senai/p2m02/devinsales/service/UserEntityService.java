package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.dto.UserDTO;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.repository.UserEntityRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserEntityService {

    @Autowired
    UserEntityRepository userRepository;

    public Long salvar(UserDTO user){
        UserEntity newUser = validationsUser(user);
        userRepository.save(newUser);
        return newUser.getId();
    }

    private UserEntity validationsUser(UserDTO user) {

        isUniqueNameUser(user);
        isUniqueLoginUser(user);

        UserEntity newUser = new UserEntity();
        newUser.setLogin(user.getLogin());
        newUser.setSenha(user.getSenha());
        newUser.setNome(user.getNome());
        newUser.setDtNascimento(LocalDate.parse(user.getDtNascimento()));

        return newUser;
    }

    private void validationFeature(UserEntity user) { }

    private void verificationAge(UserEntity user) {

        int dataAtual = 0;
        int dataNascimento = 0;
        Integer age = dataAtual - dataNascimento;
        if (age <= 18) {}
    }

    private void isUniqueLoginUser(UserDTO user) {

        Optional<UserEntity> optionalUser = userRepository.findUserEntityByLoginAndSenha(user.getLogin(), user.getSenha());
        if(optionalUser.isPresent()){
            throw new EntityExistsException("Já existe um usuário cadastrado com este login: " + user.getLogin());
        }
    }

    private void isUniqueNameUser(UserDTO user) {

        Optional<UserEntity> optionalUser = userRepository.findUserEntityByNome(user.getNome());
        if(optionalUser.isPresent()){
            throw new EntityExistsException("Já existe este nome de usuário cadastrado: " + user.getNome());
        }
    }

}
