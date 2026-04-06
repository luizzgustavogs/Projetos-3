package com.projetos3.edenred.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.projetos3.edenred.model.CalculadoraCO2; 

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/impacto")
    public String impacto() {
        return "impacto";
    }

    @PostMapping("/calcular-emissoes")
    public String calcularEmissoes(
            @RequestParam(required = false, defaultValue = "0") Integer colaboradores,
            @RequestParam(required = false, defaultValue = "0") Integer porcentagemDigitais,
            Model model) {

        try {
            if (colaboradores == null || colaboradores <= 0) {
                model.addAttribute("mensagemErro", "Erro ao calcular emissões: Dados inválidos ou ausentes.");
                return "impacto"; 
            }

            int percentualFisico = 100 - porcentagemDigitais;
            int cartoesFisicos = (colaboradores * percentualFisico) / 100;

            // Fazendo os cálculos separados usando a Calculadora
            double producao = CalculadoraCO2.calcularProducao(cartoesFisicos);
            double transporte = CalculadoraCO2.calcularTransporte(cartoesFisicos);
            double descarte = CalculadoraCO2.calcularDescarte(cartoesFisicos);
            double totalCO2 = CalculadoraCO2.calcularEmissaoTotal(cartoesFisicos);

            // Enviando as partes separadas para a tela de resultado
            model.addAttribute("mensagemSucesso", "Cálculo de emissões realizado com sucesso");
            model.addAttribute("qtdCartoesFisicos", cartoesFisicos);
            model.addAttribute("emissaoProducao", producao);
            model.addAttribute("emissaoTransporte", transporte);
            model.addAttribute("emissaoDescarte", descarte);
            model.addAttribute("emissoesCO2", totalCO2);

            return "resultado"; 

        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao calcular emissões.");
            return "impacto";
        }
    }
}