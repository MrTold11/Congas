package ru.congas.anthology;

/**
 * @author Jailflat
 */
public class Tictac {

    static String[][] lines = new String[3][3];
    static int turn;

    public static int close() {
        //выиграл такой-то
        //возврат либо рестарт
        return 0;
    }

    public static void launch() {
        turn = 1;
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                lines[i][j] = "";
        //Выбор игроками символов
    }

    private static int newTurn() {
        if(turn != 10) {
       /*
        if(turn % 2 == 1) ход игрока 1 (Х)
        else ход игрока 2 (О)

       */
            //прием символа
            //запись символа
            if (Tictac.checkGameState() != 0) return Tictac.close();
            turn++;
            Tictac.updateBoard();
            return Tictac.newTurn();
        }
        else return Tictac.close();
    }

    private static void updateBoard() {
        //вывод поля на экран
    }

    private static int checkGameState() {
        String line = "";

        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++)
                if (lines[i][j].equals("")) line += lines[i][j];

            if(line.equals("XXX")) return 1;
            else if(line.equals("OOO")) return 2;

        }

        line = lines[1][1] + lines[2][2] + lines[3][3];
        if(line.equals("XXX")) return 1;
        else if(line.equals("OOO")) return 2;

        line = lines[3][1] + lines[2][2] + lines[1][3];
        if(line.equals("XXX")) return 1;
        else if(line.equals("OOO")) return 2;

        return 0;
    }



}
