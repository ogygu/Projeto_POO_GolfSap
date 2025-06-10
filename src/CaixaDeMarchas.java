class CaixaMarchas {
    private int marchaAtual;
    private boolean modoAutomatico;
    private double[] multiplicadores = {0, 3.5, 2.1, 1.4, 1.0, 0.8}; // N, 1ª, 2ª, 3ª, 4ª, 5ª
    private double[] velocidadesMaximas = {0, 40, 70, 110, 160, 240}; // Velocidades máximas por marcha em km/h
    
    public CaixaMarchas() {
        this.marchaAtual = 0; // Neutro
        this.modoAutomatico = false;
    }
    
    public void trocarMarcha(int marcha) {
        if (!modoAutomatico && marcha >= 0 && marcha <= 5) {
            marchaAtual = marcha;
        }
    }
    
    public void trocarMarchaAutomatica(double velocidade) {
        if (!modoAutomatico) return;
        
        // Lógica de troca automática baseada na velocidade
        // Troca pra cima quando próximo do limite da marcha atual
        if (marchaAtual == 0 || marchaAtual == 1) {
            marchaAtual = 1;
        }
        
        if (velocidade >= 35 && marchaAtual == 1) {
            marchaAtual = 2;
        } else if (velocidade >= 65 && marchaAtual == 2) {
            marchaAtual = 3;
        } else if (velocidade >= 105 && marchaAtual == 3) {
            marchaAtual = 4;
        } else if (velocidade >= 155 && marchaAtual == 4) {
            marchaAtual = 5;
        }
        
        // Troca pra baixo quando velocidade muito baixa para a marcha atual
        if (velocidade < 25 && marchaAtual == 2) {
            marchaAtual = 1;
        } else if (velocidade < 55 && marchaAtual == 3) {
            marchaAtual = 2;
        } else if (velocidade < 95 && marchaAtual == 4) {
            marchaAtual = 3;
        } else if (velocidade < 145 && marchaAtual == 5) {
            marchaAtual = 4;
        }
    }
    
    public void ativarModoAutomatico() {
        modoAutomatico = true;
    }
    
    public void desativarModoAutomatico() {
        modoAutomatico = false;
    }
    
    public double getMultiplicador() {
        return multiplicadores[marchaAtual];
    }
    
    public double getVelocidadeMaximaMarcha() {
        return velocidadesMaximas[marchaAtual];
    }
    
    public int getMarchaAtual() { return marchaAtual; }
    public boolean isModoAutomatico() { return modoAutomatico; }
}