package Client;

import javax.swing.*;
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

        //Alow form to close on the pressing of "X"
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    //Close other clients
                    MessageSender.sendMessage("exit");
                } catch (Exception ex) {
                }
            }
        });
    }

    public void Restart() {
        graphicsPalette.Restart();
    }

    public void MakeChanges(String[] str) throws Exception {
        //Change other car details
        graphicsPalette.MakeChanges(str);
    }
}


