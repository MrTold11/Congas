package ru.congas.anthology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Hunter extends Settings{

    public Hunter(){
        this.x = 9;
        this.y = 13;
        this.my = -1;
        this.mx = 0;
        this.lives = 1;
    }

    public void duringBoost(char[][] field){
        this.mx *= -1;
        this.my *= -1;
//        while (newcheck(field)){
//            this.mx *= -1;
//            this.my *= -1;
//        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setMx(int mx) {
        this.mx = mx;
        this.my = 0;
    }

    public void setMy(int my) {
        this.my = my;
        this.mx = 0;
    }

    public void move(char[][] field){
        //while (newcheck(field)){
            
//            if (this.y == y){
//                if (this.my != 0){
//                    int c;
//                    c = this.my;
//                    this.my = this.mx;
//                    this.mx = c;
//                    if (newcheck(field)){
//                        this.mx *= -1;
//                        this.my *= -1;
//                    }
//
//                }
//                else if(this.mx != 0 ){
//                    System.out.println(this.mx + this.my);
//                    int c;
//                    c = this.my;
//                    this.my = this.mx;
//                    this.mx = c;
//
//                    this.mx *= -1;
//                    this.my *= -1;
//
//                    if (newcheck(field)){
//                        this.mx *= -1;
//                        this.my *= -1;
//                    }
//                    System.out.println(this.mx + this.my);
//                }
//            }
//            else if (this.x == x){
//                if (this.my != 0){
//                    int c;
//                    c = this.my;
//                    this.my = this.mx;
//                    this.mx = c;
//                }
//                else if(this.mx != 0 ){
//                    System.out.println(this.mx + this.my);
//                    int c;
//                    c = this.my;
//                    this.my = this.mx;
//                    this.mx = c;
//
//                    this.mx *= -1;
//                    this.my *= -1;
//                    System.out.println(this.mx + this.my);
//                }
//            }
//            else if (this.mx == 0){
//                if (this.my == -1) {
//                    if (field[this.y][this.x - 1] == ' ') {
//                        this.mx = -1;
//                        this.my = 0;
//                    } else if (field[this.y][this.x + 1] == ' ') {
//                        this.mx = 1;
//                        this.my = 0;
//                    }
//                }
//                else if (this.my == 1) {
//                    if (field[this.y][this.x + 1] == ' ') {
//                        this.mx = 1;
//                        this.my = 0;
//                    }
//                    else{
//                        if (field[this.y][this.x - 1] == ' ') {
//                            this.mx = -1;
//                            this.my = 0;
//                        }
//                    }
//                }
//            }
//            else if (this.my ==0){
//                if (this.mx == -1){
//                    if (field[this.y + 1][this.x] == ' ') {
//                        this.mx = 0;
//                        this.my = 1;
//                    } else if (field[this.y -1][this.x] == ' ') {
//                        this.mx = 0;
//                        this.my = -1;
//                    }
//                }
//                else if (this.mx == 1){
//                    if (field[this.y - 1][this.x] == ' ') {
//                        this.mx = 0;
//                        this.my = -1;
//                    } else if (field[this.y + 1][this.x] == ' ') {
//                        this.mx = 0;
//                        this.my = 1;
//                    }
//                }
//
//            }
//            else{
//                System.out.println("Eror xy");
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        //}
        this.y += this.my;
        this.x += this.mx;
    }

    public boolean newcheck(char[][] field){
        // field[y][x]
        //System.out.println(field[this.y + this.my][this.x + this.mx]);
        return field[this.y + this.my][this.x + this.mx] != ' ' && field[this.y + this.my][this.x + this.mx] != '.' && field[this.y + this.my][this.x + this.mx] != '·';
    }

    //new
    public boolean fork(char[][] field){
        //choise at the fork
        // field[y][x]
        if (this.mx != 0){
            if(field[this.y + 1][this.x] == ' ' || field[this.y - 1][this.x] == ' '){
                return true;
            }
        }
        if (this.my != 0)
            return field[this.y][this.x + 1] == ' ' || field[this.y][this.x - 1] == ' ';

        return false;
    }

}