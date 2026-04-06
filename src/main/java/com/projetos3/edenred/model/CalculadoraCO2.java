package com.projetos3.edenred.model;

public class CalculadoraCO2 {

    // Fatores de emissão em gramas de CO2 por cartão
    private static final double FATOR_PRODUCAO = 15.0; 
    private static final double FATOR_TRANSPORTE = 5.0; 
    private static final double FATOR_DESCARTE = 1.0;   

    public static double calcularProducao(int cartoesFisicos) {
        return cartoesFisicos * FATOR_PRODUCAO;
    }

    public static double calcularTransporte(int cartoesFisicos) {
        return cartoesFisicos * FATOR_TRANSPORTE;
    }

    public static double calcularDescarte(int cartoesFisicos) {
        return cartoesFisicos * FATOR_DESCARTE;
    }

    public static double calcularEmissaoTotal(int cartoesFisicos) {
        return calcularProducao(cartoesFisicos) + calcularTransporte(cartoesFisicos) + calcularDescarte(cartoesFisicos);
    }
}