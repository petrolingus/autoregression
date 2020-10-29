package jp.petrolingus.autoregression.infrastructure.render;

public interface Drawer {

    void drawSignal(String name, double[] signal);

    void clear();
}
