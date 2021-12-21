package ru.congas.output;

import org.fusesource.jansi.Ansi;

import java.io.PrintWriter;

/**
 * @author Mr_Told
 */
public class LineBuilder {

    final String S_CDL = Ansi.ansi().cursorDownLine().toString();

    final StringBuilder line;
    final PrintWriter out;
    int prints = 0;

    public LineBuilder(int width, PrintWriter out) {
        line = new StringBuilder(width + 1);
        this.out = out;
    }

    public LineBuilder append(String a) {
        line.append(a);
        return this;
    }

    public LineBuilder append(char c) {
        line.append(c);
        return this;
    }

    public LineBuilder append(int i) {
        line.append(i);
        return this;
    }

    public void print() {
        out.write(S_CDL);
        out.write(line.toString());
        line.setLength(0);
        prints++;
    }

    public void end() {
        //out.write(Ansi.ansi().cursorDown(prints).toString().getBytes());
    }

}
