package edu.austral.prog2_2018c2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PanelHighScores implements ActionListener, Commons {

    private JFrame frame;
    private JPanel buttonPane, fieldsPanel;
    private JButton done;
    private JLabel text;
    private boolean active;
    private Scanner scanner;
    List<String> scoring = new ArrayList<>();
    private static final String FILENAME = "Scores.txt";

    public PanelHighScores(){

        frame = new JFrame("HighScores");
        buttonPane = new JPanel();
        fieldsPanel = new JPanel();
        done = new JButton("Done");
        text = new JLabel();

        try {
            scanner();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, 2));
        buttonPane.setLayout(new FlowLayout());

        buttonPane.add(done);
        frame.add(text);
        frame.add(fieldsPanel, BorderLayout.PAGE_START);
        frame.add(buttonPane, BorderLayout.PAGE_END);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setBounds(542, 230, BOARD_WIDTH, BOARD_HEIGHT);
        frame.getContentPane().setBackground(Color.black);

        done.addActionListener(this);
    }
    public static void main(String args[]) {
        new PanelStarter();
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        frame.setVisible(false);
        active = true;
    }

    public void scanner() throws IOException {

        FileReader fr = new FileReader(FILENAME);
        BufferedReader br = new BufferedReader(fr);

        String thisLine;
        while ((thisLine = br.readLine()) != null) {
            scoring.add(thisLine);
        }
        text.setText(toString());
        text.setHorizontalAlignment(SwingConstants.CENTER);
        br.close();
    }

    public String toString(){
        String result = "<html><font color='white'>";
        for (int i = 0; i < scoring.size() ; i++) {
            result += (i+1) + ") " + scoring.get(i) + "<br/>";
        }
        result += "</font></html>";

        return result;
    }
}
