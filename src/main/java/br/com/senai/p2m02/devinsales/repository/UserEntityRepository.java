package br.com.senai.p2m02.devinsales.repository;

import br.com.senai.p2m02.devinsales.model.UserEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends CrudRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findUserEntityByLoginAndSenha(String login, String senha);

    Optional<UserEntity> findUserEntityByLogin(String login);

    Optional<UserEntity> findUserEntityByNome(String nome);
}
