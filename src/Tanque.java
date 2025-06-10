// Classe Tanque
class Tanque {
    private double combustivel; // Percentual de 0 a 100
    private static final double CAPACIDADE = 50; // Litros
    
    public Tanque() {
        this.combustivel = 100; // Tanque cheio
    }
    
    public void consumir(double quantidade) {
        combustivel = Math.max(0, combustivel - quantidade);
    }
    
    public void abastecer() {
        combustivel = 100;
    }
    
    public boolean isTanqueVazio() {
        return combustivel <= 0;
    }
    
    public boolean isModoReserva() {
        return combustivel <= 15 && combustivel > 0;
    }
    
    public double getCombustivel() { return combustivel; }
}