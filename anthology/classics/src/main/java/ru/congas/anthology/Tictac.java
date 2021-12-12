package ru.congas.anthology;

import org.fusesource.jansi.Ansi;
import ru.congas.SimpleGame;
import ru.congas.input.Keycode;


/**
 * @author Jailflat
 */
public class Tictac extends SimpleGame {

    final Ansi star = Ansi.ansi().bgCyan();
    final Ansi yellow = Ansi.ansi().bgYellow();

    int turn;
    int pos_x, pos_y;

    public Tictac() {
        super("TicTacToe", true, false, false,
                false, 10, 4, 4);
    }

    public int close() {
        //возврат либо рестарт
        return 0;
    }

    public void launch() {
        start();
        super.launch();
    }

    private void switchPos(int x, int y) {
        if(x != -1 && y != -1 && y != 3 && x != 3) {
            getColors()[pos_x][pos_y] = star;
            pos_x = x;
            pos_y = y;
            getColors()[pos_x][pos_y] = yellow;
        }
        forceUpdate();
    }

    private void start() {
        turn = 1;
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++) {
                getMatrix()[i][j] = ' ';
                getColors()[i][j] = star;
            }
        pos_x = 1;
        pos_y = 1;
        switchPos(pos_x, pos_y);
    }

    private void newTurn() {
        if(turn != 10) {
            if (checkGameState() != 0) start();
            turn++;
            forceUpdate();
        }
    }

    private int checkGameState() {

        StringBuilder line = new StringBuilder();

        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++)
                if (getMatrix()[i][j] != ' ') line.append(getMatrix()[i][j]);



            if(line.toString().equals("XXX")) return 1;
            if(line.toString().equals("000")) return 2;
            line.setLength(0);
        }

        line.setLength(0);
        line.append(getMatrix()[0][0]).append(getMatrix()[1][1]).append(getMatrix()[2][2]);
        if(line.toString().equals("XXX")) return 1;
        else if(line.toString().equals("000")) return 2;

        line.setLength(0);
        line.append(getMatrix()[2][0]).append(getMatrix()[1][1]).append(getMatrix()[0][2]);
        if(line.toString().equals("XXX")) return 1;
        else if(line.toString().equals("000")) return 2;

        return 0;
    }


    @Override
    public boolean handle(int c) {
        switch (c) {
            case Keycode.ESCAPE:
                close();
                return true;
            case 'r':
                start();
                return true;
            case 'a':
                switchPos(pos_x, pos_y-1);
                return true;
            case 'w':
                switchPos(pos_x-1, pos_y);
                return true;
            case 'd':
                switchPos(pos_x, pos_y+1);
                return true;
            case 's':
                switchPos(pos_x+1, pos_y);
                return true;
            case Keycode.ENTER:
                if(getMatrix()[pos_x][pos_y] == ' ') {
                    if(turn % 2 == 0) getMatrix()[pos_x][pos_y] = '0';
                    else getMatrix()[pos_x][pos_y] = 'X';
                    newTurn();
                }
                return true;
        }
        return false;
    }

    @Override
    public void updateCanvas() {

    }
}
