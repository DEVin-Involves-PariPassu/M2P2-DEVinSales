package br.com.senai.p2m02.devinsales.service;

import br.com.senai.p2m02.devinsales.dto.FeatureDTO;
import br.com.senai.p2m02.devinsales.dto.UserDTO;
import br.com.senai.p2m02.devinsales.model.*;
import br.com.senai.p2m02.devinsales.repository.FeatureEntityRepository;
import br.com.senai.p2m02.devinsales.repository.UserEntityRepository;
import br.com.senai.p2m02.devinsales.repository.UserFeatureEntityRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class UserServiceTests {

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private FeatureEntityRepository featureEntityRepository;

    @Mock
    private UserFeatureEntityRepository userFeatureEntityRepository;

    @InjectMocks
    private UserEntityService userEntityService;

    private UserDTO userDTO;
    private FeatureDTO featureDTO;
    private UserEntity userEntity;
    private FeatureEntity featureEntity;
    private UserFeatureId userFeatureId;
    private UserFeatureEntity userFeatureEntity;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        startUser();
    }

    public static final Long ID= 1L;
    public static final String LOGIN= "kalyana";
    public static final String SENHA= "kaly123";
    public static final String NOME= "Kalyana Greim";
    public static final String DTNASCIMENTO = "12/10/1996";

    private void startUser() {

        List<FeatureDTO> featuresList = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        //Cria UserDTO que ser?? usado para criar o usu??rio
        userDTO = new UserDTO();
        userDTO.setNome(NOME);
        userDTO.setSenha(SENHA);
        userDTO.setLogin(LOGIN);
        userDTO.setDtNascimento(DTNASCIMENTO);
        userDTO.setFeatures(featuresList);

        userEntity = new UserEntity();
        userEntity.setDtNascimento(LocalDate.parse(DTNASCIMENTO, dateTimeFormatter));
        userEntity.setSenha(SENHA);
        userEntity.setNome(NOME);
        userEntity.setLogin(LOGIN);
        userEntity.setId(ID);


        featureDTO = new FeatureDTO();
        featureDTO.setFeature("product");
        featureDTO.setRead(true);
        featureDTO.setWrite(true);
        featuresList.add(0, featureDTO);

        //Cria featureEntity que ser?? retornada pelo m??todo existsFeature
        featureEntity = new FeatureEntity();
        featureEntity.setId(1L);
        featureEntity.setNomeFeature("product");

        UserFeatureId userFeatureId = new UserFeatureId(ID, featureEntity.getId());

        UserFeatureEntity userFeatureEntity = new UserFeatureEntity();
        userFeatureEntity.setUser(userEntity);
        userFeatureEntity.setFeature(featureEntity);
        userFeatureEntity.setWrite(true);
        userFeatureEntity.setRead(true);
        userFeatureEntity.setId(userFeatureId);

        userEntity.setUserFeatureEntities(Set.of(userFeatureEntity));
    }

    @Test
    @DisplayName("Salvar novo usu??rio")
    void whenSaveAnUserReturnTheUserId() {

        //verifica se j?? existe usu??rio com o mesmo login; neste caso, n??o tem, ent??o retorna um Optional.empty();
        when(userEntityRepository.findUserEntityByLogin(userDTO.getLogin())).thenReturn(Optional.empty());
        //verifica se j?? existe usu??rio com o mesmo nome; neste caso, n??o tem, ent??o retorna um Optional.empty();
        when(userEntityRepository.findUserEntityByNome(userDTO.getNome())).thenReturn(Optional.empty());
        //salva o usu??rio passada sem as features ainda; ?? usado o thenAnswer para inserir o ID da entidade criada.
        when(userEntityRepository.save(any(UserEntity.class))).thenAnswer((Answer<UserEntity>) invocation -> {
            UserEntity usuarioRetornado = invocation.getArgument(0);
            if (usuarioRetornado != null) {
                usuarioRetornado.setId(1L);
            }
            return usuarioRetornado;
        });
        //verifica se a feature passada existe; neste caso, ela existe e ?? retornada dentro de um Optional
        when(featureEntityRepository.findFirstByNomeFeature(featureDTO.getFeature())).thenReturn(Optional.of(featureEntity));
        //salva a feature passada; neste caso, ?? usado o thenAnswer para inserir o ID da entidade criada.
        when(userFeatureEntityRepository.save(any(UserFeatureEntity.class))).thenAnswer((Answer<UserFeatureEntity>) invocation -> {
            UserFeatureEntity featureRetornada = invocation.getArgument(0);
            if (featureRetornada != null) {
                featureRetornada.setId(new UserFeatureId(1L, 1L));
            }
            return featureRetornada;
        });

        Long idUsuario = userEntityService.salvar(userDTO);

        assertEquals(1L, idUsuario);
        verify(this.userEntityRepository, times(1)).findUserEntityByLogin(userDTO.getLogin());
        verify(this.userEntityRepository, times(1)).findUserEntityByNome(userDTO.getNome());
        verify(this.userEntityRepository, times(2)).save(any(UserEntity.class));
        verify(this.featureEntityRepository, times(1)).findFirstByNomeFeature(featureDTO.getFeature());
        verify(this.userFeatureEntityRepository, times(1)).save(any(UserFeatureEntity.class));
    }

    @Test
    @DisplayName("Deleta um usu??rio")
    public void deveDeletarUmUsuarioQuandoPassarUmIdValido(){

        when(userEntityRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        userEntityService.delete(userEntity.getId());

        verify(this.userEntityRepository, Mockito.times(1)).delete(userEntity);
    }

    @Test
    @DisplayName("N??o deleta um usu??rio")
    public void naoDeveDeletarUmUsuarioQuandoPassarUmIdInvalido() {
        when(userEntityRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userEntityService.delete(2L);
            verify(this.userEntityRepository, Mockito.times(1)).findAll();
        });
    }
    @Test
    @DisplayName("N??o deve salvar usu??rio com o mesmo login")
    public void naoDeveSalvarUsuarioQuandoOLoginJaExistir(){

        when(userEntityRepository.findUserEntityByLogin(userDTO.getLogin())).thenReturn(Optional.of(userEntity));

        assertThrows(EntityExistsException.class, () -> userEntityService.salvar(userDTO));

        verify(this.userEntityRepository, times(1)).findUserEntityByLogin(userDTO.getLogin());
    }
    @Test
    @DisplayName("N??o deve salvar usu??rio com o mesmo nome")
    public void naoDeveSalvarUsuarioQuandoONomeJaExistir(){

        when(userEntityRepository.findUserEntityByNome(userDTO.getNome())).thenReturn(Optional.of(userEntity));

        assertThrows(EntityExistsException.class, () -> userEntityService.salvar(userDTO));

        verify(this.userEntityRepository, times(1)).findUserEntityByNome(userDTO.getNome());
    }

    @Test
    @DisplayName("N??o deve atualizar usu??rio se o nome j?? existir")
    public void naoDeveAtualizarUsuarioSeONomeJaExistir(){

        when(userEntityRepository.findUserEntityByNome(userDTO.getNome())).thenReturn(Optional.of(userEntity));
        when(userEntityRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

        assertThrows(EntityExistsException.class, () -> {
            userEntityService.atualizar(1L,userDTO);
        });

        verify(this.userEntityRepository, times(1)).findUserEntityByNome(userDTO.getNome());
    }

    @Test
    @DisplayName("N??o deve salvar um usu??rio sem ao menos uma feature")
    public void naoDeveSalvarUsuarioSemAoMenosUmaFeature(){

        when(featureEntityRepository.findFirstByNomeFeature(featureDTO.getFeature())).thenReturn(Optional.of(featureEntity));

        when(userFeatureEntityRepository.save(any(UserFeatureEntity.class))).thenAnswer((Answer<UserFeatureEntity>) invocation -> {
            UserFeatureEntity featureRetornada = invocation.getArgument(0);
            if (featureRetornada != null) {
                featureRetornada.setId(new UserFeatureId(1L, 1L));
            }
            return featureRetornada;
        });

    }
    @Test
    @DisplayName("Busca usu??rio pelo Id")
    public void deveRetornarUmUsuarioQuandoPassarUmIdValido(){
        when(userEntityRepository.findUserEntityById(userEntity.getId())).thenReturn(Optional.of(userEntity));

        UserEntity response = userEntityService.findById(userEntity.getId());

        assertNotNull(response);
        assertEquals(UserEntity.class, response.getClass());
        assertEquals(userEntity.getId(), response.getId());
        assertEquals(NOME, response.getNome());
        assertEquals(LOGIN, response.getLogin());
        assertEquals(SENHA, response.getSenha());
        assertEquals(userEntity.getDtNascimento(), response.getDtNascimento());

    }

    @Test
    @DisplayName("N??o existe usu??rio com o id informado")
    public void deveLancarUmaExcecaoQuandoBuscarUmUsuarioComUmIdInexistente(){
        when(userEntityRepository.findUserEntityById(userEntity.getId())).thenReturn(Optional.of(userEntity));

        assertThrows(EntityNotFoundException.class, () -> userEntityService.findById(2L));

    }

    @Test
    @DisplayName("Atualizar usu??rio")
    public void deveAtualizarUsuarioQuandoPassarNovosDadosEIdValido(){
        when(userEntityRepository.findById(ID)).thenReturn(Optional.of(userEntity));

        try{
            userEntityService.atualizar(ID, userDTO);
        } catch (Exception ex){
            assertEquals(EntityNotFoundException.class, ex.getClass());
            assertEquals("Id de usu??rio inexistente.", ex.getMessage());
        }
    }

    @Test
    @DisplayName("N??o atualizar usu??rio se Id n??o existir")
    public void deveLancarExcecaoQuandoTentarAtualizarUsuarioInexistente(){
        when(userEntityRepository.findById(ID)).thenReturn(Optional.of(userEntity));

        try{
            userEntityService.atualizar(2L, userDTO);
        } catch (Exception ex){
            assertEquals(EntityNotFoundException.class, ex.getClass());
            assertEquals("Id de usu??rio inexistente.", ex.getMessage());
        }
    }

    @Test
    @DisplayName("Deve atualizar permiss??o com permiss??o read")
    public void deveAtualizarPermissaoComPermissaoRead (){
        when(userEntityRepository.findById(ID)).thenReturn(Optional.of(userEntity));
        when(featureEntityRepository.findFirstByNomeFeature(featureEntity.getNomeFeature())).thenReturn(Optional.of(featureEntity));
        when(userFeatureEntityRepository.findById(any())).thenReturn(Optional.empty());
        when(userFeatureEntityRepository.save(any())).thenReturn(userFeatureEntity);

        userEntityService.patchPermissao(ID, "product", "read");

        verify(this.userEntityRepository, times(1)).findById(ID);
        verify(this.featureEntityRepository, times(1)).findFirstByNomeFeature(featureEntity.getNomeFeature());
        verify(this.userFeatureEntityRepository, times(1)).findById(any());
        verify(this.userFeatureEntityRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve atualizar permiss??o com permiss??o write")
    public void deveAtualizarPermissaoComPermissaoWrite (){
        when(userEntityRepository.findById(ID)).thenReturn(Optional.of(userEntity));
        when(featureEntityRepository.findFirstByNomeFeature(featureEntity.getNomeFeature())).thenReturn(Optional.of(featureEntity));
        when(userFeatureEntityRepository.findById(any())).thenReturn(Optional.empty());
        when(userFeatureEntityRepository.save(any())).thenReturn(userFeatureEntity);

        userEntityService.patchPermissao(ID, "product", "write");

        verify(this.userEntityRepository, times(1)).findById(ID);
        verify(this.featureEntityRepository, times(1)).findFirstByNomeFeature(featureEntity.getNomeFeature());
        verify(this.userFeatureEntityRepository, times(1)).findById(any());
        verify(this.userFeatureEntityRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve atualizar permiss??o com permiss??o readwrite")
    public void deveAtualizarPermissaoComPermissaoReadWrite (){
        when(userEntityRepository.findById(ID)).thenReturn(Optional.of(userEntity));
        when(featureEntityRepository.findFirstByNomeFeature(featureEntity.getNomeFeature())).thenReturn(Optional.of(featureEntity));
        when(userFeatureEntityRepository.findById(any())).thenReturn(Optional.empty());
        when(userFeatureEntityRepository.save(any())).thenReturn(userFeatureEntity);

        userEntityService.patchPermissao(ID, "product", "readwrite");

        verify(this.userEntityRepository, times(1)).findById(ID);
        verify(this.featureEntityRepository, times(1)).findFirstByNomeFeature(featureEntity.getNomeFeature());
        verify(this.userFeatureEntityRepository, times(1)).findById(any());
        verify(this.userFeatureEntityRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("N??o deve atualizar permiss??o com usu??rio inexistente")
    public void naoDeveAtualizarPermissaoComUsuarioInexistente (){
        when(userEntityRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userEntityService.patchPermissao(ID, "product", "read");
        });

        verify(this.userEntityRepository, times(1)).findById(ID);
    }

    @Test
    @DisplayName("N??o deve atualizar permiss??o com permiss??o inexistente")
    public void naoDeveAtualizarPermissaoComPermissaoInexistente (){
        when(userEntityRepository.findById(ID)).thenReturn(Optional.of(userEntity));
        when(featureEntityRepository.findFirstByNomeFeature(featureEntity.getNomeFeature())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userEntityService.patchPermissao(ID, "product", "read");
        });

        verify(this.userEntityRepository, times(1)).findById(ID);
        verify(this.featureEntityRepository, times(1)).findFirstByNomeFeature(featureEntity.getNomeFeature());
    }

    @Test
    @DisplayName("Tipo de permiss??o inexistente")
    public void quandoTipoDePermissaoForInexistente (){
        when(userEntityRepository.findById(ID)).thenReturn(Optional.of(userEntity));
        when(featureEntityRepository.findFirstByNomeFeature(featureEntity.getNomeFeature())).thenReturn(Optional.of(featureEntity));
        when(userFeatureEntityRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            userEntityService.patchPermissao(ID, "product", "udjfhf");
        });

        verify(this.userEntityRepository, times(1)).findById(ID);
        verify(this.featureEntityRepository, times(1)).findFirstByNomeFeature(featureEntity.getNomeFeature());
        verify(this.userFeatureEntityRepository, times(1)).findById(any());
    }

}