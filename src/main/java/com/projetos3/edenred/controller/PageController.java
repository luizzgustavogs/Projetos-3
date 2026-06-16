package com.projetos3.edenred.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.projetos3.edenred.model.Empresa;
import com.projetos3.edenred.model.RelatorioDigitalizacao;
import com.projetos3.edenred.repository.RelatorioDigitalizacaoRepository;
import com.projetos3.edenred.service.ImpactoService;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PageController {

    private final ImpactoService impactoService;
    private final RelatorioDigitalizacaoRepository relatorioRepository;

    public PageController(ImpactoService impactoService,
                          RelatorioDigitalizacaoRepository relatorioRepository) {
        this.impactoService = impactoService;
        this.relatorioRepository = relatorioRepository;
    }

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
        model.addAttribute("totalCartoes", calcularCartoesFisicosAtuais(empresa));
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

            return impactoService.calcularImpactoAtual(empresa);
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

    @GetMapping("/relatorio")
    public String relatorio(
            @RequestParam(required = false, defaultValue = "100") Integer porcentagemAlvo,
            HttpSession session,
            Model model) {

        Empresa empresa = getEmpresaLogada(session);
        if (empresa == null) {
            return "redirect:/login";
        }

        int alvoNormalizado = Math.max(0, Math.min(100, porcentagemAlvo));
        double digitalAtual = empresa.getPorcentagemDigitalAtual();
        double ganhoDigital = alvoNormalizado - digitalAtual;
        boolean ganhoPositivo = ganhoDigital > 0;
        boolean ganhoNeutro = ganhoDigital == 0;
        boolean reducaoDigital = ganhoDigital < 0;
        Map<String, Object> simulacao = impactoService.simularImpacto(empresa, alvoNormalizado);
        RelatorioDigitalizacao relatorioSalvo = relatorioRepository.save(
                new RelatorioDigitalizacao(empresa, alvoNormalizado, simulacao));

        int transacoesAnuais = empresa.getColaboradores() * empresa.getTransacoesMensais() * 12;
        String dataGeracao = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        model.addAttribute("empresa", empresa);
        model.addAttribute("nomeEmpresa", empresa.getNome());
        model.addAttribute("digitalAtual", digitalAtual);
        model.addAttribute("digitalAlvo", alvoNormalizado);
        model.addAttribute("ganhoDigital", ganhoDigital);
        model.addAttribute("ganhoPositivo", ganhoPositivo);
        model.addAttribute("ganhoNeutro", ganhoNeutro);
        model.addAttribute("reducaoDigital", reducaoDigital);
        model.addAttribute("simulacao", simulacao);
        model.addAttribute("cartoesAtuais", calcularCartoesFisicosAtuais(empresa));
        model.addAttribute("cartoesPorColaborador", empresa.getCartoesPorColaborador());
        model.addAttribute("transacoesAnuais", transacoesAnuais);
        model.addAttribute("dataGeracao", dataGeracao);
        model.addAttribute("relatorioId", relatorioSalvo.getId());

        return "relatorio";
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

            return impactoService.simularImpacto(empresa, porcentagemAlvo);

        } catch (Exception e) {
            resposta.put("erro", "Erro no cálculo: " + e.getMessage());
            return resposta;
        }
    }

    private int calcularCartoesFisicosAtuais(Empresa empresa) {
        return (int) Math.ceil(empresa.getTotalCartoesAtuais()
                * (1.0 - empresa.getPorcentagemDigitalAtual() / 100.0));
    }
}
