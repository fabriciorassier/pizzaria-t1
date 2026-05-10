package com.bcopstein.ex4_lancheriaddd_v1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
  "spring.datasource.url=jdbc:h2:mem:entrega1tests;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
  "spring.sql.init.mode=always"
})
public class TelePizzariaEntrega1IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void uc3DeveRetornarCardapioCorrenteComTokenValido() throws Exception {
        String tokenCpf1111 = "MTExMQ==";

        mockMvc.perform(get("/cardapio")
                        .header("Authorization", "Bearer " + tokenCpf1111)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].descricao").isNotEmpty())
                .andExpect(jsonPath("$[0].preco").isNumber());
    }

    @Test
    void uc4DeveSubmeterPedidoEAprovar() throws Exception {
        String tokenCpf1111 = "MTExMQ==";

        String body = """
                {
                  "enderecoEntrega": "Rua Teste Integracao, 123",
                  "itens": [
                    {"produtoId": 1, "quantidade": 1},
                    {"produtoId": 3, "quantidade": 1}
                  ]
                }
                """;

        mockMvc.perform(post("/pedidos/submeter")
                        .header("Authorization", "Bearer " + tokenCpf1111)
            .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pedidoId").isNumber())
                .andExpect(jsonPath("$.status").value("APROVADO"))
                .andExpect(jsonPath("$.custoItens").isNumber())
                .andExpect(jsonPath("$.imposto").isNumber())
                .andExpect(jsonPath("$.custoTotal").isNumber())
                .andExpect(jsonPath("$.mensagem").value("Pedido aprovado!"));
    }
}
