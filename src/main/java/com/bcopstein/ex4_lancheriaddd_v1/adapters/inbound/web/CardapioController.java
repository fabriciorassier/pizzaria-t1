package com.bcopstein.ex4_lancheriaddd_v1.adapters.inbound.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.adapters.inbound.web.presenters.CabecalhoCardapioPresenter;
import com.bcopstein.ex4_lancheriaddd_v1.adapters.inbound.web.presenters.CardapioPresenter;
import com.bcopstein.ex4_lancheriaddd_v1.application.usecase.CarregarCardapioUC;
import com.bcopstein.ex4_lancheriaddd_v1.application.usecase.RecuperaListaCardapiosUC;
import com.bcopstein.ex4_lancheriaddd_v1.application.usecase.RecuperarCardapioUC;
import com.bcopstein.ex4_lancheriaddd_v1.application.dto.response.ProdutoCardapioResponse;
import com.bcopstein.ex4_lancheriaddd_v1.application.dto.response.CardapioResponse;
import com.bcopstein.ex4_lancheriaddd_v1.domain.model.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.application.auth.AuthTokenService;

@RestController
@RequestMapping("/cardapio")
public class CardapioController {
    private RecuperarCardapioUC recuperaCardapioUC;
    private RecuperaListaCardapiosUC recuperaListaCardapioUC;
    private CarregarCardapioUC carregarCardapioUC;
    private AuthTokenService authTokenService;

    public CardapioController(RecuperarCardapioUC recuperaCardapioUC,
                              RecuperaListaCardapiosUC recuperaListaCardapioUC,
                              CarregarCardapioUC carregarCardapioUC,
                              AuthTokenService authTokenService) {
        this.recuperaCardapioUC = recuperaCardapioUC;
        this.recuperaListaCardapioUC = recuperaListaCardapioUC;
        this.carregarCardapioUC = carregarCardapioUC;
        this.authTokenService = authTokenService;
    }

    @GetMapping("")
    @CrossOrigin("*")
    public ResponseEntity<?> recuperaCardapioCorrente(@RequestHeader("Authorization") String authorizationHeader) {
        authTokenService.extrairCpf(authorizationHeader);
        List<ProdutoCardapioResponse> produtos = carregarCardapioUC.run();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    @CrossOrigin("*")
    public CardapioPresenter recuperaCardapio(@PathVariable(value = "id") long id) {
        CardapioResponse cardapioResponse = recuperaCardapioUC.run(id);
        Set<Long> conjIdSugestoes = new HashSet<>(cardapioResponse.getSugestoesDoChef().stream()
            .map(produto -> produto.getId())
            .toList());
        CardapioPresenter cardapioPresenter = new CardapioPresenter(
            cardapioResponse.getCardapio().getCabecalhoCardapio().titulo());
        for (Produto produto : cardapioResponse.getCardapio().getProdutos()) {
            boolean sugestao = conjIdSugestoes.contains(produto.getId());
            cardapioPresenter.insereItem(produto.getId(), produto.getDescricao(), produto.getPreco(), sugestao);
        }
        return cardapioPresenter;
    }

    @GetMapping("/lista")
    @CrossOrigin("*")
    public List<CabecalhoCardapioPresenter> recuperaListaCardapios() {
         List<CabecalhoCardapioPresenter> lstCardapios =
            recuperaListaCardapioUC.run().cabecalhos().stream()
            .map(cabCar -> new CabecalhoCardapioPresenter(cabCar.id(), cabCar.titulo()))
            .toList();
         return lstCardapios;
    }
}
