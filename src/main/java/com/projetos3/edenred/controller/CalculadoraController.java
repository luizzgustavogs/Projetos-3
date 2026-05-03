package com.projetos3.edenred.controller;

import com.projetos3.edenred.model.CalculadoraCO2;
import com.projetos3.edenred.model.CalculadoraMateriais;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/calculadora")
public class CalculadoraController {

    @PostMapping("/impacto")
    public Map<String, String> calcularImpacto(@RequestBody Map<String, Double> dados) {
        int colaboradores = dados.getOrDefault("colaboradores", 0.0).intValue();
        double porcentagemDigitais = dados.getOrDefault("porcentagemDigitais", 0.0);

        // Se 100% é digital, cartões físicos = 0. Senão, calcula a proporção.
        int cartoesFisicos = (int) (colaboradores * (1.0 - (porcentagemDigitais / 100.0)));

        // Usando as suas classes para calcular
        double co2Gramas = CalculadoraCO2.calcularEmissaoTotal(cartoesFisicos);
        double plasticoGramas = CalculadoraMateriais.calcularPlastico(cartoesFisicos);
        double logisticaGramas = CalculadoraMateriais.calcularLogistica(cartoesFisicos);

        // Devolvendo os dados formatados em JSON para o frontend
        Map<String, String> resultados = new HashMap<>();
        resultados.put("co2", CalculadoraMateriais.formatarPeso(co2Gramas));
        resultados.put("plastico", CalculadoraMateriais.formatarPeso(plasticoGramas));
        resultados.put("logistica", CalculadoraMateriais.formatarPeso(logisticaGramas));

        return resultados;
    }
}