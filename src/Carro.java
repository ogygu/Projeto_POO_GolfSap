import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Carro extends JFrame {
    private Motor motor;
    private CaixaMarchas caixaMarchas;
    private Tanque tanque;
    private Roda roda;

    private boolean ligado = false;
    private int contadorAviso = 0;
    private boolean avisado = false;

    private JLabel lblVelocimetro, lblRPM, lblCombustivel, lblResistenciaAr, lblMarcha;
    private JButton btnAcelerar, btnFrear, btnMarchaMais, btnMarchaMenos, btnLigarDesligar, btnAbastecer;

    public Carro() {
        // Inicialização das classes
        motor = new Motor();
        caixaMarchas = new CaixaMarchas();
        tanque = new Tanque(100); // Nível inicial de combustível
        roda = new Roda();

        // Configura o câmbio automático como desativado por padrão
        caixaMarchas.setCambioAutomatico(false);

        // Configuração da janela
        setTitle("Simulador de Carro");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Componentes da interface
        lblVelocimetro = new JLabel("Velocidade: 0 km/h");
        lblVelocimetro.setBounds(50, 50, 200, 30);
        add(lblVelocimetro);

        lblRPM = new JLabel("RPM: 0");
        lblRPM.setBounds(50, 100, 200, 30);
        add(lblRPM);

        lblCombustivel = new JLabel("Combustível: 100%");
        lblCombustivel.setBounds(50, 150, 200, 30);
        add(lblCombustivel);

        lblResistenciaAr = new JLabel("Resistência do Ar: 0 N");
        lblResistenciaAr.setBounds(50, 200, 200, 30);
        add(lblResistenciaAr);

        lblMarcha = new JLabel("Marcha: 1");
        lblMarcha.setBounds(50, 250, 200, 30);
        add(lblMarcha);

        // Botão de Ligar/Desligar
        btnLigarDesligar = new JButton("Ligar");
        btnLigarDesligar.setBounds(300, 250, 100, 30);
        btnLigarDesligar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ligado = !ligado;
                btnLigarDesligar.setText(ligado ? "Desligar" : "Ligar");

                // Ativa/desativa outros botões
                btnAcelerar.setEnabled(ligado);
                btnFrear.setEnabled(ligado);
                btnMarchaMais.setEnabled(ligado);
                btnMarchaMenos.setEnabled(ligado);
                btnAbastecer.setEnabled(ligado);

                if (!ligado) {
                    motor.parar();
                }

                atualizarInterface();
            }
        });
        add(btnLigarDesligar);

        // Botão de Câmbio Automático
        JButton btnCambioAutomatico = new JButton("Ativar Auto");
        btnCambioAutomatico.setBounds(300, 350, 120, 30);
        btnCambioAutomatico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isAuto = caixaMarchas.isCambioAutomatico();
                caixaMarchas.setCambioAutomatico(!isAuto);

                btnCambioAutomatico.setText(caixaMarchas.isCambioAutomatico() ? "Desativar Auto" : "Ativar Auto");

                btnMarchaMais.setEnabled(!caixaMarchas.isCambioAutomatico());
                btnMarchaMenos.setEnabled(!caixaMarchas.isCambioAutomatico());

                atualizarInterface();
            }
        });
        add(btnCambioAutomatico);

        // Botão de Abastecer
        btnAbastecer = new JButton("Abastecer");
        btnAbastecer.setBounds(300, 300, 100, 30);
        btnAbastecer.setEnabled(false);
        btnAbastecer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ligado) {
                    tanque.abastecer();
                    JOptionPane.showMessageDialog(
                        Carro.this,
                        "O carro foi abastecido! Combustível restaurado para 100%",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    atualizarInterface();
                }
            }
        });
        add(btnAbastecer);

        // Botões de ação (inicialmente desativados)
        btnAcelerar = new JButton("Acelerar");
        btnAcelerar.setBounds(300, 50, 100, 30);
        btnAcelerar.setEnabled(false);
        btnAcelerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ligado) {
                    motor.acelerar();
                    atualizarDinamica();
                }
            }
        });
        add(btnAcelerar);

        btnFrear = new JButton("Frear");
        btnFrear.setBounds(300, 100, 100, 30);
        btnFrear.setEnabled(false);
        btnFrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ligado) {
                    motor.freiar();
                    atualizarDinamica();
                }
            }
        });
        add(btnFrear);

        btnMarchaMais = new JButton("Marcha +");
        btnMarchaMais.setBounds(300, 150, 100, 30);
        btnMarchaMais.setEnabled(false);
        btnMarchaMais.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ligado) {
                    caixaMarchas.mudarMarcha(true);
                    contadorAviso = 0;
                    avisado = false;
                    atualizarInterface();
                }
            }
        });
        add(btnMarchaMais);

        btnMarchaMenos = new JButton("Marcha -");
        btnMarchaMenos.setBounds(300, 200, 100, 30);
        btnMarchaMenos.setEnabled(false);
        btnMarchaMenos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ligado) {
                    caixaMarchas.mudarMarcha(false);
                    contadorAviso = 0;
                    avisado = false;
                    atualizarInterface();
                }
            }
        });
        add(btnMarchaMenos);

        setVisible(true);
        // Timer para verificar periodicamente se precisa trocar de marcha
        Timer timerVerificarMarcha = new Timer(1000, (e) -> {
            if (!caixaMarchas.isCambioAutomatico() && ligado) {
                double velocidade = roda.getVelocidade();

                if (caixaMarchas.needsGearChange(velocidade)) {
                    contadorAviso++;

                    if (contadorAviso == 3 && !avisado) {
                        JOptionPane.showMessageDialog(this,
                                "Você deveria trocar de marcha.",
                                "Dica",
                                JOptionPane.INFORMATION_MESSAGE);
                        avisado = true;

                    } else if (contadorAviso >= 6 && avisado) {
                        JOptionPane.showMessageDialog(this,
                                "ATENÇÃO: Troque de marcha imediatamente!",
                                "Alerta",
                                JOptionPane.WARNING_MESSAGE);
                        contadorAviso = 0;
                        avisado = false;
                    }

                } else {
                    // Resetar avisos se a marcha estiver correta
                    contadorAviso = 0;
                    avisado = false;
                }
            }
        });
        timerVerificarMarcha.start();
    }

    private void atualizarDinamica() {
        double rpm = motor.getRPM();
        int marcha = caixaMarchas.getMarchaAtual();
        double velocidadeMaximaPorMarcha = caixaMarchas.getVelocidadeMaximaPorMarcha();

        // Corrigido: agora passa os três parâmetros
        roda.atualizarVelocidade(rpm, marcha, velocidadeMaximaPorMarcha);
        tanque.consumirCombustivel(rpm / 7000 * 0.5); // Consumo proporcional ao RPM

        // Atualiza automaticamente a marcha se for automático
        if (caixaMarchas.isCambioAutomatico()) {
            caixaMarchas.atualizarMarchaAutomaticamente(roda.getVelocidade());
        }

        atualizarInterface();
    }

    private void atualizarInterface() {
        lblVelocimetro.setText("Velocidade: " + String.format("%.2f", roda.getVelocidade()) + " km/h");
        lblRPM.setText("RPM: " + motor.getRPM());
        lblCombustivel.setText("Combustível: " + String.format("%.2f", tanque.getNivelCombustivel()) + "%");
        lblResistenciaAr.setText("Resistência do Ar: " + String.format("%.2f", roda.getResistenciaDoAr()) + " N");
        lblMarcha.setText("Marcha: " + caixaMarchas.getMarchaAtual());

        // Alerta de combustível baixo
        if (tanque.getNivelCombustivel() < 10) {
            JOptionPane.showMessageDialog(this, "Combustível muito baixo! Abasteça o carro.", "Alerta", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Carro());
    }
}