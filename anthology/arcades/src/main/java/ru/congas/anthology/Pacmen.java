package ru.congas.anthology;

import java.util.ArrayList;
import java.util.List;

public class Pacmen extends Settings {

    List<int[]> mass = new ArrayList<>();

    public Pacmen(){
        this.x = 2;
        this.y = 1;
        this.mx = 1;
        this.lives = 5;
        killHunter = false;
    }

    public void isDead() {
        this.isDead = true;
        this.isVisible = false;
    }

    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }

    public void Boost(){
        if (field[this.y][this.x] == '·'){
            mass.add(new int[] {this.x,this.y});
        }
    }

    public void setMx(int mx) {
        this.mx = mx;
        this.my = 0;
    }

    public void setMy(int my) {
        this.my = my;
        this.mx = 0;
    }

    public void move(char[][] field) {
        if (check(field)){
            this.y += this.my;
            this.x += this.mx;
        }
    }

    //new
    public boolean check(char[][] field) {

        if (field[this.my + this.y][this.x + this.mx] != ' ' && field[this.my + this.y][this.x + this.mx] !='·'){
            this.mx = 0;
            this.my = 0;
            return false;
        }
        return true;
    }
}