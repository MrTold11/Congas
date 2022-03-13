package ru.congas.anthology;

import ru.congas.core.application.Bundle;
import ru.congas.core.application.PageActivity;
import ru.congas.core.input.keys.KeyPressed;
import ru.congas.core.output.canvas.Cell;
import ru.congas.core.output.modifier.Color;
import ru.congas.core.output.modifier.Style;
import ru.congas.core.output.widgets.TextView;
import ru.congas.core.output.widgets.dynamic.DynamicString;

import java.util.Random;

/**
 * @author Mr_Told
 */
public class Sapper extends PageActivity {

    private enum CellState {
        HIDDEN(Color.GREEN),
        OPEN(Color.TAN),
        FLAG(Color.RED);

        public final int color;

        CellState(int color) {
            this.color = color;
        }
    }

    private static class SCell {

        CellState state = CellState.HIDDEN;
        boolean hasMine = false;
        private int intValue;
        private char value; //0 - 8

        private final static int[] fgColors = new int[] {
            Color.WHITE, Color.BLUE, Color.FOREST, Color.RED, Color.VIOLET,
                Color.GOLD, Color.NAVY, Color.BLACK, Color.BLACK
        };

        public void render(Cell cell) {
            cell.setBackground(state.color);
            if (state == CellState.OPEN && value != '0')
                cell.setChar(value).setForeground(fgColors[intValue]);
            else if (state == CellState.FLAG)
                cell.setChar('!');
            else
                cell.setChar(' ');
        }

        public void setValue(int val) {
            value = Character.forDigit(val, 10);
        }

        public void countMines(SCell... cells) {
            intValue = 0;
            for (SCell cell : cells)
                if (cell.hasMine) intValue++;
            setValue(intValue);
        }

        public boolean unlocked() {
            return state == CellState.OPEN;
        }

        public void switchFlag() {
            if (state == CellState.FLAG)
                state = CellState.HIDDEN;
            else if (state == CellState.HIDDEN)
                state = CellState.FLAG;
        }

    }

    private enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        private final int dx, dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public void move(int[] from) {
            from[0] += dx;
            from[1] += dy;
        }
    }

    SCell[][] field;

    int pos_x;
    int pos_y;
    int undiscoveredCells;

    final int verticalOffset = 2;
    final DynamicString flagsText = new DynamicString("Flags: %d/%d", 0, 0);

    @Override
    public void onCreate(Bundle args) {
        super.onCreate(args);
        addWidget(new TextView(flagsText, new Style(Color.BLUE)));
        resetField(20, 20);
        generateMap((int) (field[0].length * field.length * 0.1));
    }

    private void resetField(int boardW, int boardH) {
        boardW = Math.max(5, boardW);
        boardH = Math.max(5, boardH);
        field = new SCell[boardH][boardW];
        for(int i = 0; i < field.length; i++)
            for (int j = 0; j < field[0].length; j++)
                field[i][j] = new SCell();

        pos_x = boardW / 2;
        pos_y = boardW / 2;
        render();
    }

    private void generateMap(int totalMines) {
        int fieldS = field[0].length * field.length;
        if (totalMines >= fieldS - 9)
            totalMines = fieldS - 10;
        if (totalMines < 1)
            totalMines = 1;
        undiscoveredCells = fieldS - totalMines;
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < totalMines; i++) {
            int x = random.nextInt(field[0].length);
            int y = random.nextInt(field.length);
            if (field[y][x].hasMine || (Math.abs(pos_x - x) < 3 && Math.abs(pos_y - y) < 3)) {
                i--;
                continue;
            }
            field[y][x].hasMine = true;
        }
        flagsText.updateValues(0, totalMines);

        int width = field[0].length;
        int height = field.length;
        int w1 = width - 1;
        int h1 = height - 1;
        int w2 = width - 2;
        int h2 = height - 2;

        field[0][0].countMines(field[0][1], field[1][0], field[1][1]);
        field[0][w1].countMines(field[0][w2], field[1][w1], field[1][w2]);
        field[w1][0].countMines(field[w1][1], field[h2][0], field[h2][1]);
        field[w1][w1].countMines(field[w1][w2], field[h2][w1], field[h2][w2]);

        for (int i = 1; i < w1; i++) {
            field[0][i].countMines(field[1][i],
                    field[0][i - 1], field[0][i + 1],
                    field[1][i - 1], field[1][i + 1]);
            field[h1][i].countMines(field[h2][i],
                    field[h1][i - 1], field[h1][i + 1],
                    field[h2][i - 1], field[h2][i + 1]);
        }
        for (int i = 1; i < h1; i++) {
            field[i][0].countMines(field[i][1],
                    field[i - 1][0], field[i + 1][0],
                    field[i - 1][1], field[i + 1][1]);
            field[i][w1].countMines(field[w2][1],
                    field[i - 1][w1], field[i + 1][w1],
                    field[i - 1][w2], field[i + 1][w2]);
        }

        for (int i = 1; i < h1; i++) {
            for (int j = 1; j < w1; j++)
                field[i][j].countMines(
                        field[i - 1][j - 1], field[i - 1][j], field[i - 1][j + 1],
                        field[i][j - 1], field[i][j + 1],
                        field[i + 1][j - 1], field[i + 1][j], field[i + 1][j + 1]
                );
        }

        render();
    }

    @Override
    public void onResize(int oldWidth, int oldHeight, int newWidth, int newHeight) {
        if (newHeight - 1 - verticalOffset > field.length && newWidth - 1 > field[0].length)
            screen.getCanvas().clear(newWidth, newHeight);
        render();
    }

    @Override
    protected void render() {
        for(int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++)
                renderCell(j, i);
        }
        renderPos();
        super.render();
    }

    private void renderCell(int x, int y) {
        field[y][x].render(getCanvas().getCell(y + verticalOffset, x));
    }

    private void renderPos() {
        getCanvas().getCell(pos_y + verticalOffset, pos_x)
                .setBackground(field[pos_y][pos_x].state == CellState.FLAG ? Color.ORANGE : Color.YELLOW);
    }

    private void openCell(int x, int y, boolean primary) {
        if (x < 0 || x >= field[0].length
                || y < 0 || y >= field.length
                || field[y][x].unlocked()
                || field[y][x].state == CellState.FLAG)
            return;
        if (field[y][x].hasMine) {
            if (primary) {
                getCanvas().fill(new Style(Color.RED), ' ');
                screen.updateCanvas();
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException ignored) {}
                closeActivity();
            }
            return;
        }
        field[y][x].state = CellState.OPEN;
        undiscoveredCells--;
        renderCell(x, y);
        if (field[y][x].intValue == 0) {
            openCell(x - 1, y - 1, false);
            openCell(x - 1, y + 1, false);
            openCell(x + 1, y - 1, false);
            openCell(x + 1, y + 1, false);
            openCell(x - 1, y, false);
            openCell(x + 1, y, false);
            openCell(x, y - 1, false);
            openCell(x, y + 1, false);
        }
        if (primary) {
            renderPos();
            screen.updateCanvas();
            if (undiscoveredCells == 0) {
                getCanvas().fill(new Style(Color.GREEN), ' ');
                screen.updateCanvas();
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException ignored) {}
                closeActivity();
            }
        }
    }

    private void movePos(Direction direction) {
        int[] nPos = new int[] {pos_x, pos_y};
        boolean blocked = true;
        do {
            direction.move(nPos);
            if (nPos[0] < 0 || nPos[1] < 0
                    || nPos[1] >= field.length || nPos[0] >= field[0].length) {
                if (blocked) return;
                nPos = new int[] {pos_x, pos_y};
                direction.move(nPos);
                break;
            }
            blocked = false;
        } while (field[nPos[1]][nPos[0]].unlocked());
        renderCell(pos_x, pos_y);
        pos_x = nPos[0];
        pos_y = nPos[1];
        renderPos();
        screen.updateCanvas();
    }

    @Override
    public boolean handle(KeyPressed event) {
        switch (event.getDefinedKey()) {
            case ESCAPE:
                closeActivity();
                return true;
            case KEY_A:
            case LEFT:
                movePos(Direction.LEFT);
                return true;
            case KEY_W:
            case UP:
                movePos(Direction.UP);
                return true;
            case KEY_D:
            case RIGHT:
                movePos(Direction.RIGHT);
                return true;
            case KEY_S:
            case DOWN:
                movePos(Direction.DOWN);
                return true;
            case ENTER:
                openCell(pos_x, pos_y, true);
                return true;
            case SPACE:
                flagsText.editValues()[0] = ((int) flagsText.editValues()[0]) +
                        (field[pos_y][pos_x].state == CellState.FLAG ? -1 : 1);
                field[pos_y][pos_x].switchFlag();
                renderCell(pos_x, pos_y);
                renderPos();
                super.render();
                return true;
        }
        return false;
    }

}
