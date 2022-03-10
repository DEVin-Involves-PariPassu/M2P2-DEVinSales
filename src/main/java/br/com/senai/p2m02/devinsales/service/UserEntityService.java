package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.dto.UserDTO;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.repository.UserEntityRepository;
import br.com.senai.p2m02.devinsales.service.exception.UserIsUnderAgeException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public void atualizar(Long idUser, UserDTO userDTO){
        UserEntity user = updateUser(idUser, userDTO);

        userRepository.save(user);
    }

    public UserEntity updateUser(Long idUser, UserDTO userDTO){
        isUniqueNameUser(userDTO);
        isUniqueLoginUser(userDTO);
        LocalDate dtNascimento = verificationDate(userDTO);
        verificationAge(dtNascimento);

        UserEntity user = userRepository.findById(idUser).orElseThrow(
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

        UserEntity newUser = new UserEntity();
        newUser.setLogin(user.getLogin());
        newUser.setSenha(user.getSenha());
        newUser.setNome(user.getNome());
        newUser.setDtNascimento(LocalDate.parse(user.getDtNascimento()));

        return newUser;
    }

    private void validationFeature(UserEntity user) { }

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
        Optional<UserEntity> optionalUser = userRepository.findUserEntityByLogin(user.getLogin());
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
