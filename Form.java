package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Form extends JFrame {
    //Panel that will be used for all graphical work in the program
    Palette graphicsPalette;

    public Form() {
        graphicsPalette = new Palette();

        //initalize graohicsPalette
        graphicsPalette.setFocusable(true);

        //fill form with graphics palette
        graphicsPalette.setSize(WIDTH, HEIGHT);
        add(graphicsPalette);
        setVisible(true);

        //Set form size 850 pixels by 650 pixels
        setSize(850, 650);
        setResizable(false);
        JButton button = new JButton("Info");
        graphicsPalette.add(button).setBounds(5, 205, 150, 50);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame window = new JFrame("Information");
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setLayout(new BorderLayout());
                JTextArea area = new JTextArea(15, 10);
                area.setText("Для увеличения скорости - PgUp \nДля торможения - PgDn\nДля разворота - left/right\nR - возобновить игру\nX - завершить игру\n\nИгра продолжается до тех пор, пока одна из машин не ударится\n в другую");
                window.add(new JScrollPane(area), BorderLayout.CENTER);
                window.pack();
                window.setSize(400, 400);
                window.setVisible(true);
                window.setLocationRelativeTo(null);
            }
        });
        //Alow form to close on the pressing of "X"
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    //Close other clients
                    MessageSender.sendMessage("exit");
                } catch (Exception ignored) {
                }
            }
        });
    }


    public void Restart() {
        graphicsPalette.Restart();
    }

    public void MakeChanges(String[] str) {
        //Change other car details
        graphicsPalette.MakeChanges(str);
    }
}


