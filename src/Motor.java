public class Motor {
    private int rpm;
    private double torque;

    public Motor() {
        this.rpm = 0;
        this.torque = 0;
    }

    public void acelerar() {
        rpm += 500; // Incrementa RPM
        if (rpm > 7000) rpm = 7000; // Limite máximo de RPM
        torque = rpm / 1000 * 100; // Torque proporcional ao RPM
    }

    public void freiar() {
        rpm -= 500; // Decrementa RPM
        if (rpm < 0) rpm = 0;
        torque = rpm / 1000 * 100;
    }

    public void parar() {
        rpm = 0;
        torque = 0;
    }

    public int getRPM() {
        return rpm;
    }

    public void setRPM(int rpm) {
        this.rpm = Math.max(0, Math.min(rpm, 7000));
        this.torque = this.rpm / 1000 * 100;
    }

    public double getTorque() {
        return torque;
    }
}