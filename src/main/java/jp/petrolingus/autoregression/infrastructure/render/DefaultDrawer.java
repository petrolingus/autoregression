package jp.petrolingus.autoregression.infrastructure.render;

import javafx.scene.chart.XYChart;

public class DefaultDrawer implements Drawer {

    private final XYChart<Number, Number> chart;

    public DefaultDrawer(XYChart<Number, Number> chart) {
        this.chart = chart;
    }

    public void clear() {
        chart.getData().clear();
    }

    @Override
    public void drawSignal(String name, double[] signal) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
//        if (("Error Level").equals(name)) {
//            series.getNode().getStyleClass().add("error-node");
//        }
        for (int i = 0; i < signal.length; i++) {
            series.getData().add(new XYChart.Data<>(i, signal[i]));
        }
        series.setName(name);
        chart.getData().add(series);
    }
}
