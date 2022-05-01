package br.com.senai.p2m02.devinsales.controller;
import br.com.senai.p2m02.devinsales.dto.FeatureDTO;
import br.com.senai.p2m02.devinsales.dto.UserDTO;
import br.com.senai.p2m02.devinsales.model.FeatureEntity;
import br.com.senai.p2m02.devinsales.model.UserEntity;
import br.com.senai.p2m02.devinsales.model.UserFeatureEntity;
import br.com.senai.p2m02.devinsales.model.UserFeatureId;
import br.com.senai.p2m02.devinsales.service.UserEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserEntityService service;

//    @MockBean
//    private TokenService tokenService;
//
//    @MockBean
//    private UserEntityRepository userEntityRepository;
//
//    @MockBean
//    private AutenticacaoService autenticacaoService;

    private UserDTO userDTO;
    private FeatureDTO featureDTO;
    private UserEntity userEntity;
    private FeatureEntity featureEntity;
    private UserFeatureEntity userFeatureEntity;
    private UserFeatureId userFeatureId;

    @BeforeEach
    public void setup(){
        //MockitoAnnotations.openMocks(this);
        startUser();
    }
    public static final Long ID= 1L;
    public static final String LOGIN= "kalyana";
    public static final String SENHA= "kaly123";
    public static final String NOME= "Kalyana Greim";
    public static final String DTNASCIMENTO = "12/10/1997";

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

        userFeatureId = new UserFeatureId(ID, featureEntity.getId());

        userFeatureEntity = new UserFeatureEntity();
        userFeatureEntity.setUser(userEntity);
        userFeatureEntity.setFeature(featureEntity);
        userFeatureEntity.setWrite(true);
        userFeatureEntity.setRead(true);
        userFeatureEntity.setId(userFeatureId);

        userEntity.setUserFeatureEntities(Set.of(userFeatureEntity));

    }


    @Test
    public void givenUserObject_whenPostUser_thenReturnUserSave() throws Exception{

        //when(this.service.salvar(any(UserDTO.class))).thenReturn(anyLong());
        when(service.salvar(userDTO)).thenReturn(1L);

        String userJson = """
                {
                "nome": "Kalyana Greim",
                "login": "kalyana",
                "senha": "kaly123",
                "dtNascimento": "12/10/1997",
                "features":
                [
                    {
                    "feature": "product",
                    "read": true,
                    "write": true
                    }
                ]
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .header("Content-Type", "application/json")
                        .content(userJson))
                .andExpect(status().isCreated())
                .andReturn();

    }


}