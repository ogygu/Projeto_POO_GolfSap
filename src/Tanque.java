public class Tanque {
    private double nivelCombustivel;

    public Tanque(double nivelInicial) {
        this.nivelCombustivel = nivelInicial;
    }

    public void consumirCombustivel(double consumo) {
        nivelCombustivel -= consumo;
        if (nivelCombustivel < 0) nivelCombustivel = 0;
    }

    public void abastecer() {
        nivelCombustivel = 100; // Recarrega o tanque para 100%
    }

    public double getNivelCombustivel() {
        return nivelCombustivel;
    }
}   