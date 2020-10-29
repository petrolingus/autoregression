package jp.petrolingus.autoregression.infrastructure.ui;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import jp.petrolingus.autoregression.domain.core.Algorithm;
import jp.petrolingus.autoregression.infrastructure.render.DefaultDrawer;
import jp.petrolingus.autoregression.infrastructure.render.Drawer;

public class Root {

    public BorderPane root;

    public LineChart<Number, Number> signalChart;
    public LineChart<Number, Number> errorChart;

    public TextField frequency1;
    public TextField frequency2;
    public TextField frequency3;

    public TextField boundary1;
    public TextField boundary2;
    public Slider boundarySlider1;
    public Slider boundarySlider2;

    public CheckBox enableNoise;
    public TextField noiseEnergyField;

    public TextField rectWidthField;

    public TextField epsilonField;

    public Label labelN1;
    public Label labelN2;

    Algorithm algorithm;

    Drawer signalChartDrawer;
    Drawer errorChartDrawer;

    public void initialize() {

        frequency1.setText("0.06");
        frequency2.setText("0.03");
        frequency3.setText("0.09");
        rectWidthField.setText("80");

        algorithm = new Algorithm();

        signalChartDrawer = new DefaultDrawer(signalChart);
        errorChartDrawer = new DefaultDrawer(errorChart);

        boundarySlider1.valueProperty().addListener((o, old, value) -> {
            boundary1.setText(value.intValue() + "");
            generate();
        });

        boundarySlider2.valueProperty().addListener((o, old, value) -> {
            boundary2.setText(value.intValue() + "");
            generate();
        });

        enableNoise.selectedProperty().addListener((observable) -> generate());

        generate();
    }

    public void generate() {

        algorithm.f1 = Double.parseDouble(frequency1.getText());
        algorithm.f2 = Double.parseDouble(frequency2.getText());
        algorithm.f3 = Double.parseDouble(frequency3.getText());
        algorithm.n1 = Integer.parseInt(boundary1.getText());
        algorithm.n2 = Integer.parseInt(boundary2.getText());
        algorithm.isNoised = enableNoise.isSelected();
        algorithm.noiseEnergyValue = Integer.parseInt(noiseEnergyField.getText());
        algorithm.rectWidth = Integer.parseInt(rectWidthField.getText());
        algorithm.epsilon = Double.parseDouble(epsilonField.getText());

        algorithm.calculate();

        signalChartDrawer.clear();
        errorChartDrawer.clear();

        signalChartDrawer.drawSignal("Signal", algorithm.signal);
        errorChartDrawer.drawSignal("Error", algorithm.error);
        errorChartDrawer.drawSignal("Smooth", algorithm.smooth);
        errorChartDrawer.drawSignal("Level", algorithm.level);

        labelN1.setText(algorithm.expectedN1);
        labelN2.setText(algorithm.expectedN2);
    }
}
