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
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        final String hostNo;
        final int portNo;

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

        //host and port option
        JPanel option = new JPanel();
        option.setBorder(new EmptyBorder(50, 0, 0, 0));
        option.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel hl = new JLabel("host   ");
        JTextField hostOption = new JTextField("localhost", 10);
        hostNo = hostOption.getText().toString().trim();
        JLabel pl = new JLabel("port   ");
        JTextField portOption = new JTextField("1234", 10);
        String prtn = portOption.getText().toString().trim();
        portNo = Integer.parseInt(prtn);
        JButton connect = new JButton("connetct");

        option.add(hl);
        option.add(hostOption);
        option.add(pl);
        option.add(portOption);
        option.add(connect);

        JPanel conPane = new JPanel();
        conPane.setBorder(new EmptyBorder(50, 0, 0, 0));
        conPane.setFont(new Font("Arial", Font.BOLD, 20));
        JLabel con = new JLabel("connection status");
        JTextField conState = new JTextField("not connected", 10);
        conPane.add(con);
        conPane.add(conState);

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
        sendFile.setEnabled(false);

        JButton chooseFile = new JButton("choose file");
        chooseFile.setPreferredSize(new Dimension(150, 75));
        chooseFile.setFont(new Font("Arial", Font.BOLD, 20));
        chooseFile.setEnabled(false);

        jButtons.add(sendFile);
        jButtons.add(chooseFile);

        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (hostNo.isEmpty() || Integer.toString(portNo).isEmpty()) {
                    intro.setText("please select host and port address");
                } else {
                    try {
                        Socket socket = new Socket(hostNo, portNo);
                        if (socket.isConnected()) {
                            conState.setText("connected on port " + portNo);
                            sendFile.setEnabled(true);
                            chooseFile.setEnabled(true);
                            socket.close();
                        }
                    } catch (IOException ex) {
                        conState.setText("error occured");
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        });

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
                        if (hostNo.isEmpty() || Integer.toString(portNo).isEmpty()) {
                            fileName.setText("please select host and port address");
                        } else {

                            FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());

                            Socket socket = new Socket(hostNo, portNo);

                            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                            String fileName = fileToSend[0].getName();

                            byte[] fileNameBytes = fileName.getBytes();

                            byte[] fileBytes = new byte[(int) fileToSend[0].length()];

                            fileInputStream.read(fileBytes);

                            dataOutputStream.writeInt(fileNameBytes.length);

                            dataOutputStream.write(fileNameBytes);

                            dataOutputStream.writeInt(fileBytes.length);

                            dataOutputStream.write(fileBytes);
                        }
                    } catch (IOException error) {
                        fileName.setText("error occured when sending the file " + fileToSend[0].getName());
                        error.printStackTrace();
                    }
                }

            }
        });

        window.add(intro);
        window.add(option);
        window.add(conPane);
        window.add(fileName);
        window.add(jButtons);
        window.setVisible(true);
    }

}
