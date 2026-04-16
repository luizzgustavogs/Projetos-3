package com.projetos3.edenred.model;

public class CalculadoraMateriais {
    
    private static final double PESO_CARTAO_PLASTICO_GRAMAS = 5.0; 
    private static final double PESO_COMPROVANTE_PAPEL_GRAMAS = 2.0; 
    
    // CRITÉRIO: Definir fator de emissão do transporte (ex: 0.15g de CO2 por km por cartão)
    private static final double FATOR_EMISSAO_TRANSPORTE = 0.15;

    public static double calcularPlastico(int cartoesEvitados) {
        if (cartoesEvitados <= 0) return 0.0;
        return cartoesEvitados * PESO_CARTAO_PLASTICO_GRAMAS;
    }

    public static double calcularPapel(int transacoesDigitais) {
        if (transacoesDigitais <= 0) return 0.0;
        return transacoesDigitais * PESO_COMPROVANTE_PAPEL_GRAMAS;
    }

    // CRITÉRIO: Calcular emissões com base na quantidade e distância
    public static double calcularLogistica(int quantidadeCartoes, double distanciaKm) {
        // CRITÉRIO: Bloquear cálculo em caso de dados inválidos
        if (quantidadeCartoes <= 0 || distanciaKm <= 0) {
            return 0.0;
        }
        // Aplicar fator de emissão do transporte
        return quantidadeCartoes * distanciaKm * FATOR_EMISSAO_TRANSPORTE;
    }

    public static String formatarPeso(double gramas) {
        if (gramas >= 1000) {
            return String.format("%.2f kg", gramas / 1000);
        }
        return String.format("%.1f g", gramas);
    }
}