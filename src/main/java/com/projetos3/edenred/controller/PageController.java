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
            @RequestParam(required = false, defaultValue = "0") Integer colaboradores,
            @RequestParam(required = false, defaultValue = "0") Double distanciaEntrega,
            @RequestParam(required = false, defaultValue = "Sua Empresa") String nomeEmpresa,
            Model model) {
        
        // Passamos os dados base para a tela. O JavaScript vai usar isso no slider.
        model.addAttribute("colaboradores", colaboradores);
        model.addAttribute("distanciaEntrega", distanciaEntrega);
        model.addAttribute("nomeEmpresa", nomeEmpresa);
        
        return "simulacao";
    }

    @PostMapping("/simular-ajax")
    @ResponseBody
    public Map<String, Object> simularAjax(
            @RequestParam(required = false, defaultValue = "0") Integer colaboradores,
            @RequestParam(required = false, defaultValue = "0") Double distanciaEntrega,
            @RequestParam(required = false, defaultValue = "0") Integer porcentagemAlvo) {

        Map<String, Object> resposta = new HashMap<>();

        try {
            if (porcentagemAlvo < 0 || porcentagemAlvo > 100) {
                resposta.put("erro", "Porcentagem deve estar entre 0 e 100");
                return resposta;
            }
            
            if (colaboradores == null || colaboradores <= 0 || porcentagemAlvo == 0) {
                resposta.put("co2", "0 kg");
                resposta.put("residuos", "0 kg");
                resposta.put("logistica", "0 kg");
                resposta.put("plastico", "0 kg");
                resposta.put("papel", "0 kg");
                resposta.put("arvores", 0);
                resposta.put("carros", 0);
                return resposta;
            }

            // Quantos cartões físicos deixarão de existir com essa nova porcentagem?
            int cartoesConvertidos = (colaboradores * porcentagemAlvo) / 100;

            double plastico = CalculadoraMateriais.calcularPlastico(cartoesConvertidos);
            double papel = CalculadoraMateriais.calcularPapel(cartoesConvertidos);
            double producao = CalculadoraCO2.calcularProducao(cartoesConvertidos);
            double transporte = CalculadoraMateriais.calcularLogistica(cartoesConvertidos, distanciaEntrega);
            double descarte = CalculadoraCO2.calcularDescarte(cartoesConvertidos);
            
            double totalCO2 = producao + transporte + descarte;
            double residuos = plastico + papel;

            // Fórmulas de equivalência (Ajuste os divisores conforme sua regra de negócio real)
            int arvoresPlantadas = (int) Math.ceil(totalCO2 / 20.0); // Ex: 1 árvore a cada 20kg
            int mesesCarro = (int) Math.ceil(totalCO2 / 100.0);      // Ex: 1 mês de carro a cada 100kg

            resposta.put("co2", CalculadoraMateriais.formatarPeso(totalCO2));
            resposta.put("residuos", CalculadoraMateriais.formatarPeso(residuos));
            resposta.put("logistica", CalculadoraMateriais.formatarPeso(transporte));
            resposta.put("plastico", CalculadoraMateriais.formatarPeso(plastico));
            resposta.put("papel", CalculadoraMateriais.formatarPeso(papel));
            resposta.put("arvores", arvoresPlantadas);
            resposta.put("carros", mesesCarro);

            return resposta;

        } catch (Exception e) {
            resposta.put("erro", "Erro no cálculo");
            return resposta;
        }
    }
}