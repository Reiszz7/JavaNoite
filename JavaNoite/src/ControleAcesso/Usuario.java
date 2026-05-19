package ControleAcesso;

// classe que representa um usuário do sistema
public class Usuario {

    private String id;
    private String nome;
    private int nivel; // 1 = visitante, 2 = funcionario, 3 = administrador
    private int senhaCodificada; // uso hashCode() pra não salvar a senha direto

    public Usuario(String id, String nome, String senha, int nivel) {
        this.id = id;
        this.nome = nome;
        this.senhaCodificada = senha.hashCode(); // transforma a senha num número
        this.nivel = nivel;
    }

    // verifica se a senha digitada bate com a que foi cadastrada
    public boolean verificarSenha(String senha) {
        return this.senhaCodificada == senha.hashCode();
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setSenha(String novaSenha) {
        this.senhaCodificada = novaSenha.hashCode();
    }

    // retorna o nível como texto pra mostrar pro usuário
    public String getNivelTexto() {
        if (nivel == 1) return "Visitante";
        if (nivel == 2) return "Funcionario";
        if (nivel == 3) return "Administrador";
        return "Desconhecido";
    }
}
