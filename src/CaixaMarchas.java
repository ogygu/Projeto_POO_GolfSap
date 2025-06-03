public class CaixaMarchas {
    private int marchaAtual; // 0 = neutro
    private boolean cambioAutomatico;
    // índice 0 = neutro, índices 1-5 = marchas
    private final double[] limitesMarchas = {0, 30, 60, 100, 160, 240};

    public CaixaMarchas() {
        this.marchaAtual = 0; // Começa no neutro
        this.cambioAutomatico = false;
    }

    // Sobe ou desce a marcha (0 = neutro, 1-5 = marchas)
    public void mudarMarcha(boolean up) {
        if (up && marchaAtual < 5) {
            marchaAtual++;
        } else if (!up && marchaAtual > 0) {
            marchaAtual--;
        }
    }

    // Retorna true se a marcha atual não for a ideal para a velocidade
    public boolean needsGearChange(double velocidade) {
        if (marchaAtual == 0 && velocidade == 0) return false; // Neutro parado, ok
        if (marchaAtual == 0 && velocidade > 0) return true;   // Neutro andando, errado
        int ideal = getMarchaIdeal(velocidade);
        return marchaAtual != ideal && velocidade > 0;
    }

    // Retorna a marcha ideal para a velocidade (0 = neutro, 1-5 = marchas)
    public int getMarchaIdeal(double velocidade) {
        if (velocidade == 0) return 0; // Parado, neutro
        for (int i = 1; i < limitesMarchas.length; i++) {
            if (velocidade <= limitesMarchas[i]) {
                return i;
            }
        }
        return 5; // Acima do limite, manter na 5ª
    }

    // Retorna a velocidade máxima permitida para a marcha atual
    public double getVelocidadeMaximaPorMarcha() {
        if (marchaAtual == 0) return 0; // Neutro não anda
        return limitesMarchas[marchaAtual];
    }

    // Atualiza a marcha automaticamente se o câmbio estiver no modo automático
    public void atualizarMarchaAutomaticamente(double velocidade) {
        if (cambioAutomatico) {
            int ideal = getMarchaIdeal(velocidade);
            if (marchaAtual != ideal) {
                marchaAtual = ideal;
            }
        }
    }

    public int getMarchaAtual() {
        return marchaAtual;
    }

    public void setCambioAutomatico(boolean valor) {
        this.cambioAutomatico = valor;
    }

    public boolean isCambioAutomatico() {
        return cambioAutomatico;
    }

    public String getMarchaExibicao() {
    if (marchaAtual == 0) {
        return "N"; // Representa Neutro
    }
    return String.valueOf(marchaAtual); // Marchas 1-5
}
}