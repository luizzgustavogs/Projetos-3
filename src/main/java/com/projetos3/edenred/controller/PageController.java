package com.projetos3.edenred.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.projetos3.edenred.model.CalculadoraCO2; 
import com.projetos3.edenred.model.CalculadoraMateriais;

import java.util.HashMap;
import java.util.Map;

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
            @RequestParam(required = false, defaultValue = "0") Double distanciaEntrega,
            Model model) {
        // Método original mantido
        return "impacto";
    }

    @PostMapping("/calcular-ajax")
    @ResponseBody
    public Map<String, String> calcularAjax(
            @RequestParam(required = false, defaultValue = "0") Integer colaboradores,
            @RequestParam(required = false, defaultValue = "0") Integer porcentagemDigitais,
            @RequestParam(required = false, defaultValue = "0") Double distanciaEntrega) {

        Map<String, String> resposta = new HashMap<>();
        try {
            if (porcentagemDigitais < 0 || porcentagemDigitais > 100) {
                resposta.put("erro", "Porcentagem deve estar entre 0 e 100");
                return resposta;
            }

            if (colaboradores == null || colaboradores <= 0 || distanciaEntrega == null || distanciaEntrega <= 0) {
                resposta.put("co2", "0 g");
                resposta.put("plastico", "0 g");
                resposta.put("logistica", "0 g");
                return resposta;
            }

            int percentualFisico = 100 - porcentagemDigitais;
            int cartoesFisicos = (colaboradores * percentualFisico) / 100;

            double plasticoUtilizado = CalculadoraMateriais.calcularPlastico(cartoesFisicos);
            double producao = CalculadoraCO2.calcularProducao(cartoesFisicos);
            double transporte = CalculadoraMateriais.calcularLogistica(cartoesFisicos, distanciaEntrega);
            double descarte = CalculadoraCO2.calcularDescarte(cartoesFisicos);
            double totalCO2 = producao + transporte + descarte;

            resposta.put("co2", CalculadoraMateriais.formatarPeso(totalCO2));
            resposta.put("plastico", CalculadoraMateriais.formatarPeso(plasticoUtilizado));
            resposta.put("logistica", CalculadoraMateriais.formatarPeso(transporte));

            return resposta;
        } catch (Exception e) {
            resposta.put("erro", "Erro no cálculo");
            return resposta;
        }
    }

    // --- NOVAS ROTAS PARA A TELA DE SIMULAÇÃO --- //

    @GetMapping("/simulacao")
    public String simulacao(
        @RequestParam String nomeEmpresa,
        @RequestParam int colaboradores,
        @RequestParam int distanciaEntrega,
        @RequestParam int porcentagemDigitais,
        Model model
    ) {
        
        // Passamos os dados base para a tela. O JavaScript vai usar isso no slider.
        model.addAttribute("nomeEmpresa", nomeEmpresa);
        model.addAttribute("colaboradores", colaboradores);
        model.addAttribute("distanciaEntrega", distanciaEntrega);
        model.addAttribute("porcentagemDigitais", porcentagemDigitais);
        
        return "simulacao";
    }

    @PostMapping("/simular-ajax")
    @ResponseBody
    public Map<String, Object> simular(
        @RequestParam Integer colaboradores,
        @RequestParam Integer distanciaEntrega,
        @RequestParam int porcentagemAtual,
        @RequestParam int porcentagemAlvo
) {

        Map<String, Object> resposta = new HashMap<>();

        try {
            if (porcentagemAlvo < porcentagemAtual) {
                resposta.put("erro", "Não é possível reduzir o nível de digitalização.");
                return resposta;
            }
            
            if (porcentagemAtual < 0 || porcentagemAtual > 100 || porcentagemAlvo < 0 || porcentagemAlvo > 100) {
                resposta.put("erro", "Porcentagens devem estar entre 0 e 100");
                return resposta;
            }

            if (colaboradores == null || colaboradores <= 0 || distanciaEntrega == null || distanciaEntrega <= 0) {
                resposta.put("co2", "0 kg");
                resposta.put("residuos", "0 kg");
                resposta.put("logistica", "0 kg");
                resposta.put("plastico", "0 kg");
                resposta.put("papel", "0 kg");
                resposta.put("arvores", 0);
                resposta.put("carros", 0);
                return resposta;
            }

            if (porcentagemAtual == porcentagemAlvo) {
                resposta.put("co2", "0 kg");
                resposta.put("residuos", "0 kg");
                resposta.put("logistica", "0 kg");
                resposta.put("plastico", "0 kg");
                resposta.put("papel", "0 kg");
                resposta.put("arvores", 0);
                resposta.put("carros", 0);
                return resposta;
            }

            double impactoAtual = calcularImpacto(colaboradores, distanciaEntrega,porcentagemAtual);

            double impactoFuturo = calcularImpacto(colaboradores, distanciaEntrega, porcentagemAlvo);

            double reducaoCO2 = impactoAtual - impactoFuturo;

            double plasticoAtual = CalculadoraMateriais.calcularPlastico(colaboradores);
            double plasticoFuturo = plasticoAtual * (1 - (porcentagemAlvo / 100.0));
            double plasticoReduzido = plasticoAtual - plasticoFuturo;

            double papelAtual = CalculadoraMateriais.calcularPapel(colaboradores);
            double papelFuturo = papelAtual * (1 - (porcentagemAlvo / 100.0));
            double papelReduzido = papelAtual - papelFuturo;

            double logisticaAtual = CalculadoraMateriais.calcularLogistica(colaboradores, distanciaEntrega);
            double logisticaFuturo = logisticaAtual * (1 - (porcentagemAlvo / 100.0));
            double logisticaReduzida = logisticaAtual - logisticaFuturo;

            double residuos = plasticoReduzido + papelReduzido;
            int arvores = (int) (reducaoCO2 / 20); // ajuste se quiser mais realista
            int carros = (int) (reducaoCO2 / 120);

            resposta.put("co2",CalculadoraMateriais.formatarPeso(reducaoCO2));
            resposta.put("residuos", CalculadoraMateriais.formatarPeso(residuos));
            resposta.put("logistica", CalculadoraMateriais.formatarPeso(logisticaReduzida));
            resposta.put("plastico", CalculadoraMateriais.formatarPeso(plasticoReduzido));
            resposta.put("papel", CalculadoraMateriais.formatarPeso(papelReduzido));

            resposta.put("arvores", Math.max(arvores, 0));
            resposta.put("carros", Math.max(carros, 0));

            return resposta;

        } catch (Exception e) {
            resposta.put("erro", "Erro no cálculo");
            return resposta;
        }
    }

    private double calcularImpacto(int colaboradores, int distanciaEntrega, int porcentagemDigital) {

        double fatorDigital = porcentagemDigital / 100.0;
    
        double impactoFisico = CalculadoraMateriais.calcularLogistica(colaboradores, distanciaEntrega);
    
        // Simulação de impacto digital (menor que físico)
        double impactoDigital = impactoFisico * 0.3;
    
        return impactoFisico * (1 - fatorDigital) +
               impactoDigital * fatorDigital;
    }
}