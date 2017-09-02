package main;

import java.awt.*;

/**
 * A class which calls the update method 60 times a second
 */
public class Updater implements Runnable {
    int refreshRate;

    public Updater() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        DisplayMode dm = gs[0].getDisplayMode();
        int refreshRate = dm.getRefreshRate();
    }

    @Override
    public void run() {


    }
}
