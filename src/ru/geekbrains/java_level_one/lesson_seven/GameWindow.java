package ru.geekbrains.java_level_one.lesson_seven;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Tom on 02.08.2017.
 */
public class GameWindow extends JFrame {

    private static final int WIN_HEIGHT = 555;
    private static final int WIN_WIDTH = 506;
    private static final int WIN_POX_X = 500;
    private static final int WIN_POS_Y = 100;

    private static StartNewGameWindow startNewGameWindow;
    private static  Map field;



    GameWindow(){

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setBounds(WIN_POX_X, WIN_POS_Y, WIN_WIDTH, WIN_HEIGHT);
        setTitle("TicTacToe");
        setResizable(false);

        startNewGameWindow = new StartNewGameWindow(this);
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));

        JButton btnNewGame = new JButton("Start new game");
        bottomPanel.add(btnNewGame);
        btnNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGameWindow.setVisible(true);
            }
        });

        JButton btnExit = new JButton("Exit game");
        bottomPanel.add(btnExit);
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        field = new Map();
        add(field, BorderLayout.CENTER); ///по центру ставится по умолчанию.
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    void startNewGame(int mode, int fieldSizeX, int fieldSizeY, int winLength ){
        field.startNewGame(mode, fieldSizeX, fieldSizeY, winLength);
    }
}
