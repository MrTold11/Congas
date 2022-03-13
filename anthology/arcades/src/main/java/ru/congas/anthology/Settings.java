package ru.congas.anthology;

import ru.congas.core.output.canvas.Canvas;
import ru.congas.core.output.modifier.Color;

public class Settings {
    int x;
    int y;
    int mx;
    int my;

    int score = 0;

    boolean booster;
    int lives;

    public char[][] newOutput(Pacmen pacmen, Red red, char[][] field, Canvas canvas) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == '·' && i == pacmen.getY() && j == pacmen.getX()) {
                    field[i][j] = ' ';
                    red.duringBoost(field);
                    this.booster = true;
                }

                if (field[i][j] == '.' && i == pacmen.getY() && j == pacmen.getX()) {
                    field[i][j] = ' ';
                    score += 15;
                } else if (field[i][j] == 0) {
                    canvas.getCell(i, j).setChar(' ').setStyle(null);
                } else {
                    canvas.getCell(i, j).setChar(field[i][j]).setStyle(null);
                }

            }
        }
        if (red.y >= 0 && red.x >= 0)
            canvas.getCell(red.y, red.x).setChar('*').setBackground(Color.RED);
        canvas.getCell(pacmen.y, pacmen.x).setChar('+').setBackground(Color.YELLOW);
        return field;
    }

    public int getScore(){
        return score;
    }

    public boolean isBooster() {
        return this.booster;
    }

}