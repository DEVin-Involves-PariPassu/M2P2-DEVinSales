package br.com.senai.p2m02.devinsales.controller;

import br.com.senai.p2m02.devinsales.api.v1.AutenticacaoController;
import br.com.senai.p2m02.devinsales.configuration.AutenticacaoService;
import br.com.senai.p2m02.devinsales.configuration.TokenService;
import br.com.senai.p2m02.devinsales.dto.FeatureDTO;
import br.com.senai.p2m02.devinsales.dto.LoginDTO;
import br.com.senai.p2m02.devinsales.dto.UserDTO;
import br.com.senai.p2m02.devinsales.model.*;
import br.com.senai.p2m02.devinsales.repository.UserEntityRepository;
import br.com.senai.p2m02.devinsales.service.UserEntityService;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserEntityService service;


    private UserDTO userDTO;
    private FeatureDTO featureDTO;
    private UserEntity userEntity;
    private FeatureEntity featureEntity;
    private UserFeatureEntity userFeatureEntity;
    private UserFeatureId userFeatureId;

    @BeforeEach
    public void setup(){
        startUser();
    }
    public static final Long ID= 1L;
    public static final String LOGIN= "daimich";
    public static final String SENHA= "dai123";
    public static final String NOME= "Daiana Michels";
    public static final String DTNASCIMENTO = "23/09/1997";

    private void startUser() {

        List<FeatureDTO> featuresList = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        featureDTO = new FeatureDTO();
        featureDTO.setFeature("product");
        featureDTO.setRead(true);
        featureDTO.setWrite(true);
        featuresList.add(0, featureDTO);

        //Cria UserDTO que será usado para criar o usuário
        userDTO = new UserDTO();
        userDTO.setNome(NOME);
        userDTO.setSenha(SENHA);
        userDTO.setLogin(LOGIN);
        userDTO.setDtNascimento(DTNASCIMENTO);
        userDTO.setFeatures(featuresList);

        userEntity = new UserEntity();
        userEntity.setDtNascimento(LocalDate.parse(userDTO.getDtNascimento(), dateTimeFormatter));
        userEntity.setSenha(userDTO.getSenha());
        userEntity.setNome(userDTO.getNome());
        userEntity.setLogin(userDTO.getLogin());
        userEntity.setId(ID);

        //Cria featureEntity que será retornada pelo método existsFeature
        featureEntity = new FeatureEntity();
        featureEntity.setId(1L);
        featureEntity.setNomeFeature(featureDTO.getFeature());

        userFeatureId = new UserFeatureId(userEntity.getId(), featureEntity.getId());

        userFeatureEntity = new UserFeatureEntity();
        userFeatureEntity.setUser(userEntity);
        userFeatureEntity.setFeature(featureEntity);
        userFeatureEntity.setWrite(featuresList.get(0).getWrite());
        userFeatureEntity.setRead(featuresList.get(0).getRead());
        userFeatureEntity.setId(userFeatureId);

        userEntity.setUserFeatureEntities(Set.of(userFeatureEntity));

    }

    @Test
    @DisplayName("Deletar usuário pelo Id")
    public void deveDeletarUsuarioQuandoInformarOId() throws Exception{
        String body = "{\"login\":\"admin\",\"senha\":\"admin123\"}";

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/auth")
                        .header("Content-Type", "application/json" )
                        .content(body))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        String token = (String) json.get("token");

        Assertions.assertNotNull(token);


        doNothing().when(service).delete(userEntity.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/{id_user}", 1L)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json" )
                )
                .andExpect(status().isNoContent())
                .andReturn();
    }


}