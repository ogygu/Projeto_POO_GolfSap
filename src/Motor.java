class Motor {
    private boolean ligado;
    private double potencia; // Potência base do motor
    
    public Motor() {
        this.ligado = false;
        this.potencia = 120; // HP convertido para kW aproximadamente
    }
    
    public void ligar() {
        ligado = true;
    }
    
    public void desligar() {
        ligado = false;
    }
    
    public double getPotencia() {
        return ligado ? potencia : 0;
    }
    
    public boolean isLigado() { return ligado; }
}