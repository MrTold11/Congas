package ru.congas;

import org.fusesource.jansi.Ansi;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Mr_Told
 */
public class TestPictureOutput extends SimpleGame {

    private final BufferedImage image;
    final Ansi a1 = Ansi.ansi().bgGreen();
    final Ansi a2 = Ansi.ansi().bgBrightGreen();
    final Ansi a3 = Ansi.ansi().bgCyan();

    int pos = 1;
    Ansi prev = null;

    public TestPictureOutput() {
        super("TestPO", false, false, false,
                true, 10, 10, 10);
        image = new BufferedImage(144, 32, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setFont(new Font("Dialog", Font.PLAIN, 24));
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.drawString("Congas!", 6, 24);
    }

    @Override
    public boolean handle(int c) {
        return false;
    }

    @Override
    public void updateCanvas() {
        getColors()[pos / getColors()[0].length][pos % getColors()[0].length] = prev;
        if (pos >= getMatrix().length * getMatrix()[0].length) pos = 1;
        pos++;
        prev = getColors()[pos / getColors()[0].length][pos % getColors()[0].length];
        getColors()[pos / getColors()[0].length][pos % getColors()[0].length] = a3;
    }

    public void resized(int w, int h) {
        initCanvas(w, h);
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 144; x++) {
                if (image.getRGB(x, y) != -16777216) {
                    int y1 = y * h / 32;
                    int x1 = x * w / 144;
                    if (image.getRGB(x, y) == -1) {
                        getMatrix()[y1][x1] = '#';
                        getColors()[y1][x1] = a1;
                    } else {
                        getMatrix()[y1][x1] = '*';
                        getColors()[y1][x1] = a2;
                    }
                }
            }
        }
        if (pos >= getMatrix().length * getMatrix()[0].length) pos = 1;
    }
}
