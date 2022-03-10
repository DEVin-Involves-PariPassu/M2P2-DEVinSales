package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserEntityRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findUserEntityByLoginAndSenha(String login, String senha);

    Optional<UserEntity> findUserEntityByNome(String nome);
}
