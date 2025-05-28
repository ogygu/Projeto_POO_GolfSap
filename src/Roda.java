public class Roda {
    private double velocidade;
    private double resistenciaDoAr;

    public Roda() {
        this.velocidade = 0;
        this.resistenciaDoAr = 0;
    }

    public void atualizarVelocidade(double torque, double marcha) {
        // Fórmula simplificada para velocidade
        velocidade = torque / marcha * 0.1; // Exemplo de cálculo

        // Resistência do ar (proporcional à velocidade quadrática)
        resistenciaDoAr = 0.5 * 1.225 * Math.pow(velocidade, 2) * 2.0; // Área frontal estimada
    }

    public double getVelocidade() {
        return velocidade;
    }

    public double getResistenciaDoAr() {
        return resistenciaDoAr;
    }
}