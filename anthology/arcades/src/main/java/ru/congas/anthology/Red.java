package ru.congas.anthology;

import ru.congas.core.application.Activity;

public class Red extends Hunter {

    boolean exam;

    public void move(int x, int y, char[][] field, Settings settings, Activity activity) {
        //System.out.println(settings.isBooster());
        if (!settings.isBooster()) {
            if (this.x == x && this.y == y)
                activity.closeActivity();

            if (field[this.y][this.y] == '^') {
                this.mx = 0;
                this.my = -1;
            } else if (fork(field) && (!newcheck(field))) {
                //System.out.println("Choise at the fork");
                shortWay(x, y, field);
            } else while (newcheck(field)) {
                if (this.y == y) {
                    if (this.my != 0) {
                        int c;
                        c = this.my;
                        this.my = this.mx;
                        this.mx = c;
                        if (newcheck(field)) {
                            this.mx *= -1;
                            this.my *= -1;
                        } else {
                            for (int i = Math.min(this.x, x); i < Math.max(this.x, x); i++) {
                                if (field[this.y][i] != ' ') {
                                    exam = false;
                                    break;
                                }
                            }
                            if (exam) {
                                this.mx = (int) Math.signum(x - this.x);
                                this.my = 0;
                            }
                        }

                    } else if (this.mx != 0) {
                        //System.out.println(this.mx + this.my);
                        int c;
                        c = this.my;
                        this.my = this.mx;
                        this.mx = c;

                        this.my *= -1;

                        if (newcheck(field)) {
                            this.mx *= -1;
                            this.my *= -1;
                        } else {
                            //add 'for' before pacmen - no

                        }
                        //System.out.println(this.mx + this.my);
                    }
                } else if (this.x == x) {
                    if (this.my != 0) {
                        int c;
                        c = this.my;
                        this.my = this.mx;
                        this.mx = c;
                    } else if (this.mx != 0) {
                        //System.out.println(this.mx + this.my);
                        int c;
                        c = this.my;
                        this.my = this.mx;
                        this.mx = c;

                        this.my *= -1;
                        //System.out.println(this.mx + this.my);
                    }
                } else if (this.mx == 0) {
                    double a = Math.sqrt((this.x - 1 - x) * (this.x - 1 - x) + (this.y - y) * (this.y - y));
                    double b = Math.sqrt((this.x + 1 - x) * (this.x + 1 - x) + (this.y - y) * (this.y - y));
                    if (a > b) {
                        this.mx = 1;
                        this.my = 0;
                    } else if (a < b) {
                        this.mx = -1;
                        this.my = 0;
                    }
                    if (newcheck(field)) {
                        this.mx *= -1;
                        this.my *= -1;
                    }
                } else if (this.my == 0) {
                    double a = Math.sqrt((this.x - x) * (this.x - x) + (this.y - 1 - y) * (this.y - 1 - y));
                    double b = Math.sqrt((this.x - x) * (this.x - x) + (this.y + 1 - y) * (this.y + 1 - y));
                    if (a > b) {
                        //a > b and y != this.y
                        this.mx = 0;
                        this.my = 1;
                    } else if (a < b) {
                        this.mx = 0;
                        this.my = -1;
                    }
                    if (newcheck(field)) {
                        this.mx *= -1;
                        this.my *= -1;
                    }
                } else {
                    throw new RuntimeException("Error");
                }
            }
            this.y += this.my;
            this.x += this.mx;
        } else {
            move(field);
//            if (!newcheck(field)){
//                this.y += this.my;
//                this.x += this.mx;
//            }
            if (this.x == x && this.y == y){
                //System.out.println("Red hunter died");
                this.x = 13;
                this.y = 10;
            }
        }

    }

    public void shortWay(int x, int y, char[][] field){
        exam = true;
        if (this.y == y){
            for (int i = Math.min(this.x,x); i < Math.max(this.x,x); i++){
                if (field[this.y][i] != ' ') {
                    exam = false;
                    break;
                }
            }
            if (exam) {
                this.mx = (int) Math.signum(x - this.x);
                this.my = 0;
            }
        }
        else if (this.x == x){
            for (int i = Math.min(this.y,y); i < Math.max(this.y,y); i++){
                if (field[i][this.x] != ' ') {
                    exam = false;
                    break;
                }
            }
            if (exam) {
                this.mx = 0;
                this.my = (int) Math.signum(y - this.y);
            }
        }
        else if (this.mx == 0){
            double a = Math.sqrt((this.x - 1  - x)*(this.x - 1  - x) + (this.y - y)*(this.y - y));
            double b = Math.sqrt((this.x + 1 - x)*(this.x + 1  - x) + (this.y - y)*(this.y - y));
            double c = Math.sqrt((this.x- x)*(this.x  - x) + (this.y - y + this.my)*(this.y - y + this.my));

            if(c>a && c>b){
                //System.out.println("Вперед");
            }
            else if (a >= b && field[this.y][this.x + 1] == ' '){
                //System.out.println("Вправо");
                this.my = 0;
                this.mx = 1;
            }
            else if (a < b && field[this.y][this.x - 1] == ' '){
                //System.out.println("Влево");
                this.my = 0;
                this.mx = -1;
            }
        }
        else if (this.my == 0){
            double a = Math.sqrt((this.x - x)*(this.x - x) + (this.y - 1 - y)*(this.y - 1 - y));
            double b = Math.sqrt((this.x - x)*(this.x - x) + (this.y + 1 - y)*(this.y + 1 - y));
            double c = Math.sqrt((this.x- x)*(this.x  - x) + (this.y - y + this.my)*(this.y - y + this.my));

            if(c>a && c>b){
                //System.out.println("Вправа");
                //this.my = 0;
                //this.mx = 1;
            }
            else if (a >= b && field[this.y + 1][this.x] == ' '){
                //System.out.println("Вниз");
                this.my = 1;
                this.mx = 0;
            }
            else if (a < b && field[this.y - 1][this.x] == ' '){
                //System.out.println("Вверх");
                this.my = -1;
                this.mx = 0;
            }
        }
    }
}