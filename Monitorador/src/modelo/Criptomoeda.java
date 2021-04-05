package modelo;

import java.math.BigDecimal;

/**
 *
 * @author RAS - DESKTOP
 */

public class Criptomoeda {
    private BigDecimal maiorPreco;
    private BigDecimal menorPreco;
    private BigDecimal volume;
    private BigDecimal ultimoPreco;
    private BigDecimal precoCompra;
    private BigDecimal precoVenda;
    private Integer data;
    
    // Getters and Setters

    public BigDecimal getMaiorPreco() {
        return maiorPreco;
    }

    public void setMaiorPreco(BigDecimal maiorPreco) {
        this.maiorPreco = maiorPreco;
    }

    public BigDecimal getMenorPreco() {
        return menorPreco;
    }

    public void setMenorPreco(BigDecimal menorPreco) {
        this.menorPreco = menorPreco;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getUltimoPreco() {
        return ultimoPreco;
    }

    public void setUltimoPreco(BigDecimal ultimoPreco) {
        this.ultimoPreco = ultimoPreco;
    }

    public BigDecimal getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(BigDecimal precoCompra) {
        this.precoCompra = precoCompra;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }
    
}
