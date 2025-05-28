public class CaixaMarchas {
    private int marchaAtual;
    private boolean cambioAutomatico;

    // Limites de velocidade por marcha
    private final double[] limitesMarchas = {10, 30, 60, 90, 240};

    public CaixaMarchas() {
        this.marchaAtual = 1;
        this.cambioAutomatico = false; // Começa no modo manual
    }

    public void mudarMarcha(boolean up) {
        if (up && marchaAtual < 5) {
            marchaAtual++;
        } else if (!up && marchaAtual > 1) {
            marchaAtual--;
        }
    }
    // ...existing code...

    // Atualiza a marcha automaticamente se o câmbio estiver no modo automático
    public void atualizarMarchaAutomaticamente(double velocidade) {
        if (cambioAutomatico) {
            int marchaIdeal = getMarchaIdeal(velocidade);
            if (marchaIdeal != marchaAtual) {
                marchaAtual = marchaIdeal;
            }
        } 
    }
// ...existing code...

    // Retorna qual seria a marcha ideal com base na velocidade
    public int getMarchaIdeal(double velocidade) {
        for (int i = 0; i < limitesMarchas.length; i++) {
            if (velocidade <= limitesMarchas[i]) {
                return i + 1; // Índice começa em 0, marchas começam em 1
            }
        }
        return 5; // Velocidade máxima, manter na 5ª marcha
    }

    // Verifica se precisa trocar de marcha com base na velocidade
    public boolean needsGearChange(double velocidade) {
        int marchaIdeal = getMarchaIdeal(velocidade);
        return marchaIdeal != marchaAtual;
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
}