package jp.petrolingus.autoregression.domain.generator;

public class DefaultSignalGenerator implements SignalGenerator {

    private final double f1;
    private final double f2;
    private final double f3;

    private final int n1;
    private final int n2;

    public DefaultSignalGenerator(double f1, double f2, double f3, int n1, int n2) {
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.n1 = n1;
        this.n2 = n2;
    }

    public double[] generate() {

        int n = 1000;
        double fd = 1;

        double phi = 2 * Math.PI * (f1 - f2) * n1 * fd;
        double theta = 2 * Math.PI * (f2 - f3) * n2 * fd + phi;

        double[] signal = new double[n];

        for (int i = 0; i < n; i++) {
            double x = i * fd;
            if (i < n1) {
                signal[i] = Math.sin(2 * Math.PI * f1 * x);
            } else if (i < n2){
                signal[i] = Math.sin(2 * Math.PI * f2 * x + phi);
            } else {
                signal[i] = Math.sin(2 * Math.PI * f3 * x + theta);
            }
        }

        return signal;
    }
}
