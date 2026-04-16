package com.projetos3.edenred.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.projetos3.edenred.model.CalculadoraCO2; 
import com.projetos3.edenred.model.CalculadoraMateriais;

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
            @RequestParam(required = false, defaultValue = "0") Double distanciaEntrega, // NOVO CAMPO
            Model model) {

        try {
            // CRITÉRIO: Verificar se existem dados suficientes e validar distância
            if (colaboradores == null || colaboradores <= 0) {
                model.addAttribute("mensagemErro", "Erro ao calcular emissões: Dados inválidos ou ausentes.");
                return "impacto"; 
            }
            
            // CRITÉRIO: Bloquear cálculo em caso de dados inválidos (Distância)
            if (distanciaEntrega == null || distanciaEntrega <= 0) {
                model.addAttribute("mensagemErro", "Erro no cálculo logístico: a distância deve ser maior que zero.");
                return "impacto";
            }

            int percentualFisico = 100 - porcentagemDigitais;
            int cartoesFisicos = (colaboradores * percentualFisico) / 100;
            int cartoesDigitais = colaboradores - cartoesFisicos;

            // Cálculos de Materiais
            double qtdPlastico = CalculadoraMateriais.calcularPlastico(cartoesDigitais);
            double qtdPapel = CalculadoraMateriais.calcularPapel(cartoesDigitais);

            String plasticoFormatado = CalculadoraMateriais.formatarPeso(qtdPlastico);
            String papelFormatado = CalculadoraMateriais.formatarPeso(qtdPapel);

            // Cálculos de CO2 (Logística incluída)
            double producao = CalculadoraCO2.calcularProducao(cartoesFisicos);
            
            // CRITÉRIO: Aplicar fator de emissão do transporte e gerar resultado
            // Aqui usamos a nova função que criamos na CalculadoraMateriais
            double transporte = CalculadoraMateriais.calcularLogistica(cartoesFisicos, distanciaEntrega);
            
            double descarte = CalculadoraCO2.calcularDescarte(cartoesFisicos);
            double totalCO2 = producao + transporte + descarte;

            // CRITÉRIO: Exibir mensagem de sucesso ("Impacto logístico calculado com sucesso")
            model.addAttribute("mensagemSucesso", "Impacto logístico calculado com sucesso!");
            
            // Atributos para a tela de resultado
            model.addAttribute("qtdCartoesFisicos", cartoesFisicos);
            model.addAttribute("qtdCartoesDigitais", cartoesDigitais);
            model.addAttribute("plasticoEconomizado", plasticoFormatado);
            model.addAttribute("papelEconomizado", papelFormatado);

            model.addAttribute("emissaoProducao", producao);
            model.addAttribute("emissaoTransporte", transporte); // Exibe resultado ao usuário
            model.addAttribute("emissaoDescarte", descarte);
            model.addAttribute("emissoesCO2", totalCO2);

            return "resultado";

        } catch (Exception e) {
            // CRITÉRIO: Tratar erro no cálculo e exibir mensagem
            model.addAttribute("mensagemErro", "Erro no cálculo logístico.");
            return "impacto";
        }
    }
}