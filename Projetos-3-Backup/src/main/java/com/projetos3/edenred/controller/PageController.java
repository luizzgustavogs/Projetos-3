package com.projetos3.edenred.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.projetos3.edenred.model.Empresa;
import com.projetos3.edenred.model.CalculadoraCO2;
import com.projetos3.edenred.model.CalculadoraMateriais;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    // Verifica se empresa está logada na sessão
    private Empresa getEmpresaLogada(HttpSession session) {
        return (Empresa) session.getAttribute("empresaLogada");
    }

    @GetMapping("/impacto")
    public String impacto(HttpSession session, Model model) {
        Empresa empresa = getEmpresaLogada(session);
        if (empresa == null) {
            return "redirect:/login";
        }

        // Passa os dados da empresa para o template
        model.addAttribute("empresa", empresa);
        model.addAttribute("nomeEmpresa", empresa.getNome());
        model.addAttribute("colaboradores", empresa.getColaboradores());
        model.addAttribute("numeroBeneficios", empresa.getNumeroBeneficios());
        model.addAttribute("multibeneficio", empresa.isMultibeneficio());
        model.addAttribute("cartoesPorColaborador", empresa.getCartoesPorColaborador());
        model.addAttribute("totalCartoes", empresa.getTotalCartoesAtuais());
        model.addAttribute("digitalAtual", empresa.getPorcentagemDigitalAtual());

        return "impacto";
    }

    // Cálculo AJAX para a tela de impacto atual
    @PostMapping("/calcular-ajax")
    @ResponseBody
    public Map<String, String> calcularAjax(HttpSession session) {
        Map<String, String> resposta = new HashMap<>();
        try {
            Empresa empresa = getEmpresaLogada(session);
            if (empresa == null) {
                resposta.put("erro", "Sessão expirada. Faça login novamente.");
                return resposta;
            }

            int totalCartoes = empresa.getTotalCartoesAtuais();
            double porcentagemDigital = empresa.getPorcentagemDigitalAtual();

            // Bloco 1: Impacto dos cartões físicos atuais
            double co2Total = CalculadoraCO2.calcularCO2PorCartoes(totalCartoes);
            double co2Material = CalculadoraCO2.calcularCO2Material(totalCartoes);
            double co2Fabricacao = CalculadoraCO2.calcularCO2Fabricacao(totalCartoes);
            double co2Transporte = CalculadoraCO2.calcularCO2Transporte(totalCartoes);
            double plastico = CalculadoraMateriais.calcularPlastico(totalCartoes);

            resposta.put("co2", CalculadoraMateriais.formatarPeso(co2Total));
            resposta.put("co2Material", CalculadoraMateriais.formatarPeso(co2Material));
            resposta.put("co2Fabricacao", CalculadoraMateriais.formatarPeso(co2Fabricacao));
            resposta.put("co2Transporte", CalculadoraMateriais.formatarPeso(co2Transporte));
            resposta.put("plastico", CalculadoraMateriais.formatarPeso(plastico));
            resposta.put("totalCartoes", CalculadoraMateriais.formatarNumero(totalCartoes));
            resposta.put("digitalAtual", String.valueOf(porcentagemDigital));
            
            // Sinaliza condição ruim se digital < 30%
            resposta.put("condicaoRuim", String.valueOf(porcentagemDigital < 30));

            return resposta;
        } catch (Exception e) {
            resposta.put("erro", "Erro no cálculo: " + e.getMessage());
            return resposta;
        }
    }

    @GetMapping("/simulacao")
    public String simulacao(HttpSession session, Model model) {
        Empresa empresa = getEmpresaLogada(session);
        if (empresa == null) {
            return "redirect:/login";
        }

        model.addAttribute("empresa", empresa);
        model.addAttribute("nomeEmpresa", empresa.getNome());
        model.addAttribute("digitalAtual", empresa.getPorcentagemDigitalAtual());

        
        return "simulacao";
    }

    @PostMapping("/simular-ajax")
    @ResponseBody
    public Map<String, Object> simularAjax(
            @RequestParam(required = false, defaultValue = "0") Integer porcentagemAlvo,
            HttpSession session) {

        Map<String, Object> resposta = new HashMap<>();

        try {
            Empresa empresa = getEmpresaLogada(session);
            if (empresa == null) {
                resposta.put("erro", "Sessão expirada. Faça login novamente.");
                return resposta;
            }

            double atual = empresa.getPorcentagemDigitalAtual();
            double diff = porcentagemAlvo - atual; // positivo = melhora, negativo = piora

            resposta.put("porcentagemAlvo", porcentagemAlvo);
            resposta.put("porcentagemAtual", atual);
            resposta.put("piorou", diff < 0);

            double diffAbs = Math.abs(diff);
            int colaboradores = empresa.getColaboradores();
            int cartoesPorColab = empresa.getCartoesPorColaborador();

            // Cartões envolvidos na mudança
            int cartoesMudança = (int) Math.ceil((colaboradores * cartoesPorColab) * (diffAbs / 100.0));

            // Bloco 1: Impacto da mudança nos cartões
            double co2Cartoes = CalculadoraCO2.calcularCO2PorCartoes(cartoesMudança);
            double plastico = CalculadoraMateriais.calcularPlastico(cartoesMudança);

            // Bloco 2: Impacto nas transações
            int transacoesAnuais = CalculadoraMateriais.calcularTransacoesAnuais(
                colaboradores, empresa.getTransacoesMensais());
            int transacoesMudanca = (int) Math.ceil(transacoesAnuais * (diffAbs / 100.0));
            double co2Transacional = CalculadoraCO2.calcularCO2PorTransacoes(transacoesMudanca);
            double papel = CalculadoraMateriais.calcularPapel(transacoesMudanca);

            double co2TotalMudanca = co2Cartoes + co2Transacional;
            double residuosMudanca = plastico + papel;

            // Projeção Anual ESG (baseada no novo estado alvo)
            int cartoesFuturoPorColab = (int) Math.ceil(cartoesPorColab * (1.0 - porcentagemAlvo / 100.0));
            int cartoesAnuaisNovoCenario = CalculadoraCO2.calcularCartoesEvitadosPorAno(
                colaboradores, cartoesPorColab, cartoesFuturoPorColab,
                empresa.getVidaUtilCartaoAnos(), empresa.getTaxaTurnover(), empresa.getTaxaReemissao());
            
            double co2AnualTotal = CalculadoraCO2.calcularCO2AnualEvitado(cartoesAnuaisNovoCenario);

            int arvores = CalculadoraCO2.calcularArvoresEquivalentes(co2TotalMudanca);
            int kmCarro = CalculadoraCO2.calcularKmCarroEquivalente(co2TotalMudanca);

            // ==== CÁLCULOS PARA A TABELA COMPARATIVA ====
            int maxCartoes = colaboradores * cartoesPorColab;
            
            // Cenário Atual (Baseado na porcentagem digital atual)
            int cartoesAtuais = (int) Math.ceil(maxCartoes * (1.0 - atual / 100.0));
            int transAtual = (int) Math.ceil(transacoesAnuais * (1.0 - atual / 100.0));
            double co2AtualCartoes = CalculadoraCO2.calcularCO2PorCartoes(cartoesAtuais);
            double plasticoAtual = CalculadoraMateriais.calcularPlastico(cartoesAtuais);
            double co2AtualTrans = CalculadoraCO2.calcularCO2PorTransacoes(transAtual);
            double papelAtual = CalculadoraMateriais.calcularPapel(transAtual);

            // Cenário Simulado (Baseado na nova porcentagem alvo)
            int cartoesSim = (int) Math.ceil(maxCartoes * (1.0 - porcentagemAlvo / 100.0));
            int transSim = (int) Math.ceil(transacoesAnuais * (1.0 - porcentagemAlvo / 100.0));
            double co2SimCartoes = CalculadoraCO2.calcularCO2PorCartoes(cartoesSim);
            double plasticoSim = CalculadoraMateriais.calcularPlastico(cartoesSim);
            double co2SimTrans = CalculadoraCO2.calcularCO2PorTransacoes(transSim);
            double papelSim = CalculadoraMateriais.calcularPapel(transSim);

            resposta.put("co2", CalculadoraMateriais.formatarPeso(co2Cartoes));
            resposta.put("co2Transacional", CalculadoraMateriais.formatarPeso(co2Transacional));
            resposta.put("plastico", CalculadoraMateriais.formatarPeso(plastico));
            resposta.put("papel", CalculadoraMateriais.formatarPeso(papel));
            resposta.put("residuos", CalculadoraMateriais.formatarPeso(residuosMudanca));
            resposta.put("cartoesAfetados", CalculadoraMateriais.formatarNumero(cartoesMudança));
            
            // Valores para a Tabela Comparativa
            resposta.put("tabCo2Atual", CalculadoraMateriais.formatarPeso(co2AtualCartoes));
            resposta.put("tabCo2Sim", CalculadoraMateriais.formatarPeso(co2SimCartoes));
            resposta.put("tabPlasAtual", CalculadoraMateriais.formatarPeso(plasticoAtual));
            resposta.put("tabPlasSim", CalculadoraMateriais.formatarPeso(plasticoSim));
            resposta.put("tabPapelAtual", CalculadoraMateriais.formatarPeso(papelAtual));
            resposta.put("tabPapelSim", CalculadoraMateriais.formatarPeso(papelSim));
            resposta.put("tabResAtual", CalculadoraMateriais.formatarPeso(plasticoAtual + papelAtual));
            resposta.put("tabResSim", CalculadoraMateriais.formatarPeso(plasticoSim + papelSim));
            resposta.put("tabLogAtual", CalculadoraMateriais.formatarPeso(co2AtualTrans));
            resposta.put("tabLogSim", CalculadoraMateriais.formatarPeso(co2SimTrans));
            resposta.put("co2AnualTotal", CalculadoraMateriais.formatarPeso(co2AnualTotal));
            resposta.put("arvores", arvores);
            resposta.put("km", kmCarro);

            return resposta;

        } catch (Exception e) {
            resposta.put("erro", "Erro no cálculo: " + e.getMessage());
            return resposta;
        }
    }
}