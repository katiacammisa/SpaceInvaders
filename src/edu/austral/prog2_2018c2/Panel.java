package edu.austral.prog2_2018c2;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.util.*;

public class Panel implements ActionListener {

        private JFrame frame;
        private JPanel buttonPane, fieldsPanel;
        private JLabel cash;
        private JTextField cashField;
        private JButton ok;
        private static int score;

        public Panel(Integer score) {

            this.score = score;

            frame = new JFrame("");
            buttonPane = new JPanel();
            fieldsPanel = new JPanel();
            cash = new JLabel("Insert your name:");
            cashField = new JTextField("Player");
            ok = new JButton("OK");

            fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.PAGE_AXIS));
            buttonPane.setLayout(new FlowLayout());

            fieldsPanel.add(cash);
            fieldsPanel.add(cashField);
            buttonPane.add(ok);
            frame.add(fieldsPanel, BorderLayout.PAGE_START);
            frame.add(buttonPane, BorderLayout.PAGE_END);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.setBounds(542, 150, 357, 100);

            ok.addActionListener(this);
        }
        public static void main(String args[]) {
            new Panel(score);
        }



    @Override
    public void actionPerformed(ActionEvent e) {
        frame.setVisible(false);
        ScoreData data = new ScoreData(cashField.getText(), score);
    }
}
