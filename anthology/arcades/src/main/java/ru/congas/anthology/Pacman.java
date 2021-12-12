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

    private boolean inGame,win;

    private final int N_BLOCKS = 15;

    private int N_GHOSTS = 2;
    private int lives, score;
    private int[] ghost_x, ghost_y;
    private int[] ghost_dx, ghost_dy;

    private int Pos_x, Pos_y;

    private int pacman_x, pacman_y;
    char temp;

    short[] levelData = {
            18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18,
            18, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 18,
            18, 24, 24, 24, 28, 0, 17, 16, 16, 16, 16, 16, 16, 16, 18,
            0, 0, 0, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 18,
            18, 18, 18, 18, 18, 18, 16, 16, 16, 16, 24, 24, 24, 24, 18,
            18, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 0, 0, 0, 18,
            18, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 0, 0, 0, 18,
            18, 16, 16, 16, 24, 16, 16, 16, 16, 20, 0, 0, 0, 0, 18,
            18, 16, 16, 20, 0, 17, 16, 16, 16, 16, 18, 18, 18, 18, 18,
            18, 24, 24, 28, 0, 25, 24, 24, 16, 16, 16, 16, 16, 16, 18,
            18, 0, 0, 0, 0, 0, 0, 0, 17, 16, 16, 16, 16, 16, 18,
            18, 18, 18, 22, 0, 19, 18, 18, 16, 16, 16, 16, 16, 16, 18,
            18, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 18,
            18, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 18,
            18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18
    };


    public Pacman() {
        super("TestIO", false, false, true,
                true, 5, 30, 30);
        Pos_x = 0;
        Pos_y = 0;
        pacman_y = 2;
        pacman_x = 14;

        temp = '^';
        ghost_x = new int[N_GHOSTS];
        ghost_y = new int[N_GHOSTS];

        ghost_dx = new int[N_GHOSTS];
        ghost_dy = new int[N_GHOSTS];
        for (int i = 0; i < N_GHOSTS; i++){
            if (i%2 ==0) {
                ghost_x[i] = 10;
                ghost_y[i] = 10;
                ghost_dx[i] = 1;
            }
            else{
                ghost_x[i] = 6;
                ghost_y[i] = 6;
                ghost_dy[i] = 1;
            }
        }
        inGame = true;
        lives = 3;
        win = false;
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
        if (!win){
            if (inGame) {
                drawMaze();
                movePacman();
                drawPacman();
                drawGhost();
            }
            else{
                exit();
            }
        }
        else {
            getMatrix()[5][5] = 'Y';
            getMatrix()[5][6] = 'o';
            getMatrix()[5][7] = 'u';
            getMatrix()[5][9] = 'w';
            getMatrix()[5][10] = 'i';
            getMatrix()[5][11] = 'n';
            getMatrix()[5][12] = '!';
        }

    }

    public void drawMaze() {

        int x, y;
        int i = 0;
        if (levelData[pacman_y+(pacman_x-1)*15 -1]!= -1 ){
            score += 1;
            if (score == 194){
                win = true;
            }
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

        int n = new Random().nextInt(3)+1;
        if (n == 1){
            ghost_dy[i] = -1;
            ghost_dx[i] = 0;
        }
        if(n == 2){
            ghost_dy[i] = 0;
            ghost_dx[i] = -1;
        }
        else if (n == 3){
            ghost_dy[i] = 1;
            ghost_dx[i] = 0;
        }
        else if(n == 4){
            ghost_dy[i] = 0;
            ghost_dx[i] = 1;
        }


    }

    private void drawGhost() {
        for (int i = 0; i < N_GHOSTS; i++) {
            if (N_BLOCKS >= ghost_x[i] + ghost_dx[i] && ghost_x[i] + ghost_dx[i] >=1){
                int x, y;
                int j = 0;

                for (x = 1; x <= 15; x += 1) {
                    for (y = 1; y <= 15; y += 1) {
                        if ((ghost_x[i] + ghost_dx[i] == x )&&(ghost_y[i]+ ghost_dy[i] == y)&&(levelData[j] == 0
                                ||((levelData[j])%10 ==8))){
                            ghost_dy[i] = ghost_dy[i] * (-1);
                            ghost_dx[i] = ghost_dx[i] * (-1);
                        }
                        j++;
                    }
                }
                ghost_x[i] = ghost_x[i] + ghost_dx[i];
            }
            if (N_BLOCKS >= ghost_y[i]+ ghost_dy[i] && ghost_y[i] + ghost_dy[i] >=1){
                int x, y;
                int j = 0;

                for (x = 1; x <= 15; x += 1) {
                    for (y = 1; y <= 15; y += 1) {
                        if ((ghost_x[i] + ghost_dx[i] == x )&&(ghost_y[i] + ghost_dy[i] == y)&&(levelData[j] == 0)
                                ||((j+2)%10 ==9)) {
                            ghost_dy[i] = ghost_dy[i] * (-1);
                            ghost_dx[i] = ghost_dx[i] * (-1);
                        }
                        j++;
                    }
                }

                ghost_y[i] = ghost_y[i] + ghost_dy[i];
            }
            getColors()[ghost_x[i]][ghost_y[i]] = Ghost;

            if (pacman_x == ghost_x[i] &&pacman_y ==ghost_y[i] && inGame){
                death();
            }
        }
    }

    private void death() {

        lives--;

        if (lives == 0) {
            inGame = false;
        }

    }
}