package ru.geekbrains.java_level_one.lesson_seven;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Math.min;

/**
 * Created by Tom on 02.08.2017.
 */
class StartNewGameWindow extends JFrame{
    private static final int WIN_HEIGHT = 330;
    private static final int WIN_WIDTH = 400;

    private static final int MIN_WIN_LENGTH = 3;
    private static final int MIN_FIELD_SIZE_X = 3;
    private static final int MAX_FIELD_SIZE_X = 10;
    private static final int MIN_FIELD_SIZE_Y = 3;
    private static final int MAX_FIELD_SIZE_Y = 10;
    private static final String STR_MIN_LEN = "Winning length: ";
    private static final String STR_FIELD_SIZE_X = "Field size by X: ";
    private static final String STR_FIELD_SIZE_Y = "Field size by Y: ";

    private JRadioButton jrbHumVsAi = new JRadioButton("Human vs AI", true);
    private JRadioButton jrbHumVsHum = new JRadioButton("Human vs Human");
    private ButtonGroup gameMode = new ButtonGroup();
    private JSlider slFieldSizeX;
    private JSlider slFieldSizeY;
    private JSlider slWinLength;


    private final GameWindow gameWindow;

    StartNewGameWindow(GameWindow gameWindow){
        this.gameWindow = gameWindow;
        setSize(WIN_WIDTH, WIN_HEIGHT);
        Rectangle gameWindowBounds = gameWindow.getBounds();
        int posX = (int)gameWindowBounds.getCenterX() - WIN_WIDTH / 2;
        int posY = (int)gameWindowBounds.getCenterY() - WIN_HEIGHT / 2;
        setLocation(posX, posY);
        setTitle("New game settings");
        setLayout(new GridLayout(14, 1));

        addGameControlsMode();
        addGameControlsFieldWinLength();
        JButton btnStartGame = new JButton("Start a game");
        add(btnStartGame);
        btnStartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnStartGameClick();
            }
        });


    }

    void btnStartGameClick(){
        int mode;
        if(jrbHumVsAi.isSelected())
            mode = Map.MODE_H_V_A;
        else if(jrbHumVsHum.isSelected())
            mode = Map.MODE_H_V_H;
        else
            throw new RuntimeException("No buttons selected");

        int fieldSizeX = slFieldSizeX.getValue();
        int fieldSizeY = slFieldSizeY.getValue();
        int winLength = slWinLength.getValue();
        gameWindow.startNewGame(mode, fieldSizeX, fieldSizeY, winLength);
        setVisible(false);
    }


    void addGameControlsMode(){
        add(new JLabel("Chose gaming mode"));
        gameMode.add(jrbHumVsAi);
        gameMode.add(jrbHumVsHum);
        add(jrbHumVsAi);
        add(jrbHumVsHum);
    }

    void addGameControlsFieldWinLength(){
        add(new JLabel("Choose field size by X:"));
        JLabel lblFieldSizeX = new JLabel(STR_FIELD_SIZE_X + MIN_FIELD_SIZE_X);
        add(lblFieldSizeX);
        slFieldSizeX = new JSlider(MIN_FIELD_SIZE_X, MAX_FIELD_SIZE_X, MIN_FIELD_SIZE_X);
        slFieldSizeX.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentFieldSizeX = slFieldSizeX.getValue();
                lblFieldSizeX.setText(STR_FIELD_SIZE_X + currentFieldSizeX);
                slWinLength.setMaximum(currentFieldSizeX);
            }
        });
        add(slFieldSizeX);

        add(new JLabel("Choose field size by Y:"));
        JLabel lblFieldSizeY = new JLabel(STR_FIELD_SIZE_Y + MIN_FIELD_SIZE_Y);
        add(lblFieldSizeY);
        slFieldSizeY = new JSlider(MIN_FIELD_SIZE_Y, MAX_FIELD_SIZE_Y, MIN_FIELD_SIZE_Y);
        slFieldSizeY.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentFieldSizeY = slFieldSizeY.getValue();
                lblFieldSizeY.setText(STR_FIELD_SIZE_Y + currentFieldSizeY);
                slWinLength.setMaximum(currentFieldSizeY);
            }
        });
        add(slFieldSizeY);

        add(new JLabel("Choose winning length:"));
        JLabel lblWinLen = new JLabel(STR_MIN_LEN + MIN_WIN_LENGTH);
        add(lblWinLen);
        slWinLength = new JSlider(MIN_WIN_LENGTH, min(slFieldSizeX.getValue(), slFieldSizeY.getValue()), MIN_WIN_LENGTH);
        slWinLength.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblWinLen.setText(STR_MIN_LEN + slWinLength.getValue());
            }
        });
        add(slWinLength);

    }


}
