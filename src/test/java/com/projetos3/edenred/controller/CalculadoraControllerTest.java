package com.projetos3.edenred.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(CalculadoraController.class)
public class CalculadoraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveriaCalcularImpactoComSucesso() throws Exception {

        String jsonEnvio = "{\"colaboradores\": 100, \"porcentagemDigitais\": 50 }";
        mockMvc.perform(post("/api/calculadora/impacto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonEnvio))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.co2").exists())
                .andExpect(jsonPath("$.plastico").exists())
                .andExpect(jsonPath("$.logistica").exists());
    }

    @Test
    public void deveriaFuncionarComJsonVazioUsandoPadroes() throws Exception {
        mockMvc.perform(post("/api/calculadora/impacto")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.co2").value("0,0 g"));
    }
}