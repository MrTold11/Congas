package ru.congas.anthology;

import org.fusesource.jansi.Ansi;
        import ru.congas.CongasClient;
        import ru.congas.SimpleGame;
        import ru.congas.input.Keycode;
        import java.awt.*;
import java.util.Random;

/**
 * @author DemonTerra
 */
public class Pacman extends SimpleGame {

    final Ansi star = Ansi.ansi().bgCyan();
    final Ansi Ghost = Ansi.ansi().bgRed();
    int a = 0;
    private boolean inGame = false;
    private boolean dying = false;

    private final int BLOCK_SIZE = 24;
    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int MAX_GHOSTS = 12;
    private final int PACMAN_SPEED = 6;

    private int N_GHOSTS = 1;
    private int lives, score;
    private int[] dx, dy;
    private int[] ghost_x, ghost_y;
    private int[] ghost_dx, ghost_dy;

    private Image heart, ghost;
    private int Pos_x, Pos_y;

    private int pacman_x, pacman_y;
    private int req_dx, req_dy;
    private short[] screenData;
    char temp;
    int Score;



    short[] levelData = {
            19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
            17, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            25, 24, 24, 24, 28, 0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
            0, 0, 0, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
            19, 18, 18, 18, 18, 18, 16, 16, 16, 16, 24, 24, 24, 24, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 0, 0, 0, 21,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 0, 0, 0, 21,
            17, 16, 16, 16, 24, 16, 16, 16, 16, 20, 0, 0, 0, 0, 21,
            17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 18, 18, 18, 18, 20,
            17, 24, 24, 28, 0, 25, 24, 24, 16, 16, 16, 16, 16, 16, 20,
            21, 0, 0, 0, 0, 0, 0, 0, 17, 16, 16, 16, 16, 16, 20,
            17, 18, 18, 22, 0, 19, 18, 18, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            25, 24, 24, 24, 26, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28
    };


    public Pacman() {
        super("TestIO", false, false, true,
                true, 5, 30, 30);
        screenData = new short[N_BLOCKS * N_BLOCKS];
        Pos_x = 0;
        Pos_y = 0;
        pacman_y = 2;
        pacman_x = 14;
        //levelData[10] = -1;

        temp = '^';
        for (int i = 0; i < N_GHOSTS; i++){
            //ghost_x[i] = 5;
            //ghost_y[i] = 5;
            //moveGhosts(i);
        }
        //ghost_x[1] = 5;
        //ghost_y[1] = 5;
        ghost_y[0] = 6;
        ghost_x[0] = 6;
        moveGhosts(0);
    }

    @Override
    public boolean handle(int c) {
        switch (c) {
            case Keycode.ESCAPE:
                CongasClient.close();
                return true;
            case 'd':
                Pos_y = 1;
                Pos_x = 0;
                forceUpdate();
                return true;
            case 'a':
                Pos_y = -1;
                Pos_x = 0;
                forceUpdate();
                return true;
            case 'w':
                Pos_x = -1;
                Pos_y = 0;
                forceUpdate();
                return true;
            case 's':
                Pos_x = 1;
                Pos_y = 0;
                forceUpdate();
                return true;
        }
        return false;
    }

    @Override
    public void updateCanvas() {
        drawMaze();
        movePacman();
        drawPacman();
        //drawGhost();
    }

    public void drawMaze() {

        int x, y;
        int i = 0;
        if (levelData[pacman_y+(pacman_x-1)*15 -1]!= -1 ){
            Score += 10;
            levelData[pacman_y+(pacman_x-1)*15-1] = -1;
        }
        for (x = 1; x <= 15; x += 1) {
            for (y = 1; y <= 15; y += 1) {
                if ((levelData[i] == 0)) {
                    getColors()[x][y] = star;
                }
                else if (levelData[i] != -1) {
                    getMatrix()[x][y] = '*';
                }
                i++;
            }
        }
    }
    private void movePacman() {

        if (N_BLOCKS >= pacman_x+ Pos_x && pacman_x + Pos_x >=1){
            int x, y;
            int i = 0;

            for (x = 1; x <= 15; x += 1) {
                for (y = 1; y <= 15; y += 1) {
                    if ((pacman_x + Pos_x == x )&&(pacman_y+ Pos_y == y)&&(levelData[i] == 0)) {

                        Pos_x = 0;
                    }
                    i++;
                }
            }
            pacman_x = pacman_x + Pos_x;
        }
        if (N_BLOCKS >= pacman_y+ Pos_y && pacman_y + Pos_y >=1){
            int x, y;
            int i = 0;

            for (x = 1; x <= 15; x += 1) {
                for (y = 1; y <= 15; y += 1) {
                    if ((pacman_x + Pos_x == x )&&(pacman_y+ Pos_y == y)&&(levelData[i] == 0)) {

                        Pos_y = 0;
                    }
                    i++;
                }
            }

            pacman_y = pacman_y + Pos_y;
        }
    }

    private void drawPacman() {
        if (Pos_x == -1){
            getMatrix()[pacman_x][pacman_y] = '^';
            temp = '^';
        }
        if (Pos_x == 1){
            getMatrix()[pacman_x ][pacman_y] = '|';
            temp = '|';
        }
        if (Pos_y == -1){
            getMatrix()[pacman_x][pacman_y] = '<';
            temp = '<';
        }
        if (Pos_y == 1){
            getMatrix()[pacman_x][pacman_y] = '>';
            temp = '>';
        }
        if (Pos_x == 0 || Pos_y==0){
            getMatrix()[pacman_x][pacman_y] = temp;
        }
    }

    private void moveGhosts(int i) {

        int pos;
        int count;

        //int n  = 1+(int)(Math.random()*(4-1+1));
        int n = new Random().nextInt(3)+1;
        /*if (n == 1){
            ghost_dy[i][i] = -1;
            ghost_dx[i][i] = 0;
        }*/
        if(n == 2){
            ghost_dy[i] = 0;
            ghost_dx[i] = -1;
        }
        /*else if (n == 3){
            ghost_dy[i][i] = 1;
            ghost_dx[i][i] = 0;
        }*/
        else if(n == 4){
            ghost_dy[i] = 0;
            ghost_dx[i] = 1;
        }


    }

    private void drawGhost() {
        for (int i = 0; i < N_GHOSTS; i++) {
            if (N_BLOCKS >= pacman_x+ ghost_dx[i] && pacman_x + ghost_dx[i] >=1){
                int x, y;
                int j = 0;

                for (x = 1; x <= 15; x += 1) {
                    for (y = 1; y <= 15; y += 1) {
                        if ((pacman_x + ghost_dx[i] == x )&&(pacman_y+ ghost_dy[i] == y)&&(levelData[j] == 0)) {

                            getColors()[20][20] = Ghost;
                            moveGhosts(i);
                        }
                        j++;
                    }
                }
                pacman_x = pacman_x + Pos_x;
            }
            getColors()[pacman_x][pacman_y] = Ghost;

            /*if (pacman_x == ghost_x &&pacman_y ==ghost_y && inGame) {
                dying = true;
            }*/
        }
    }

    private void death() {

        lives--;

        if (lives == 0) {
            inGame = false;
        }

    }
}