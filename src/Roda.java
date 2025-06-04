public class Roda {
    private double velocidade; // km/h

    public Roda() {
        this.velocidade = 0;
    }

    public double getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(double velocidade) {
        this.velocidade = Math.max(0, velocidade);
    }

    public void atualizarVelocidade(double rpm, int marcha, double velocidadeMaximaPorMarcha) {
        if (marcha == 0) {
            this.velocidade = 0;
        } else {
            double novaVelocidade = (rpm / 7000.0) * velocidadeMaximaPorMarcha;
            this.velocidade = Math.min(novaVelocidade, velocidadeMaximaPorMarcha);
        }
    }

    public double getResistenciaDoAr() {
        return 0.3 * velocidade * velocidade;
    }
}