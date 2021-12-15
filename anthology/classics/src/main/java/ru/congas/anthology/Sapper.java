package ru.congas.anthology;

import org.fusesource.jansi.Ansi;
import ru.congas.CongasClient;
import ru.congas.SimpleGame;
import ru.congas.input.Keycode;

import java.util.Random;

/**
 * @author Jailflat
 */
public class Sapper extends SimpleGame {

    final Ansi star = Ansi.ansi().bgCyan();
    final Ansi yellow = Ansi.ansi().bgYellow();
    final Ansi green = Ansi.ansi().bgBrightGreen();
    final Ansi red = Ansi.ansi().bgRed();
    int boardW = 22;
    int boardH = 22;
    int numberMines = 2;
    int[][] mines;
    boolean[][] flags;
    boolean[][] revealed;
    int pos_x;
    int pos_y;

    public Sapper() {
        super("Sapper", true, false, false,
                false, 10, 22, 22);
        start();
    }

    public void launch() {
        start();

    }

    private void start() {
        mines = new int[boardW][boardH];
        flags = new boolean[boardW][boardH];
        revealed = new boolean[boardW][boardH];
        for(int i = 1; i < boardW; i++) {
            for(int j = 1; j < boardH; j++) {
                mines[i][j] = 0;
                flags[i][j] = false;
                revealed[i][j] = false;
            }
        }
        int i = 0;
        while(i < numberMines){
            Random random = new Random();
            int x = random.nextInt(boardW);
            int y = random.nextInt(boardH);
            if(mines[x][y] == 1) continue;
            mines[x][y] = 1;
            i++;
        }
        for(i = 1; i < 21; i++)
            for(int j = 1; j < 21; j++) {
                getMatrix()[i][j] = ' ';
                getColors()[i][j] = star;
            }
        pos_x = 10;
        pos_y = 10;
        switchPos(pos_x, pos_y);
    }

    private void openBoard(int x, int y) {
        if(isUnavailable(x,y)) exit();
        revealed[x][y] = true;
        updateBoard();
        if(minesNearby(x,y) != 0) CongasClient.close();
        boolean won = true;
        for(int i = 1; i < 21; i++) {
            for (int j = 1; j < 21; j++)
                if (!revealed[i][j] && mines[i][j] == 0) won = false;
            break;
        }
        if(won) exit();
    }

    private void switchPos(int x, int y) {
        if(!isUnavailable(x, y)) {
            if(revealed[pos_x][pos_y]) getColors()[pos_x][pos_y] = green;
            else getColors()[pos_x][pos_y] = star;
            pos_x = x;
            pos_y = y;
            getColors()[pos_x][pos_y] = yellow;
        }
        forceUpdate();
    }

    private boolean isUnavailable(int x,int y) {
        return x < 0 || y < 0 || x >= boardW || y >= boardH;
    }

    private int minesNearby(int x, int y) {
        if(isUnavailable(x,y)) return 0;
        int i=0;
        for (int offsetX=-1; offsetX<=1; offsetX++) {
            for (int offsetY=-1; offsetY<=1; offsetY++) {
                if (isUnavailable(offsetX+x, offsetY+y)) continue;
                i+=mines[offsetX+x][offsetY+y];
            }
        }
        return i;
    }

    private void updateBoard() {
        for(int i = 1; i < 21; i++)
            for(int j = 1; j < 21; j++)
                if(revealed[i][j]) getColors()[i][j] = green;
                else if(mines[i][j] == 1) getColors()[i][j] = red;
                else if(!revealed[i][j]) getColors()[i][j] = star;
        getColors()[pos_x][pos_y] = yellow;
        forceUpdate();


    }

    @Override
    public boolean handle(int c) {
        switch (c) {
            case Keycode.ESCAPE:
                CongasClient.close();
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
                if(!revealed[pos_x][pos_y]) {
                    openBoard(pos_x, pos_y);
                    openBoard(pos_x-1,pos_y-1);
                    openBoard(pos_x-1,pos_y+1);
                    openBoard(pos_x+1,pos_y-1);
                    openBoard(pos_x+1,pos_y+1);
                    openBoard(pos_x-1,pos_y);
                    openBoard(pos_x+1,pos_y);
                    openBoard(pos_x,pos_y-1);
                    openBoard(pos_x,pos_y+1);
                    updateBoard();
                }
                return true;
        }
        return false;
    }

    @Override
    public void updateCanvas() {

    }
}
