package lanchonete;

import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private Cliente cliente;
    private List<Produto> itens;
    private double taxaEntrega;

    public Pedido(Cliente cliente, double taxaEntrega) {
        this.cliente = cliente;
        this.taxaEntrega = taxaEntrega;
        this.itens = new ArrayList<>();
    }

    public void adicionarItem(Produto produto) {
        itens.add(produto);
        System.out.println("✔ \"" + produto.getNome() + "\" adicionado ao carrinho!");
    }

    public void verCarrinho() {
        if (itens.isEmpty()) {
            System.out.println("Carrinho vazio.");
            return;
        }
        System.out.println("\n--- Seu carrinho ---");
        double subtotal = 0;
        for (Produto p : itens) {
            System.out.println("  -> " + p.getDescricao());
            subtotal += p.getPreco();
        }
        System.out.printf("Subtotal: R$ %.2f%n", subtotal);
        System.out.println("--------------------");
    }

    public double calcularTotal() {
        double subtotal = 0;
        for (Produto produto : itens) {
            subtotal += produto.getPreco();
        }
        if (subtotal > 50.0) {
            double desconto = subtotal * 0.10;
            subtotal = subtotal - desconto;
            System.out.printf("Desconto de 10%% aplicado! Voce economizou: R$ %.2f%n", desconto);
        }
        return subtotal + taxaEntrega;
    }

    public void exibirResumo() {
        System.out.println("\n========================================");
        System.out.println("          RESUMO DO PEDIDO              ");
        System.out.println("========================================");
        System.out.println("Cliente : " + cliente.getNome());
        System.out.println("Endereco: " + cliente.getEndereco());
        System.out.println("----------------------------------------");
        System.out.println("ITENS DO PEDIDO:");
        for (Produto produto : itens) {
            System.out.println("  -> " + produto.getDescricao());
        }
        System.out.println("----------------------------------------");
        System.out.printf("Taxa de entrega : R$ %.2f%n", taxaEntrega);
        System.out.printf("TOTAL DO PEDIDO : R$ %.2f%n", calcularTotal());
        System.out.println("========================================\n");
    }

    public boolean estaVazio() { return itens.isEmpty(); }
    public Cliente getCliente() { return cliente; }
    public List<Produto> getItens() { return itens; }
    public double getTaxaEntrega() { return taxaEntrega; }
    public void setTaxaEntrega(double taxaEntrega) { this.taxaEntrega = taxaEntrega; }
}