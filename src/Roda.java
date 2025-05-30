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

    // Atualiza a velocidade da roda de acordo com o rpm, marcha e limite da marcha
    public void atualizarVelocidade(double rpm, int marcha, double velocidadeMaximaPorMarcha) {
        if (marcha == 0) {
            this.velocidade = 0;
        } else {
            // Simula velocidade proporcional ao rpm, limitada pelo máximo da marcha
            double novaVelocidade = (rpm / 7000.0) * velocidadeMaximaPorMarcha;
            this.velocidade = Math.min(novaVelocidade, velocidadeMaximaPorMarcha);
        }
    }

    // Calcula a resistência do ar (exemplo: 0.3 * v^2)
    public double getResistenciaDoAr() {
        return 0.3 * velocidade * velocidade;
    }
}