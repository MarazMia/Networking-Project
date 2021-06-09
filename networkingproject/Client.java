/*
Client side code for choosing 
and sending files over server
 */
package networkingproject;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Maraz Mia
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        final File[] fileToSend = new File[1];

        //our project window
        JFrame window = new JFrame("Networking Project");
        window.setSize(450, 450);
        window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //introductory text
        JLabel intro = new JLabel("Client Side GUI");
        intro.setFont(new Font("Arial", Font.BOLD, 25));
        intro.setBorder(new EmptyBorder(20, 0, 10, 0));
        intro.setAlignmentX(Component.CENTER_ALIGNMENT);

        //file name label
        JLabel fileName = new JLabel("choose a file to send...");
        fileName.setBorder(new EmptyBorder(50, 0, 0, 0));
        fileName.setFont(new Font("Arial", Font.BOLD, 20));
        fileName.setAlignmentX(Component.CENTER_ALIGNMENT);

        //button part
        JPanel jButtons = new JPanel();
        jButtons.setBorder(new EmptyBorder(75, 0, 10, 0));

        JButton sendFile = new JButton("send file");
        sendFile.setPreferredSize(new Dimension(150, 75));
        sendFile.setFont(new Font("Arial", Font.BOLD, 20));

        JButton chooseFile = new JButton("choose file");
        chooseFile.setPreferredSize(new Dimension(150, 75));
        chooseFile.setFont(new Font("Arial", Font.BOLD, 20));

        jButtons.add(sendFile);
        jButtons.add(chooseFile);

        chooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser expectedFile = new JFileChooser("choose a file...");

                if (expectedFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    fileToSend[0] = expectedFile.getSelectedFile();
                    fileName.setText("your selected file is " + fileToSend[0].getName());
                }
            }
        });

        sendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (fileToSend[0] == null) {
                    fileName.setText("please choose a file first!!!");
                } else {
                    try {
                        FileInputStream fis = new FileInputStream(fileToSend[0].getAbsolutePath());  
                        Socket socket = new Socket("localhost",1011);
                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                        
                        String FileName = fileToSend[0].getName();
                        byte[] FileNameBytes = FileName.getBytes();
                        byte[] FileBytes = new byte[(int)fileToSend[0].length()];
                        
                        fis.read(FileBytes);
                        dos.writeInt(FileNameBytes.length);
                        dos.write(FileNameBytes);
                        dos.write(FileBytes);
                    } catch (IOException error) {
                        error.printStackTrace();
                    }
                }

            }
        });

        window.add(intro);
        window.add(fileName);
        window.add(jButtons);
        window.setVisible(true);
    }

}
