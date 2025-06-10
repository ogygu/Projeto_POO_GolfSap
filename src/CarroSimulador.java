// Interface Gráfica
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CarroSimulador extends JFrame {
    private Carro carro;
    private Timer timer;
    private boolean subirMorro = false;
    
    // Componentes da interface
    private JLabel velocimetroLabel;
    private JLabel marchaLabel;
    private JLabel combustivelLabel;
    private JLabel rpmLabel;
    private JLabel statusLabel;
    private JLabel resistenciaLabel;
    private JProgressBar velocimetroBar;
    private JProgressBar combustivelBar;
    private JProgressBar rpmBar;
    private JProgressBar resistenciaBar;
    private JButton ligarBtn, acelerarBtn, frearBtn, subirMorroBtn;
    private JButton marcha1Btn, marcha2Btn, marcha3Btn, marcha4Btn, marcha5Btn, neutroBtn;
    private JButton modoAutoBtn, abastecerBtn;
    
    public CarroSimulador() {
        carro = new Carro();
        initializeGUI();
        startTimer();
    }
    
    private void initializeGUI() {
        setTitle("Simulador de Carro - POO Java");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Painel principal
        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Painel de instrumentos
        JPanel instrumentPanel = createInstrumentPanel();
        mainPanel.add(instrumentPanel);
        
        // Painel de controles básicos
        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel);
        
        // Painel de marchas
        JPanel gearPanel = createGearPanel();
        mainPanel.add(gearPanel);
        
        // Painel de opções extras
        JPanel extraPanel = createExtraPanel();
        mainPanel.add(extraPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private JPanel createInstrumentPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Instrumentos"));
        
        // Velocímetro
        JPanel velocPanel = new JPanel(new BorderLayout());
        velocPanel.setBorder(BorderFactory.createTitledBorder("Velocímetro"));
        velocimetroLabel = new JLabel("0 km/h", SwingConstants.CENTER);
        velocimetroLabel.setFont(new Font("Arial", Font.BOLD, 16));
        velocimetroBar = new JProgressBar(0, 240);
        velocimetroBar.setStringPainted(true);
        velocPanel.add(velocimetroLabel, BorderLayout.NORTH);
        velocPanel.add(velocimetroBar, BorderLayout.CENTER);
        
        // Conta-giros
        JPanel rpmPanel = new JPanel(new BorderLayout());
        rpmPanel.setBorder(BorderFactory.createTitledBorder("Conta-giros"));
        rpmLabel = new JLabel("0 RPM", SwingConstants.CENTER);
        rpmLabel.setFont(new Font("Arial", Font.BOLD, 16));
        rpmBar = new JProgressBar(0, 7000);
        rpmBar.setStringPainted(true);
        rpmPanel.add(rpmLabel, BorderLayout.NORTH);
        rpmPanel.add(rpmBar, BorderLayout.CENTER);
        
        // Combustível
        JPanel fuelPanel = new JPanel(new BorderLayout());
        fuelPanel.setBorder(BorderFactory.createTitledBorder("Combustível"));
        combustivelLabel = new JLabel("100%", SwingConstants.CENTER);
        combustivelLabel.setFont(new Font("Arial", Font.BOLD, 16));
        combustivelBar = new JProgressBar(0, 100);
        combustivelBar.setStringPainted(true);
        combustivelBar.setValue(100);
        fuelPanel.add(combustivelLabel, BorderLayout.NORTH);
        fuelPanel.add(combustivelBar, BorderLayout.CENTER);
        
        // Resistência do Ar
        JPanel resistenciaPanel = new JPanel(new BorderLayout());
        resistenciaPanel.setBorder(BorderFactory.createTitledBorder("Resistência do Ar"));
        resistenciaLabel = new JLabel("0 N", SwingConstants.CENTER);
        resistenciaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resistenciaBar = new JProgressBar(0, 2000);
        resistenciaBar.setStringPainted(true);
        resistenciaPanel.add(resistenciaLabel, BorderLayout.NORTH);
        resistenciaPanel.add(resistenciaBar, BorderLayout.CENTER);
        
        // Marcha
        JPanel gearDisplayPanel = new JPanel(new BorderLayout());
        gearDisplayPanel.setBorder(BorderFactory.createTitledBorder("Marcha Atual"));
        marchaLabel = new JLabel("N", SwingConstants.CENTER);
        marchaLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gearDisplayPanel.add(marchaLabel, BorderLayout.CENTER);
        
        // Status
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status"));
        statusLabel = new JLabel("Desligado", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        
        panel.add(velocPanel);
        panel.add(rpmPanel);
        panel.add(fuelPanel);
        panel.add(resistenciaPanel);
        panel.add(gearDisplayPanel);
        panel.add(statusPanel);
        
        return panel;
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Controles Básicos"));
        
        ligarBtn = new JButton("Ligar/Desligar");
        ligarBtn.addActionListener(e -> toggleEngine());
        
        acelerarBtn = new JButton("Acelerar");
        // Implementar pressionamento contínuo
        acelerarBtn.addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                carro.iniciarAceleracao();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                carro.pararAceleracao();
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {
                carro.pararAceleracao(); // Parar se sair do botão
            }
        });
        
        frearBtn = new JButton("Frear");
        frearBtn.addActionListener(e -> {
            if (frearBtn.getText().equals("Frear")) {
                carro.frear();
                frearBtn.setText("Soltar Freio");
            } else {
                carro.soltarFreio();
                frearBtn.setText("Frear");
            }
        });
        
        subirMorroBtn = new JButton("Subir Morro");
        subirMorroBtn.addActionListener(e -> {
            subirMorro = !subirMorro;
            subirMorroBtn.setText(subirMorro ? "Descer Morro" : "Subir Morro");
        });
        
        panel.add(ligarBtn);
        panel.add(acelerarBtn);
        panel.add(frearBtn);
        panel.add(subirMorroBtn);
        
        return panel;
    }
    
    private JPanel createGearPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Marchas"));
        
        neutroBtn = new JButton("N");
        neutroBtn.addActionListener(e -> carro.getCaixaMarchas().trocarMarcha(0));
        
        marcha1Btn = new JButton("1ª");
        marcha1Btn.addActionListener(e -> carro.getCaixaMarchas().trocarMarcha(1));
        
        marcha2Btn = new JButton("2ª");
        marcha2Btn.addActionListener(e -> carro.getCaixaMarchas().trocarMarcha(2));
        
        marcha3Btn = new JButton("3ª");
        marcha3Btn.addActionListener(e -> carro.getCaixaMarchas().trocarMarcha(3));
        
        marcha4Btn = new JButton("4ª");
        marcha4Btn.addActionListener(e -> carro.getCaixaMarchas().trocarMarcha(4));
        
        marcha5Btn = new JButton("5ª");
        marcha5Btn.addActionListener(e -> carro.getCaixaMarchas().trocarMarcha(5));
        
        panel.add(neutroBtn);
        panel.add(marcha1Btn);
        panel.add(marcha2Btn);
        panel.add(marcha3Btn);
        panel.add(marcha4Btn);
        panel.add(marcha5Btn);
        
        return panel;
    }
    
    private JPanel createExtraPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Opções Extras"));
        
        modoAutoBtn = new JButton("Modo Automático");
        modoAutoBtn.addActionListener(e -> {
            if (carro.getCaixaMarchas().isModoAutomatico()) {
                carro.getCaixaMarchas().desativarModoAutomatico();
                modoAutoBtn.setText("Modo Automático");
            } else {
                carro.getCaixaMarchas().ativarModoAutomatico();
                if (carro.isLigado()) {
                    carro.getCaixaMarchas().trocarMarcha(1);
                }
                modoAutoBtn.setText("Modo Manual");
            }
        });
        
        abastecerBtn = new JButton("Abastecer");
        abastecerBtn.addActionListener(e -> carro.getTanque().abastecer());
        
        panel.add(modoAutoBtn);
        panel.add(abastecerBtn);
        
        return panel;
    }
    
    private void toggleEngine() {
        if (carro.isLigado()) {
            carro.desligar();
        } else {
            carro.ligar();
        }
    }
    
    private void startTimer() {
        timer = new Timer(100, e -> {
            carro.atualizarVelocidade(subirMorro);
            updateDisplay();
        });
        timer.start();
    }
    
    private void updateDisplay() {
        // Atualizar velocímetro
        int velocidade = (int) carro.getVelocidade();
        velocimetroLabel.setText(velocidade + " km/h");
        velocimetroBar.setValue(velocidade);
        
        // Atualizar RPM
        double rpm = carro.getRoda().calcularRPM(carro.getVelocidade());
        rpmLabel.setText((int)rpm + " RPM");
        rpmBar.setValue(Math.min((int)rpm, 7000));
        
        // Atualizar resistência do ar
        int resistencia = (int) carro.getResistenciaArAtual();
        resistenciaLabel.setText(resistencia + " N");
        resistenciaBar.setValue(Math.min(resistencia, 2000));
        
        // Colorir barra de resistência baseado na intensidade
        if (resistencia < 500) {
            resistenciaBar.setForeground(Color.GREEN);
        } else if (resistencia < 1000) {
            resistenciaBar.setForeground(Color.YELLOW);
        } else {
            resistenciaBar.setForeground(Color.RED);
        }
        
        // Atualizar combustível
        int fuel = (int) carro.getTanque().getCombustivel();
        combustivelLabel.setText(fuel + "%");
        combustivelBar.setValue(fuel);
        
        if (carro.getTanque().isModoReserva() && !carro.getTanque().isTanqueVazio()) {
            combustivelLabel.setText(fuel + "% (RESERVA)");
            combustivelBar.setForeground(Color.ORANGE);
        } else if (carro.getTanque().isTanqueVazio()) {
            combustivelLabel.setText("VAZIO");
            combustivelBar.setForeground(Color.RED);
        } else {
            combustivelBar.setForeground(Color.GREEN);
        }
        
        // Atualizar marcha
        int marcha = carro.getCaixaMarchas().getMarchaAtual();
        if (marcha == 0) {
            marchaLabel.setText("N");
        } else {
            marchaLabel.setText(marcha + "ª");
        }
        
        // Atualizar status
        String status = "";
        if (carro.getTanque().isTanqueVazio()) {
            status = "COMBUSTÍVEL ACABOU!";
            statusLabel.setForeground(Color.RED);
        } else if (carro.isLigado()) {
            status = "Ligado";
            if (carro.getCaixaMarchas().isModoAutomatico()) {
                status += " (Auto)";
            }
            if (carro.isAcelerando()) {
                status += " - Acelerando";
            }
            if (subirMorro) {
                status += " - Subindo Morro";
            }
            statusLabel.setForeground(Color.GREEN);
        } else {
            status = "Desligado";
            statusLabel.setForeground(Color.RED);
        }
        statusLabel.setText(status);
        
        // Habilitar/desabilitar botões baseado no estado
        boolean engineOn = carro.isLigado() && !carro.getTanque().isTanqueVazio();
        acelerarBtn.setEnabled(engineOn);
        frearBtn.setEnabled(engineOn);
        
        boolean manualMode = !carro.getCaixaMarchas().isModoAutomatico();
        neutroBtn.setEnabled(manualMode);
        marcha1Btn.setEnabled(manualMode);
        marcha2Btn.setEnabled(manualMode);
        marcha3Btn.setEnabled(manualMode);
        marcha4Btn.setEnabled(manualMode);
        marcha5Btn.setEnabled(manualMode);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CarroSimulador().setVisible(true);
        });
    }
}