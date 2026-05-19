package ControleAcesso;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    // lista com todos os usuarios cadastrados
    static ArrayList<Usuario> usuarios = new ArrayList<>();

    // historico de acessos, cada entrada é uma linha de texto com as infos
    static ArrayList<String> historico = new ArrayList<>();

    // areas do sistema e o nivel minimo pra entrar em cada uma
    // posicao 0 = recepcao (nivel 1), posicao 1 = escritorio (nivel 2), etc.
    static String[] nomesAreas = {"Recepcao", "Escritorio", "Laboratorio", "Sala de Servidores", "Diretoria"};
    static int[] nivelMinimoArea = {1, 2, 2, 3, 3};

    public static void main(String[] args) {

        // cadastra alguns usuarios pra comecar o sistema
        usuarios.add(new Usuario("admin", "Administrador", "admin123", 3));
        usuarios.add(new Usuario("joao", "Joao Silva", "joao123", 2));
        usuarios.add(new Usuario("maria", "Maria Souza", "maria123", 1));

        System.out.println("=== SISTEMA DE CONTROLE DE ACESSO ===");

        // fica rodando até o usuario escolher sair
        boolean rodando = true;
        while (rodando) {

            System.out.println("\n1 - Fazer login");
            System.out.println("0 - Sair do sistema");
            System.out.print("Opcao: ");
            String op = sc.nextLine();

            if (op.equals("1")) {
                Usuario logado = fazerLogin();
                if (logado != null) {
                    // direciona pro menu certo de acordo com o nivel
                    if (logado.getNivel() == 3) {
                        menuAdmin(logado);
                    } else {
                        menuUsuario(logado);
                    }
                }
            } else if (op.equals("0")) {
                rodando = false;
                System.out.println("Sistema encerrado.");
            } else {
                System.out.println("Opcao invalida.");
            }
        }
    }

    // tenta autenticar, devolve o Usuario se der certo ou null se falhar
    static Usuario fazerLogin() {
        System.out.print("\nID: ");
        String id = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            if (u.getId().equals(id) && u.verificarSenha(senha)) {
                System.out.println("Bem-vindo, " + u.getNome() + " [" + u.getNivelTexto() + "]");
                return u;
            }
        }

        System.out.println("ID ou senha incorretos.");
        return null;
    }

    // menu pra visitante e funcionario
    static void menuUsuario(Usuario u) {
        boolean ativo = true;
        while (ativo) {
            System.out.println("\n-- MENU --");
            System.out.println("1 - Acessar area");
            System.out.println("2 - Ver meu historico");
            System.out.println("0 - Logout");
            System.out.print("Opcao: ");
            String op = sc.nextLine();

            if (op.equals("1")) {
                acessarArea(u);
            } else if (op.equals("2")) {
                verHistoricoDoUsuario(u.getId());
            } else if (op.equals("0")) {
                ativo = false;
            } else {
                System.out.println("Opcao invalida.");
            }
        }
    }

    // menu so pra admin
    static void menuAdmin(Usuario u) {
        boolean ativo = true;
        while (ativo) {
            System.out.println("\n-- MENU ADMIN --");
            System.out.println("1 - Acessar area");
            System.out.println("2 - Cadastrar usuario");
            System.out.println("3 - Editar usuario");
            System.out.println("4 - Remover usuario");
            System.out.println("5 - Listar usuarios");
            System.out.println("6 - Ver historico completo");
            System.out.println("0 - Logout");
            System.out.print("Opcao: ");
            String op = sc.nextLine();

            if (op.equals("1")) {
                acessarArea(u);
            } else if (op.equals("2")) {
                cadastrarUsuario();
            } else if (op.equals("3")) {
                editarUsuario();
            } else if (op.equals("4")) {
                removerUsuario(u);
            } else if (op.equals("5")) {
                listarUsuarios();
            } else if (op.equals("6")) {
                verHistoricoCompleto();
            } else if (op.equals("0")) {
                ativo = false;
            } else {
                System.out.println("Opcao invalida.");
            }
        }
    }

    // mostra as areas e tenta liberar o acesso
    static void acessarArea(Usuario u) {
        System.out.println("\nAreas disponiveis:");
        for (int i = 0; i < nomesAreas.length; i++) {
            System.out.println((i + 1) + " - " + nomesAreas[i]);
        }
        System.out.print("Escolha a area: ");

        int escolha;
        try {
            escolha = Integer.parseInt(sc.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Entrada invalida.");
            return;
        }

        if (escolha < 0 || escolha >= nomesAreas.length) {
            System.out.println("Area inexistente.");
            return;
        }

        String nomeArea = nomesAreas[escolha];
        boolean permitido = u.getNivel() >= nivelMinimoArea[escolha];

        // monta a linha do historico com data/hora atual
        String agora = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        String resultado = permitido ? "AUTORIZADO" : "NEGADO";
        String registro = agora + " | " + u.getNome() + " (" + u.getId() + ") | " + nomeArea + " | " + resultado;
        historico.add(registro);

        if (permitido) {
            System.out.println("Acesso AUTORIZADO - " + nomeArea);
        } else {
            System.out.println("Acesso NEGADO - seu nivel nao permite entrar em " + nomeArea);
        }
    }

    static void cadastrarUsuario() {
        System.out.print("Novo ID: ");
        String id = sc.nextLine();

        // checa se o id ja existe
        for (Usuario u : usuarios) {
            if (u.getId().equals(id)) {
                System.out.println("Ja existe um usuario com esse ID.");
                return;
            }
        }

        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        int nivel = escolherNivel();
        if (nivel == -1) return;

        usuarios.add(new Usuario(id, nome, senha, nivel));
        System.out.println("Usuario cadastrado com sucesso!");
    }

    static void editarUsuario() {
        listarUsuarios();
        System.out.print("ID do usuario a editar: ");
        String id = sc.nextLine();

        Usuario encontrado = buscarPorId(id);
        if (encontrado == null) {
            System.out.println("Usuario nao encontrado.");
            return;
        }

        System.out.print("Novo nome (enter pra manter): ");
        String nome = sc.nextLine();
        if (!nome.isBlank()) {
            encontrado.setNome(nome);
        }

        System.out.print("Nova senha (enter pra manter): ");
        String senha = sc.nextLine();
        if (!senha.isBlank()) {
            encontrado.setSenha(senha);
        }

        System.out.print("Novo nivel (enter pra manter): ");
        String nivelStr = sc.nextLine();
        if (!nivelStr.isBlank()) {
            try {
                int novoNivel = Integer.parseInt(nivelStr);
                if (novoNivel >= 1 && novoNivel <= 3) {
                    encontrado.setNivel(novoNivel);
                } else {
                    System.out.println("Nivel invalido, mantendo o atual.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida, mantendo o nivel atual.");
            }
        }

        System.out.println("Usuario atualizado.");
    }

    static void removerUsuario(Usuario quemPediu) {
        listarUsuarios();
        System.out.print("ID do usuario a remover: ");
        String id = sc.nextLine();

        // nao deixa remover o admin principal
        if (id.equals("admin")) {
            System.out.println("Nao e possivel remover o administrador principal.");
            return;
        }

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId().equals(id)) {
                usuarios.remove(i);
                System.out.println("Usuario removido.");
                return;
            }
        }
        System.out.println("Usuario nao encontrado.");
    }

    static void listarUsuarios() {
        System.out.println("\nUsuarios cadastrados:");
        for (Usuario u : usuarios) {
            System.out.println("  " + u.getId() + " - " + u.getNome() + " [" + u.getNivelTexto() + "]");
        }
    }

    static void verHistoricoCompleto() {
        System.out.println("\nHistorico de acessos:");
        if (historico.isEmpty()) {
            System.out.println("Nenhum acesso registrado ainda.");
            return;
        }
        for (String linha : historico) {
            System.out.println(linha);
        }
    }

    static void verHistoricoDoUsuario(String id) {
        System.out.println("\nSeus acessos:");
        boolean achouAlgo = false;
        for (String linha : historico) {
            // a linha contém o id entre parênteses, então dá pra filtrar assim
            if (linha.contains("(" + id + ")")) {
                System.out.println(linha);
                achouAlgo = true;
            }
        }
        if (!achouAlgo) {
            System.out.println("Nenhum registro encontrado.");
        }
    }

    // busca um usuario pelo id, retorna null se nao achar
    static Usuario buscarPorId(String id) {
        for (Usuario u : usuarios) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    // mostra as opcoes de nivel e devolve o numero escolhido
    static int escolherNivel() {
        System.out.println("Nivel: 1-Visitante  2-Funcionario  3-Administrador");
        System.out.print("Escolha: ");
        try {
            int n = Integer.parseInt(sc.nextLine());
            if (n >= 1 && n <= 3) return n;
            System.out.println("Nivel invalido.");
            return -1;
        } catch (NumberFormatException e) {
            System.out.println("Entrada invalida.");
            return -1;
        }
    }
}
