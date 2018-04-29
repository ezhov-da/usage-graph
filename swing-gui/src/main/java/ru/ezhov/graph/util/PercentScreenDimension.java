package ru.ezhov.graph.util;

import java.awt.*;

public class PercentScreenDimension implements ScreenDimension {
    private int percent;

    public PercentScreenDimension(int percent) {
        this.percent = percent;
    }

    @Override
    public Dimension dimension() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();


        return new Dimension(
                value(dimension.width),
                value(dimension.height)
        );
    }

    private int value(int source) {
        return source * percent / 100;
    }
}
