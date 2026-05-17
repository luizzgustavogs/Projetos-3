package com.projetos3.edenred.model;

public class Empresa {

    private String cnpj;
    private String nome;
    private String senha;
    private int colaboradores;
    private int numeroBeneficios;       // quantos tipos de benefício (VA, VR, VT, combustível...)
    private boolean multibeneficio;     // true = 1 cartão para todos os benefícios
    private double vidaUtilCartaoAnos;  // V (ex: 3.5 anos)
    private double taxaTurnover;        // T (ex: 0.20 = 20% ao ano)
    private double taxaReemissao;       // R (ex: 0.05 = 5% ao ano)
    private int transacoesMensais;      // média de transações por colaborador por mês
    private double porcentagemDigitalAtual; // Novo campo: 0 a 100

    // Construtor vazio
    public Empresa() {
    }

    // Construtor completo
    public Empresa(String cnpj, String nome, String senha, int colaboradores,
                   int numeroBeneficios, boolean multibeneficio,
                   double vidaUtilCartaoAnos, double taxaTurnover,
                   double taxaReemissao, int transacoesMensais,
                   double porcentagemDigitalAtual) {
        this.cnpj = cnpj;
        this.nome = nome;
        this.senha = senha;
        this.colaboradores = colaboradores;
        this.numeroBeneficios = numeroBeneficios;
        this.multibeneficio = multibeneficio;
        this.vidaUtilCartaoAnos = vidaUtilCartaoAnos;
        this.taxaTurnover = taxaTurnover;
        this.taxaReemissao = taxaReemissao;
        this.transacoesMensais = transacoesMensais;
        this.porcentagemDigitalAtual = porcentagemDigitalAtual;
    }

    // Retorna quantos cartões físicos cada colaborador tem HOJE
    // Se multibenefício = true → 1 cartão por colaborador
    // Se multibenefício = false → 1 cartão por benefício
    public int getCartoesPorColaborador() {
        if (multibeneficio) {
            return 1;
        } else {
            return numeroBeneficios;
        }
    }

    // Retorna o total de cartões físicos atuais da empresa
    public int getTotalCartoesAtuais() {
        return colaboradores * getCartoesPorColaborador();
    }

    // Getters e Setters

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getColaboradores() {
        return colaboradores;
    }

    public void setColaboradores(int colaboradores) {
        this.colaboradores = colaboradores;
    }

    public int getNumeroBeneficios() {
        return numeroBeneficios;
    }

    public void setNumeroBeneficios(int numeroBeneficios) {
        this.numeroBeneficios = numeroBeneficios;
    }

    public boolean isMultibeneficio() {
        return multibeneficio;
    }

    public void setMultibeneficio(boolean multibeneficio) {
        this.multibeneficio = multibeneficio;
    }

    public double getVidaUtilCartaoAnos() {
        return vidaUtilCartaoAnos;
    }

    public void setVidaUtilCartaoAnos(double vidaUtilCartaoAnos) {
        this.vidaUtilCartaoAnos = vidaUtilCartaoAnos;
    }

    public double getTaxaTurnover() {
        return taxaTurnover;
    }

    public void setTaxaTurnover(double taxaTurnover) {
        this.taxaTurnover = taxaTurnover;
    }

    public double getTaxaReemissao() {
        return taxaReemissao;
    }

    public void setTaxaReemissao(double taxaReemissao) {
        this.taxaReemissao = taxaReemissao;
    }

    public int getTransacoesMensais() {
        return transacoesMensais;
    }

    public void setTransacoesMensais(int transacoesMensais) {
        this.transacoesMensais = transacoesMensais;
    }

    public double getPorcentagemDigitalAtual() {
        return porcentagemDigitalAtual;
    }

    public void setPorcentagemDigitalAtual(double porcentagemDigitalAtual) {
        this.porcentagemDigitalAtual = porcentagemDigitalAtual;
    }
}
