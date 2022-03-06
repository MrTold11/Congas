package ru.congas.anthology;

import ru.congas.core.output.canvas.Canvas;

import java.util.ArrayList;
import java.util.List;

public class Settings extends Field {
    int x;
    int y;
    int mx;
    int my;

    List<int[]> usedBoosters = new ArrayList<>();
    boolean used = true;

    boolean isDead = false;
    boolean isVisible = true;
    boolean killHunter;

    int yb = 0;
    int xb = 0;

    int numberWalls = 11;
    int score = 0;

    boolean booster;
    int lives;

    public char[][] newOutput(Pacmen pacmen, Red red, char[][] field, Canvas canvas) {
        xb = 0;
        yb = 0;

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
                } else if (field[i][j] == '^') {
                    canvas.getCell(i, j).setChar(' ');
                } else {
                    canvas.getCell(i, j).setChar(field[i][j]);
                }

            }
        }
        canvas.getCell(red.y, red.x).setChar('*');
        canvas.getCell(pacmen.y, pacmen.x).setChar('+');
        return field;
    }

    public int getScore(){
        return score;
    }

    public boolean isBooster() {
        return this.booster;
    }

    public void setBooster(boolean booster) {
        this.booster = booster;
    }
}