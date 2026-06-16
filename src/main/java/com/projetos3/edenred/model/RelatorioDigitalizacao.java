package com.projetos3.edenred.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RelatorioDigitalizacao {

    private Long id;

    private String empresaCnpj;

    private String empresaNome;

    private LocalDateTime dataGeracao;

    private double digitalAtual;

    private int digitalEscolhido;

    private String cartoesAfetados;

    private String co2Cartoes;

    private String co2Transacional;

    private String residuos;

    private String plastico;

    private String papel;

    private int arvoresEquivalentes;

    public RelatorioDigitalizacao() {
    }

    public RelatorioDigitalizacao(Empresa empresa, int digitalEscolhido,
                                  java.util.Map<String, Object> simulacao) {
        this.empresaCnpj = empresa.getCnpj();
        this.empresaNome = empresa.getNome();
        this.dataGeracao = LocalDateTime.now();
        this.digitalAtual = empresa.getPorcentagemDigitalAtual();
        this.digitalEscolhido = digitalEscolhido;
        this.cartoesAfetados = String.valueOf(simulacao.get("cartoesAfetados"));
        this.co2Cartoes = String.valueOf(simulacao.get("co2"));
        this.co2Transacional = String.valueOf(simulacao.get("co2Transacional"));
        this.residuos = String.valueOf(simulacao.get("residuos"));
        this.plastico = String.valueOf(simulacao.get("plastico"));
        this.papel = String.valueOf(simulacao.get("papel"));
        this.arvoresEquivalentes = (Integer) simulacao.getOrDefault("arvores", 0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmpresaCnpj() {
        return empresaCnpj;
    }

    public String getEmpresaNome() {
        return empresaNome;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public String getDataGeracaoFormatada() {
        return dataGeracao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public double getDigitalAtual() {
        return digitalAtual;
    }

    public int getDigitalEscolhido() {
        return digitalEscolhido;
    }

    public double getGanhoDigital() {
        return digitalEscolhido - digitalAtual;
    }

    public String getCartoesAfetados() {
        return cartoesAfetados;
    }

    public String getCo2Cartoes() {
        return co2Cartoes;
    }

    public String getCo2Transacional() {
        return co2Transacional;
    }

    public String getResiduos() {
        return residuos;
    }

    public String getPlastico() {
        return plastico;
    }

    public String getPapel() {
        return papel;
    }

    public int getArvoresEquivalentes() {
        return arvoresEquivalentes;
    }
}
