package com.projetos3.edenred.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CalculadoraCO2Test {

    @Test
    public void deveriaCalcularProducaoCorretamente() {
        double resultado = CalculadoraCO2.calcularProducao(2);
        assertEquals(30.0, resultado);
    }

    @Test
    public void deveriaCalcularTransporteCorretamente() {
        double resultado = CalculadoraCO2.calcularTransporte(10);
        assertEquals(50.0, resultado);
    }

    @Test
    public void deveriaCalcularDescarteCorretamente() {
        double resultado = CalculadoraCO2.calcularDescarte(5);
        assertEquals(5.0, resultado);
    }

    @Test
    public void deveriaCalcularEmissaoTotalCorretamente() {
        double resultado = CalculadoraCO2.calcularEmissaoTotal(2);
        assertEquals(42.0, resultado);
    }
}