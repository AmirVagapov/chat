package ru.geekbrains.java_level_one.lesson_seven;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

/**
 * Created by Tom on 02.08.2017.
 */
public class Map extends JPanel{

    public static final int MODE_H_V_A = 0;
    public static final int MODE_H_V_H = 1;

    private static final int EMPTY_DOT = 0;
    private static final int AI_DOT = 1;
    private static final int HUMAN_DOT = 2;
    private static final  int HUMAN_2_DOT = 3;
    private static final Random RANDOM = new Random();

    private static final int DOTS_PADDING = 5;

    private static final int DRAW = 0;
    private static final int HUM_WIN = 1;
    private static final int AI_WIN = 2;
    private static final int HUM_2_WIN = 3;
    private static int stateGameOver;
    private static boolean gameOver;
    private static int count = 2;

    private static final Font font = new Font("Times new roman", Font.BOLD, 48);

    private static final String MSG_DRAW = "Ничья";
    private static final  String MSG_HUM = "Победил игрок";
    private static final  String MSG_HUM_2 = "Победил игрок 2";
    private static final  String MSG_AI = "Победил Компьютер";


    int[][] field;
    int fieldSizeX;
    int fieldSizeY;
    int winLength;
    int mode;

    int cellHeight;
    int cellWidth;

    boolean isInitialized = false;


    Map(){

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(count % 2 == 0) {
                    update(e);
                    repaint();
                }
                else if(mode == MODE_H_V_H){
                    playerTwoTurn(e);
                    repaint();
                }

            }
        });

        repaint();



    }

    void update(MouseEvent e) {

        int cellX = e.getX() / cellWidth;
        int cellY = e.getY() / cellHeight;
        if (!isValidCell(cellX, cellY) || !isEmptyCell(cellX, cellY)) return;
        field[cellY][cellX] = HUMAN_DOT;
        repaint();

        if (checkWin(HUMAN_DOT)) {
            stateGameOver = HUM_WIN;
            gameOver = true;
            return;
        }
        if (isMapFull()) {
            stateGameOver = DRAW;
            gameOver = true;
            return;
        }
        if (mode == MODE_H_V_A) {
            aiTurn();
            repaint();
            if (checkWin(AI_DOT)) {
                stateGameOver = AI_WIN;
                gameOver = true;
                return;
            }
            if (isMapFull()) {
                stateGameOver = DRAW;
            }
        } else if(mode == MODE_H_V_H) count++;
    }



    void playerTwoTurn(MouseEvent u){

            int playerTwoCellX = u.getX() / cellWidth;
            int playerTwoCellY = u.getY() / cellHeight;
            if (!isValidCell(playerTwoCellX, playerTwoCellY) || !isEmptyCell(playerTwoCellX, playerTwoCellY)) return;
            field[playerTwoCellY][playerTwoCellX] = HUMAN_2_DOT;
            repaint();
        if (checkWin(HUMAN_2_DOT)) {
            stateGameOver = HUM_2_WIN;
            gameOver = true;
            return;
        }
        if (isMapFull()) {
            stateGameOver = DRAW;
            gameOver = true;
            return;
        }
        count++;
    }

    @Override
    protected void paintComponent (Graphics g){
        super.paintComponent(g);
        render(g);
    }


    void startNewGame(int mode, int fieldSizeX, int fieldSizeY, int winLength ){
        this.fieldSizeX = fieldSizeX;
        this.fieldSizeY = fieldSizeY;
        this.winLength = winLength;
        this.mode = mode;
        field = new int[fieldSizeY][fieldSizeX];
        isInitialized = true;
        gameOver = false;
        repaint();
    }

    void render(Graphics g){
        if(!isInitialized) return;

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        cellHeight = panelHeight / fieldSizeY;
        cellWidth = panelWidth / fieldSizeX;


        for (int i = 0; i < fieldSizeY; i++) {
            int y = i * cellHeight;
            g.drawLine(0, y, panelWidth, y);
        }

        for(int i = 0; i < fieldSizeX; i++){
            int x = i * cellWidth;
            g.drawLine(x, 0, x, panelHeight);
        }
        for(int y = 0; y < fieldSizeY; y++){
            for(int x = 0; x < fieldSizeX; x++){
                if(isEmptyCell(x,y)) continue;
                if(field[y][x] == HUMAN_DOT){
                    g.setColor(Color.BLUE);
                }else if(field[y][x] == AI_DOT) {
                    g.setColor(Color.RED);
                }else if(field[y][x] == HUMAN_2_DOT) {
                    g.setColor(Color.GREEN);
                }else{
                    throw new RuntimeException("Неизвестный символ " + field[y][x] + "в ячейке: " + y + x);
                }
                g.fillOval(x * cellWidth + DOTS_PADDING, y * cellHeight + DOTS_PADDING,
                        cellWidth - DOTS_PADDING *2, cellHeight - DOTS_PADDING * 2);
            }
        }

        if(gameOver) {
            showMessageGameOver();
            isInitialized = false;
            repaint();
        }

    }

    void showMessageGameOver() {
        switch (stateGameOver) {
            case DRAW:
                JOptionPane.showMessageDialog(null, MSG_DRAW);
                break;
            case HUM_WIN:
                JOptionPane.showMessageDialog(null, MSG_HUM);
                break;
            case AI_WIN:
                JOptionPane.showMessageDialog(null, MSG_AI);
                break;
            case HUM_2_WIN:
                JOptionPane.showMessageDialog(null, MSG_HUM_2);
                break;
            default:
                throw new RuntimeException("Unknown gameover state: " + stateGameOver);


        /*
        g.setColor(Color.GRAY);
        g.setFont(font);
        g.fillRect(0, 200, getWidth(),70);
        g.setColor(Color.YELLOW);
        switch (stateGameOver){
            case DRAW:
                g.drawString(MSG_DRAW, 180, getHeight() / 2);
                break;
            case HUM_WIN:
                g.drawString(MSG_HUM, 70, getHeight() / 2);
                break;
            case AI_WIN:
                g.drawString(MSG_AI, 20, getHeight() / 2);
                break;
            default:
                throw new RuntimeException("Unknown gameover state: " + stateGameOver);

        }*/
        }
    }


    // Ход компьютера
    private void aiTurn() {
        if(turnAIWinCell()) return;		// проверим, не выиграет-ли игрок на следующем ходу
        if(turnHumanWinCell()) return;	// проверим, не выиграет-ли комп на следующем ходу
        int x, y;
        do {							// или комп ходит в случайную клетку
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[y][x] = AI_DOT;
    }
    // Проверка, может ли выиграть комп
    private boolean turnAIWinCell() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(j, i)) {				// поставим нолик в каждую клетку поля по очереди
                    field[i][j] = AI_DOT;
                    if (checkWin(AI_DOT)) return true;	// если мы выиграли, вернём истину, оставив нолик в выигрышной позиции
                    field[i][j] = EMPTY_DOT;			// если нет - вернём обратно пустоту в клетку и пойдём дальше
                }
            }
        }
        return false;
    }
    // Проверка, выиграет-ли игрок своим следующим ходом
    private boolean turnHumanWinCell() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(j, i)) {
                    field[i][j] = HUMAN_DOT;			// поставим крестик в каждую клетку по очереди
                    if (checkWin(HUMAN_DOT)) {			// если игрок победит
                        field[i][j] = AI_DOT;			// поставить на то место нолик
                        return true;
                    }
                    field[i][j] = EMPTY_DOT;			// в противном случае вернуть на место пустоту
                }
            }
        }
        return false;
    }
    // проверка на победу
    private boolean checkWin(int c) {
        for (int i = 0; i < fieldSizeX; i++) {			// ползём по всему полю
            for (int j = 0; j < fieldSizeY; j++) {
                if (checkLine(i, j, 1, 0, winLength, c)) return true;	// проверим линию по х
                if (checkLine(i, j, 1, 1, winLength, c)) return true;	// проверим по диагонали х у
                if (checkLine(i, j, 0, 1, winLength, c)) return true;	// проверим линию по у
                if (checkLine(i, j, 1, -1, winLength, c)) return true;	// проверим по диагонали х -у
            }
        }
        return false;
    }
    // проверка линии
    private boolean checkLine(int x, int y, int vx, int vy, int len, int c) {
        final int far_x = x + (len - 1) * vx;			// посчитаем конец проверяемой линии
        final int far_y = y + (len - 1) * vy;
        if (!isValidCell(far_x, far_y)) return false;	// проверим не выйдет-ли проверяемая линия за пределы поля
        for (int i = 0; i < len; i++) {					// ползём по проверяемой линии
            if (field[y + i * vy][x + i * vx] != c) return false;	// проверим одинаковые-ли символы в ячейках
        }
        return true;
    }
    // ничья?
    private boolean isMapFull() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[i][j] == EMPTY_DOT) return false;
            }
        }
        return true;
    }
    // ячейка-то вообще правильная?
    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }
    // а пустая?
    private boolean isEmptyCell(int x, int y) {
        return field[y][x] == EMPTY_DOT;
    }


}
