package com.projetos3.edenred.controller;

import com.projetos3.edenred.dados.BancoEmMemoria;
import com.projetos3.edenred.model.Empresa;
import com.projetos3.edenred.model.DadosEmpresaException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    // Verifica se o usuário é admin
    private boolean isAdmin(HttpSession session) {
        Boolean admin = (Boolean) session.getAttribute("admin");
        return admin != null && admin;
    }

    @GetMapping("/admin")
    public String telaAdmin(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        popularDadosAdmin(model);
        return "admin";
    }

    private void popularDadosAdmin(Model model) {
        model.addAttribute("empresas", BancoEmMemoria.listarEmpresas());
        
        long totalEmpresas = BancoEmMemoria.listarEmpresas().size();
        long totalColaboradores = BancoEmMemoria.listarEmpresas().stream()
                .mapToLong(Empresa::getColaboradores).sum();
        
        model.addAttribute("totalEmpresas", totalEmpresas);
        model.addAttribute("totalColaboradores", totalColaboradores);
    }

    @PostMapping("/admin/cadastrar")
    public String cadastrarEmpresa(@RequestParam String cnpj,
                                   @RequestParam String nome,
                                   @RequestParam String senha,
                                   @RequestParam int colaboradores,
                                   @RequestParam int numeroBeneficios,
                                   @RequestParam(required = false, defaultValue = "false") boolean multibeneficio,
                                   @RequestParam double vidaUtilCartaoAnos,
                                   @RequestParam double taxaTurnover,
                                   @RequestParam double taxaReemissao,
                                   @RequestParam int transacoesMensais,
                                   @RequestParam double porcentagemDigitalAtual,
                                   HttpSession session,
                                   Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        try {
            Empresa empresa = new Empresa(
                cnpj, nome, senha, colaboradores,
                numeroBeneficios, multibeneficio,
                vidaUtilCartaoAnos, taxaTurnover,
                taxaReemissao, transacoesMensais,
                porcentagemDigitalAtual
            );
            BancoEmMemoria.cadastrarEmpresa(empresa);
            model.addAttribute("sucesso", "Empresa '" + nome + "' cadastrada com sucesso!");
            popularDadosAdmin(model);
        } catch (DadosEmpresaException e) {
            model.addAttribute("erro", e.getMessage());
            popularDadosAdmin(model);
        }

        return "admin";
    }

    @PostMapping("/admin/remover")
    public String removerEmpresa(@RequestParam String cnpj, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        BancoEmMemoria.removerEmpresa(cnpj);
        model.addAttribute("sucesso", "Empresa removida com sucesso!");
        
        popularDadosAdmin(model);
        return "admin";
    }
}
