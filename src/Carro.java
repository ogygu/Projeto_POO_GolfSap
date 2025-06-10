class Carro {
    private Motor motor;
    private CaixaMarchas caixaMarchas;
    private Roda roda;
    private Tanque tanque;
    private double velocidade;
    private boolean ligado;
    private boolean acelerando;
    private boolean freando;
    private static final double MASSA_CARRO = 1200; // kg
    private static final double COEFICIENTE_ARRASTO = 0.3; // Reduzido de valores típicos (0.25-0.35)
    private static final double AREA_FRONTAL = 2.2; // m²
    private static final double DENSIDADE_AR = 1.225; // kg/m³
    
    public Carro() {
        this.motor = new Motor();
        this.caixaMarchas = new CaixaMarchas();
        this.roda = new Roda();
        this.tanque = new Tanque();
        this.velocidade = 0;
        this.ligado = false;
        this.acelerando = false;
        this.freando = false;
    }
    
    public void ligar() {
        if (!tanque.isTanqueVazio()) {
            ligado = true;
            motor.ligar();
        }
    }
    
    public void desligar() {
        ligado = false;
        motor.desligar();
        velocidade = 0;
        acelerando = false;
        freando = false;
    }
    
    public void iniciarAceleracao() {
        if (ligado && !tanque.isTanqueVazio()) {
            acelerando = true;
            freando = false;
        }
    }
    
    public void pararAceleracao() {
        acelerando = false;
    }
    
    public void frear() {
        freando = true;
        acelerando = false;
    }
    
    public void soltarFreio() {
        freando = false;
    }
    
    public void atualizarVelocidade(boolean subirMorro) {
        if (!ligado || tanque.isTanqueVazio()) {
            // Desaceleração natural quando desligado ou sem combustível
            velocidade = Math.max(0, velocidade - 2.0);
            return;
        }
        
        // Consumo de combustível
        if (acelerando) {
            tanque.consumir(0.05); // Consumo mais realista
        } else {
            tanque.consumir(0.01); // Consumo em marcha lenta
        }
        
        // Verificar limite de velocidade por marcha ANTES de acelerar
        double velocidadeMaximaMarcha = caixaMarchas.getVelocidadeMaximaMarcha();
        
        // Forças que atuam no carro
        double forcaMotor = 0;
        double forcaResistencia = 0;
        
        if (acelerando && caixaMarchas.getMarchaAtual() > 0 && velocidade < velocidadeMaximaMarcha) {
            // Força do motor baseada na marcha - reduzida próximo ao limite
            double potenciaMotor = motor.getPotencia() * caixaMarchas.getMultiplicador();
            double fatorLimite = Math.max(0.1, 1.0 - (velocidade / velocidadeMaximaMarcha));
            forcaMotor = potenciaMotor * 50 * fatorLimite; // Força reduz próximo ao limite
        }
        
        // Resistência do ar (reduzida significativamente)
        double velocidadeMS = velocidade / 3.6; // Conversão km/h para m/s
        double resistenciaAr = 0.5 * DENSIDADE_AR * COEFICIENTE_ARRASTO * AREA_FRONTAL * 
                              Math.pow(velocidadeMS, 2) * 0.3; // Fator 0.3 para reduzir impacto
        
        // Resistência adicional ao subir morro (mais suave)
        double resistenciaMorro = 0;
        if (subirMorro) {
            resistenciaMorro = MASSA_CARRO * 9.81 * 0.1; // Inclinação de 10% (mais suave)
        }
        
        // Resistência de rolamento (constante e baixa)
        double resistenciaRolamento = MASSA_CARRO * 9.81 * 0.015; // Coef. rolamento típico
        
        forcaResistencia = resistenciaAr + resistenciaMorro + resistenciaRolamento;
        
        // Aplicar freio
        if (freando) {
            forcaResistencia += 3000; // Força de frenagem
        }
        
        // Calcular aceleração (F = ma, logo a = F/m)
        double forcaLiquida = forcaMotor - forcaResistencia;
        double aceleracao = forcaLiquida / MASSA_CARRO;
        
        // Atualizar velocidade (limitando a variação para suavizar)
        double deltaVelocidade = aceleracao * 0.36; // Conversão m/s² para km/h e ajuste de tempo
        deltaVelocidade = Math.max(-5.0, Math.min(5.0, deltaVelocidade)); // Limitar variação
        
        double novaVelocidade = velocidade + deltaVelocidade;
        
        // Aplicar limites de velocidade
        novaVelocidade = Math.max(0, Math.min(novaVelocidade, 240)); // Limite máximo absoluto
        novaVelocidade = Math.min(novaVelocidade, velocidadeMaximaMarcha); // Limite por marcha
        
        velocidade = novaVelocidade;
        
        // Trocar marcha automaticamente se necessário
        if (caixaMarchas.isModoAutomatico()) {
            caixaMarchas.trocarMarchaAutomatica(velocidade);
        }
    }
    
    public double getResistenciaArAtual() {
        double velocidadeMS = velocidade / 3.6;
        return 0.5 * DENSIDADE_AR * COEFICIENTE_ARRASTO * AREA_FRONTAL * 
               Math.pow(velocidadeMS, 2) * 0.3;
    }
    
    // Getters
    public double getVelocidade() { return velocidade; }
    public boolean isLigado() { return ligado; }
    public boolean isAcelerando() { return acelerando; }
    public Motor getMotor() { return motor; }
    public CaixaMarchas getCaixaMarchas() { return caixaMarchas; }
    public Roda getRoda() { return roda; }
    public Tanque getTanque() { return tanque; }
}