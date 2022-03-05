package ru.congas.pages.testApps;

import ru.congas.core.application.GameActivity;
import ru.congas.core.input.keys.Key;
import ru.congas.core.input.keys.KeyPressed;
import ru.congas.core.output.modifier.Color;
import ru.congas.core.output.modifier.Style;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Mr_Told
 */
@SuppressWarnings("unused")
public class TestPictureOutput extends GameActivity {

    private final BufferedImage image;
    final Style a1 = new Style(Color.FOREST);
    final Style a2 = new Style(Color.FIREBRICK);
    final Style a3 = new Style(Color.CYAN);

    int pos = 1;
    Style prev;

    public TestPictureOutput() {
        image = new BufferedImage(144, 32, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setFont(new Font("Dialog", Font.PLAIN, 24));
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.drawString("Congas!", 6, 24);
        screen.setLiveUpdate(true);
    }

    @Override
    public boolean handle(KeyPressed event) {
        if (event.getDefinedKey() == Key.UP) {
            setTps(getTps() + 5);
            screen.setFps(screen.getFps() + 5);
            return true;
        } else if (event.getDefinedKey() == Key.DOWN) {
            setTps(getTps() - 5);
            screen.setFps(screen.getFps() - 5);
            return true;
        }
        return false;
    }

    @Override
    public void onResize(int w, int h, int nw, int nh) {
        screen.getCanvas().clear(nw, nh);
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 144; x++) {
                if (image.getRGB(x, y) != -16777216) {
                    int y1 = y * nh / 32;
                    int x1 = x * nw / 144;
                    if (image.getRGB(x, y) == -1)
                        getCanvas().getCell(y1, x1).setChar('#').setStyle(a1);
                    else
                        getCanvas().getCell(y1, x1).setChar('*').setStyle(a2);
                }
            }
        }
        if (pos >= screen.getHeight() * screen.getWidth()) pos = 1;
        screen.updateCanvas();
    }

    @Override
    public void onMainLoop() {
        getCanvas().getCell(pos / screen.getWidth(), pos % screen.getWidth()).setStyle(prev);
        pos++;
        if (pos >= screen.getHeight() * screen.getWidth()) pos = 1;
        prev = new Style(getCanvas().getCell(pos / screen.getWidth(), pos % screen.getWidth()));
        getCanvas().getCell(pos / screen.getWidth(), pos % screen.getWidth()).setStyle(a3);
        screen.updateCanvas();
    }
}
