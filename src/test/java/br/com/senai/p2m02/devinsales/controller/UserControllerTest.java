package br.com.senai.p2m02.devinsales.controller;

import br.com.senai.p2m02.devinsales.service.UserEntityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;



class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserEntityService service;

    @Test
    void post() throws Exception {
    }
}