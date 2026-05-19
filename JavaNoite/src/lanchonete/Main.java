package lanchonete;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("   SISTEMA DE PEDIDOS - LANCHONETE     ");
        System.out.println("========================================");

        // --- Dados do cliente ---
        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine();

        System.out.print("Digite seu endereco: ");
        String endereco = scanner.nextLine();

        Cliente cliente = new Cliente(nome, endereco);
        Pedido pedido = new Pedido(cliente, 5.00);

        // --- Cardápio disponível ---
        Lanche[] lanches = {
                new Lanche("X-Burguer",  25.00, "Pao, carne, queijo, alface, tomate"),
                new Lanche("X-Bacon",    28.00, "Pao, carne dupla, bacon, queijo"),
                new Lanche("X-Salada",   22.00, "Pao, frango, alface, tomate, maionese"),
                new Lanche("X-Tudo",     35.00, "Pao, carne, bacon, ovo, queijo, alface")
        };

        Bebida[] bebidas = {
                new Bebida("Coca-Cola",   8.00, 350),
                new Bebida("Suco de Laranja", 7.00, 300),
                new Bebida("Agua Mineral", 4.00, 500),
                new Bebida("Guarana",      7.00, 350)
        };

        // --- Menu principal ---
        int opcao = 0;
        while (opcao != 4) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Ver cardapio e adicionar item");
            System.out.println("2. Ver carrinho");
            System.out.println("3. Finalizar pedido");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opcao: ");

            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    // Mostra categorias
                    System.out.println("\n--- CATEGORIAS ---");
                    System.out.println("1. Lanches");
                    System.out.println("2. Bebidas");
                    System.out.print("Escolha a categoria: ");
                    int categoria = scanner.nextInt();

                    if (categoria == 1) {
                        System.out.println("\n--- LANCHES ---");
                        for (int i = 0; i < lanches.length; i++) {
                            System.out.println((i + 1) + ". " + lanches[i].getDescricao());
                        }
                        System.out.print("Escolha o numero do lanche (0 para cancelar): ");
                        int escolha = scanner.nextInt();
                        if (escolha >= 1 && escolha <= lanches.length) {
                            pedido.adicionarItem(lanches[escolha - 1]);
                        }

                    } else if (categoria == 2) {
                        System.out.println("\n--- BEBIDAS ---");
                        for (int i = 0; i < bebidas.length; i++) {
                            System.out.println((i + 1) + ". " + bebidas[i].getDescricao());
                        }
                        System.out.print("Escolha o numero da bebida (0 para cancelar): ");
                        int escolha = scanner.nextInt();
                        if (escolha >= 1 && escolha <= bebidas.length) {
                            pedido.adicionarItem(bebidas[escolha - 1]);
                        }
                    }
                    break;

                case 2:
                    pedido.verCarrinho();
                    break;

                case 3:
                    if (pedido.estaVazio()) {
                        System.out.println("Seu carrinho esta vazio! Adicione itens antes de finalizar.");
                    } else {
                        pedido.exibirResumo();
                        System.out.println("Pedido realizado com sucesso! Obrigado, " + cliente.getNome() + "!");
                        opcao = 4; // encerra o loop
                    }
                    break;

                case 4:
                    System.out.println("Saindo... Ate logo!");
                    break;

                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }
        }

        scanner.close();
    }
}