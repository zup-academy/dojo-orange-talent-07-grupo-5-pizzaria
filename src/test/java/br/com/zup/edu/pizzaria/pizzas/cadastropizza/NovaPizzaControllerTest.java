package br.com.zup.edu.pizzaria.pizzas.cadastropizza;

import br.com.zup.edu.pizzaria.ingredientes.cadastrodeingredientes.NovoIngredienteRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NovaPizzaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void deveCadastrarNovaPizza() throws Exception {

        NovoIngredienteRequest bodyIngrediente = new NovoIngredienteRequest("Queijo muçarela", new BigDecimal("2.0"), 200);
        MockHttpServletRequestBuilder requestIgrediente = post("/api/ingredientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(bodyIngrediente));

        mvc.perform(requestIgrediente);

        List<Long> ingredientes = new ArrayList<>();
        ingredientes.add(1L);
        NovaPizzaRequest bodyPizza = new NovaPizzaRequest("Alho", ingredientes);
        MockHttpServletRequestBuilder requestPizza = post("/api/pizzas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(bodyPizza));

        mvc.perform(requestPizza)
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(redirectedUrlPattern("/api/pizzas/*"));

    }

    @Test
    void naoDeveCadastrarUmaPizzaSemIngredientes() throws Exception {

        NovoIngredienteRequest bodyIngrediente = new NovoIngredienteRequest("Queijo muçarela", new BigDecimal("2.0"), 200);
        MockHttpServletRequestBuilder requestIgrediente = post("/api/ingredientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(bodyIngrediente));

        mvc.perform(requestIgrediente);

        NovaPizzaRequest bodyPizza = new NovaPizzaRequest("Alho", Collections.EMPTY_LIST);
        MockHttpServletRequestBuilder requestPizza = post("/api/pizzas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(bodyPizza));

        mvc.perform(requestPizza)
                .andExpect(status().isBadRequest());
    }






}