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
public class LoginController {

    @GetMapping("/login")
    public String telaLogin(HttpSession session) {
        if (session.getAttribute("admin") != null && (Boolean) session.getAttribute("admin")) {
            return "redirect:/admin";
        }
        if (session.getAttribute("empresaLogada") != null) {
            return "redirect:/impacto";
        }
        return "login";
    }

    @PostMapping("/login")
    public String fazerLogin(@RequestParam String cnpj,
                             @RequestParam String senha,
                             HttpSession session,
                             Model model) {

        // Primeiro verifica se é o admin Edenred
        if (BancoEmMemoria.autenticarAdmin(cnpj, senha)) {
            session.setAttribute("admin", true);
            session.setAttribute("empresaLogada", null);
            return "redirect:/admin";
        }

        // Se não é admin, tenta autenticar como empresa
        try {
            Empresa empresa = BancoEmMemoria.autenticar(cnpj, senha);
            session.setAttribute("empresaLogada", empresa);
            session.setAttribute("admin", false);
            return "redirect:/impacto";
        } catch (DadosEmpresaException e) {
            model.addAttribute("erro", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
