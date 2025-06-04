import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JButton btnMarchaMais, btnMarchaMenos, btnLigarDesligar, btnAbastecer, btnAcelerador;

    private Timer timerAceleracao;
    private Timer timerDesaceleracao;
    private double velocidadeAlvo = 0;

    public Carro() {
        motor = new Motor();
        caixaMarchas = new CaixaMarchas();
        tanque = new Tanque(100); // Nível inicial de combustível
        roda = new Roda();

        caixaMarchas.setCambioAutomatico(false);

        setTitle("Simulador de Carro");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Labels
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

        lblMarcha = new JLabel("Marcha: N");
        lblMarcha.setBounds(50, 250, 200, 30);
        add(lblMarcha);

        // Botões
        btnLigarDesligar = new JButton("Ligar");
        btnLigarDesligar.setBounds(300, 250, 100, 30);
        btnLigarDesligar.addActionListener(this::toggleLigado);
        add(btnLigarDesligar);

        JButton btnCambioAutomatico = new JButton("Ativar Auto");
        btnCambioAutomatico.setBounds(300, 350, 120, 30);
        btnCambioAutomatico.addActionListener(this::toggleCambio);
        add(btnCambioAutomatico);

        btnAbastecer = new JButton("Abastecer");
        btnAbastecer.setBounds(300, 300, 100, 30);
        btnAbastecer.setEnabled(false);
        btnAbastecer.addActionListener(this::abastecer);
        add(btnAbastecer);

        // Botão de Acelerador Segurável
        btnAcelerador = new JButton("Segure para acelerar");
        btnAcelerador.setBounds(300, 50, 200, 40);
        btnAcelerador.setEnabled(false);
        add(btnAcelerador);

        // Timer para aceleração gradual
        timerAceleracao = new Timer(100, (e) -> {
            double velocidadeAtual = roda.getVelocidade();
            if (velocidadeAtual < velocidadeAlvo && ligado && tanque.getNivelCombustivel() > 0) {
                double diferenca = velocidadeAlvo - velocidadeAtual;
                double passo = Math.min(diferenca, 2); // Acelera 2 km/h por ciclo

                velocidadeAtual += passo;

                // Simula aumento de RPM proporcional
                int rpm = motor.getRPM();
                motor.setRPM((int)(rpm + (passo / caixaMarchas.getVelocidadeMaximaPorMarcha()) * 700));

                roda.setVelocidade(velocidadeAtual);
                atualizarInterface();
            }

            if (velocidadeAtual >= velocidadeAlvo || !ligado || tanque.getNivelCombustivel() <= 0) {
                ((Timer)e.getSource()).stop();
            }
        });

        // Timer para desaceleração automática
        timerDesaceleracao = new Timer(100, (e) -> {
            double velocidadeAtual = roda.getVelocidade();
            if (velocidadeAtual > velocidadeAlvo) {
                double diferenca = velocidadeAtual - velocidadeAlvo;
                double passo = Math.min(diferenca, 2); // Reduz 2 km/h por ciclo
                velocidadeAtual -= passo;

                // Simula queda de RPM proporcional
                int rpm = motor.getRPM();
                motor.setRPM((int)(rpm - (passo / caixaMarchas.getVelocidadeMaximaPorMarcha()) * 700));

                roda.setVelocidade(velocidadeAtual);
                atualizarInterface();

                if (velocidadeAtual <= velocidadeAlvo) {
                    ((Timer)e.getSource()).stop();
                }
            } else {
                ((Timer)e.getSource()).stop();
            }
        });

        // Eventos do botão do acelerador
        btnAcelerador.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                if (ligado && tanque.getNivelCombustivel() > 0) {
                    velocidadeAlvo = caixaMarchas.getVelocidadeMaximaPorMarcha(); // Máx da marcha atual
                    if (!timerAceleracao.isRunning()) {
                        timerAceleracao.start();
                    }
                    if (timerDesaceleracao.isRunning()) {
                        timerDesaceleracao.stop();
                    }
                }
            }

            public void mouseReleased(MouseEvent evt) {
                timerAceleracao.stop();

                velocidadeAlvo = Math.max(0, roda.getVelocidade() * 0.6); // 60% da velocidade atual

                if (!timerDesaceleracao.isRunning()) {
                    timerDesaceleracao.start();
                }
            }
        });

        // Botões de Marcha
        btnMarchaMais = new JButton("Marcha +");
        btnMarchaMais.setBounds(300, 150, 100, 30);
        btnMarchaMais.setEnabled(false);
        btnMarchaMais.addActionListener(e -> {
            if (ligado) {
                caixaMarchas.mudarMarcha(true);
                contadorAviso = 0;
                avisado = false;
                atualizarInterface();
            }
        });
        add(btnMarchaMais);

        btnMarchaMenos = new JButton("Marcha -");
        btnMarchaMenos.setBounds(300, 200, 100, 30);
        btnMarchaMenos.setEnabled(false);
        btnMarchaMenos.addActionListener(e -> {
            if (ligado) {
                caixaMarchas.mudarMarcha(false);
                contadorAviso = 0;
                avisado = false;
                atualizarInterface();
            }
        });
        add(btnMarchaMenos);

        setVisible(true);

        // Verificação periódica de marcha
        Timer timerVerificarMarcha = new Timer(1000, e -> {
            if (!caixaMarchas.isCambioAutomatico() && ligado) {
                double velocidade = roda.getVelocidade();
                if (caixaMarchas.needsGearChange(velocidade)) {
                    contadorAviso++;
                    if (contadorAviso == 3 && !avisado) {
                        JOptionPane.showMessageDialog(Carro.this,
                                "Você deveria trocar de marcha.",
                                "Dica",
                                JOptionPane.INFORMATION_MESSAGE);
                        avisado = true;
                    } else if (contadorAviso >= 6 && avisado) {
                        JOptionPane.showMessageDialog(Carro.this,
                                "ATENÇÃO: Troque de marcha imediatamente!",
                                "Alerta",
                                JOptionPane.WARNING_MESSAGE);
                        contadorAviso = 0;
                        avisado = false;
                    }
                } else {
                    contadorAviso = 0;
                    avisado = false;
                }
            }
        });
        timerVerificarMarcha.start();
    }

    private void toggleLigado(ActionEvent e) {
        ligado = !ligado;
        btnLigarDesligar.setText(ligado ? "Desligar" : "Ligar");

        btnAcelerador.setEnabled(ligado);
        btnMarchaMais.setEnabled(ligado);
        btnMarchaMenos.setEnabled(ligado);
        btnAbastecer.setEnabled(ligado);

        if (!ligado) motor.parar();
        atualizarInterface();
    }

    private void toggleCambio(ActionEvent e) {
        boolean isAuto = caixaMarchas.isCambioAutomatico();
        caixaMarchas.setCambioAutomatico(!isAuto);
        btnMarchaMais.setEnabled(!caixaMarchas.isCambioAutomatico());
        btnMarchaMenos.setEnabled(!caixaMarchas.isCambioAutomatico());
        atualizarInterface();
    }

    private void abastecer(ActionEvent e) {
        if (ligado) {
            tanque.abastecer();
            JOptionPane.showMessageDialog(this,
                    "O carro foi abastecido! Combustível restaurado para 100%",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            atualizarInterface();
        }
    }

    private void atualizarDinamica() {
        double rpm = motor.getRPM();
        int marcha = caixaMarchas.getMarchaAtual();
        double velocidadeMaximaPorMarcha = caixaMarchas.getVelocidadeMaximaPorMarcha();

        roda.atualizarVelocidade(rpm, marcha, velocidadeMaximaPorMarcha);
        tanque.consumirCombustivel(rpm / 7000 * 0.5);

        if (caixaMarchas.isCambioAutomatico()) {
            caixaMarchas.atualizarMarchaAutomaticamente(roda.getVelocidade(), motor.getRPM());
        }

        atualizarInterface();
    }

    private void atualizarInterface() {
        lblVelocimetro.setText("Velocidade: " + String.format("%.2f", roda.getVelocidade()) + " km/h");
        lblRPM.setText("RPM: " + motor.getRPM());
        lblCombustivel.setText("Combustível: " + String.format("%.2f", tanque.getNivelCombustivel()) + "%");
        lblResistenciaAr.setText("Resistência do Ar: " + String.format("%.2f", roda.getResistenciaDoAr()) + " N");
        lblMarcha.setText("Marcha: " + caixaMarchas.getMarchaExibicao());

        if (tanque.getNivelCombustivel() < 10) {
            JOptionPane.showMessageDialog(this, "Combustível muito baixo! Abasteça o carro.", "Alerta", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Carro());
    }
}