package main.java;

import main.java.ImageAPI.ChannelsEnum;
import main.java.ImageAPI.ImageAPI;

import java.awt.*;
import javax.swing.*;

public class VoronCalc extends JFrame {
    private int voron = 0;
    private JLabel countLabel;
    private JButton addCrow;
    private JButton removeCrow;

    public VoronCalc(){
        super("Crow calculator");
        //Подготавливаем компоненты объекта
        countLabel = new JLabel("Crows:" + voron);
        addCrow = new JButton("Add Crow");
        removeCrow = new JButton("Remove Crow");

        //Подготавливаем временные компоненты
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        //Расставляем компоненты по местам
        buttonsPanel.add(countLabel, BorderLayout.NORTH); //О размещении компонент поговорим позже

        buttonsPanel.add(addCrow);
        buttonsPanel.add(removeCrow);

        add(buttonsPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws Exception {
//        VoronCalc app = new VoronCalc();
//        app.setVisible(true);
//        app.pack(); //Эта команда подбирает оптимальный размер в зависимости от содержимого окна
        ImageAPI app = new ImageAPI();
        app.loadImage(
                "color.jpg",
//                "example.jpg",
                ChannelsEnum.Color
        );
        System.out.println(1);
    }
}