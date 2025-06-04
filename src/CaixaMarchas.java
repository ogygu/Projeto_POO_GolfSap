public class CaixaMarchas {
    private int marchaAtual; // 0 = neutro
    private boolean cambioAutomatico;
    private final double[] limitesMarchas = {0, 30, 60, 100, 160, 240};

    public CaixaMarchas() {
        this.marchaAtual = 0; // Começa no neutro
        this.cambioAutomatico = false;
    }

    public void mudarMarcha(boolean up) {
        if (up && marchaAtual < 5) {
            marchaAtual++;
        } else if (!up && marchaAtual > 0) {
            marchaAtual--;
        }
    }

    public boolean needsGearChange(double velocidade) {
        if (marchaAtual == 0 && velocidade == 0) return false;
        if (marchaAtual == 0 && velocidade > 0) return true;
        int ideal = getMarchaIdeal(velocidade);
        return marchaAtual != ideal && velocidade > 0;
    }

    public int getMarchaIdeal(double velocidade) {
        if (velocidade == 0) return 0;
        for (int i = 1; i < limitesMarchas.length; i++) {
            if (velocidade <= limitesMarchas[i]) {
                return i;
            }
        }
        return 5;
    }

    public double getVelocidadeMaximaPorMarcha() {
        if (marchaAtual == 0) return 0;
        return limitesMarchas[marchaAtual];
    }

    public void atualizarMarchaAutomaticamente(double velocidade, int rpm) {
        if (cambioAutomatico) {
            if (rpm > 6000 && marchaAtual < 5) {
                marchaAtual++; // Subir marcha
            } else if (rpm < 1500 && marchaAtual > 1) {
                marchaAtual--; // Reduzir marcha
            } else {
                int ideal = getMarchaIdeal(velocidade);
                if (marchaAtual != ideal) {
                    marchaAtual = ideal;
                }
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
        return marchaAtual == 0 ? "N" : String.valueOf(marchaAtual);
    }
}