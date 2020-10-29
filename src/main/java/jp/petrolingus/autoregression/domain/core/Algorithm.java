package jp.petrolingus.autoregression.domain.core;

import jp.petrolingus.autoregression.domain.generator.DefaultSignalGenerator;
import jp.petrolingus.autoregression.domain.generator.SignalGenerator;
import jp.petrolingus.autoregression.domain.util.Point;

import java.util.Arrays;
import java.util.Random;

import static java.lang.Math.sqrt;

public class Algorithm {

    public double f1;
    public double f2;
    public double f3;

    public int n1;
    public int n2;

    public boolean isNoised;
    public double noiseEnergyValue;

    public int rectWidth;

    public double epsilon;

    public String expectedN1;
    public String expectedN2;

    public double[] signal;
    public double[] error;
    public double[] smooth;
    public double[] level;

    private void imposeNoise(double[] signal) {

        int n = signal.length;

        Random random = new Random();

        double[] noise = new double[n];
        double noiseEnergy = 0;
        for (int i = 0; i < n; i++) {
            double y = random.nextGaussian();
            noiseEnergy += y * y;
            noise[i] = y;
        }

        double signalEnergy = 0;
        for (double v : signal) {
            signalEnergy += v * v;
        }

        double alpha = sqrt((noiseEnergyValue / 100) * signalEnergy / noiseEnergy);

        for (int i = 0; i < signal.length; i++) {
            signal[i] += alpha * noise[i];
        }
    }

    private Point calculateParameters(double f) {

        double w = 2 * Math.PI * f;
        double dt = 0.0001;

        double a = Math.cos(w * dt);
        double b = Math.cos(2 * w * dt);

        double delta = a * a - b;
        double delta1 = -a + a * b;
        double delta2 = -(a * a) + 1;

//        double a1 = 2 * Math.cos(2.0 * Math.PI * f);
//        double a2 = -1;

        double a1 = delta1 / delta;
        double a2 = delta2 / delta;

        return new Point(a1, a2);
    }

    public void calculate() {

        SignalGenerator generator = new DefaultSignalGenerator(f1, f2, f3, n1, n2);

        int n = 1000;

        signal = generator.generate();

        if (isNoised) {
            imposeNoise(signal);
        }

        Point coefficients = calculateParameters(f2);

        double[] regression = new double[n];
        for (int i = 2; i < n; i++) {
            double a = -coefficients.x * signal[i - 1];
            double b = -coefficients.y * signal[i - 2];
            regression[i] = a + b;
        }

        error = new double[n];
        for (int i = 2; i < n; i++) {
            error[i] = Math.pow(signal[i] - regression[i], 2);
        }

        smooth = new double[n];
        int shift = rectWidth / 2 + 1;
        for (int i = shift; i < n - shift; i++) {
            double sum = error[i];
            for (int j = 1; j <= rectWidth / 2; j++) {
                sum += error[i - j] + error[i + j];
            }
            smooth[i] = sum / rectWidth;
        }
        for (int i = 0; i < shift; i++) {
            smooth[i] = smooth[shift];
        }
        for (int i = n - shift; i < n; i++) {
            smooth[i] = smooth[n - shift - 1];
        }

        level = new double[n];
        Arrays.fill(level, epsilon);

        double[] squareDeviation = new double[n];
        for (int i = 0; i < n; i++) {
            double div = level[i] - smooth[i];
            squareDeviation[i] = Math.pow(div, 2);
        }

        int n1 = 0;
        for (int i = 0; i < n / 2; i++) {
            if (squareDeviation[i] < squareDeviation[n1]) {
                n1 = i;
            }
        }

        int n2 = n - 1;
        for (int i = 500; i < n; i++) {
            if (squareDeviation[i] < squareDeviation[n2]) {
                n2 = i;
            }
        }

        expectedN1 = n1 + "";
        expectedN2 = n2 + "";
    }
}
