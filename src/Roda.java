class Roda {
    private static final double DIAMETRO = 0.65; // metros (pneu 195/65R15 aproximadamente)
    
    public double calcularRPM(double velocidadeKmh) {
        if (velocidadeKmh == 0) return 0;
        
        double velocidadeMS = velocidadeKmh / 3.6;
        double circunferencia = Math.PI * DIAMETRO;
        double rotacoesPorSegundo = velocidadeMS / circunferencia;
        return rotacoesPorSegundo * 60; // Converter para RPM
    }
}