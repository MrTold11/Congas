package ru.congas.anthology;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Field {
    String s = "";
    //ArrayList<ArrayList> field = new ArrayList<>();
    char[][] field = new char[31][29];

    public void read() {
        Path path = new File("C:\\Users\\mrtol\\Desktop\\Projects\\Congas\\anthology\\arcades\\src\\main\\java\\ru\\congas\\anthology\\field.txt").toPath();
        try {
            int i = 0;
            for (String line : Files.readAllLines(path)) {
                field[i] = line.toCharArray();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public char[][] field(){
        return this.field;
    }
}