/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkingproject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {

    // Array list to hold information about the files received.
    static ArrayList<MyFile> myFiles = new ArrayList<>();
    static ArrayList<MyFile> allFiles = new ArrayList<>();
    static ServerSocket serverSocket;
    static Socket socket;
    static DataInputStream dis;
    static FileOutputStream fos;
    static OutputStream os;

    /*Server() throws IOException{
        //fileNames();
        serverSocket = new ServerSocket(1234);
        socket = serverSocket.accept();
        dis = new DataInputStream(socket.getInputStream());
        fos = new FileOutputStream("src/server_files/");
        //sendFile();
    }*/
    public static void main(String[] args) throws IOException {

        int fileId = 0;

        JFrame jFrame = new JFrame("Server");

        jFrame.setSize(400, 400);

        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();

        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        JScrollPane jScrollPane = new JScrollPane(jPanel);

        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel jlTitle = new JLabel("File Receiver");

        jlTitle.setFont(new Font("Arial", Font.BOLD, 25));

        jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));

        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        jFrame.add(jlTitle);
        jFrame.add(jScrollPane);

        jFrame.setVisible(true);

        fileNames();
        System.out.println(allFiles.size());
        serverSocket = new ServerSocket(1234);
        socket = serverSocket.accept();
        
        os = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(allFiles);

        while (true) {

            try {

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                int fileNameLength = dataInputStream.readInt();

                if (fileNameLength > 0) {

                    byte[] fileNameBytes = new byte[fileNameLength];

                    dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);

                    String fileName = new String(fileNameBytes);

                    int fileContentLength = dataInputStream.readInt();

                    if (fileContentLength > 0) {

                        byte[] fileContentBytes = new byte[fileContentLength];

                        dataInputStream.readFully(fileContentBytes, 0, fileContentBytes.length);

                        JPanel jpFileRow = new JPanel();
                        jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.X_AXIS));

                        JLabel jlFileName = new JLabel(fileName);
                        jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
                        jlFileName.setBorder(new EmptyBorder(10, 0, 10, 0));
                        if (getFileExtension(fileName).equalsIgnoreCase("txt")) {

                            jpFileRow.setName((String.valueOf(fileId)));
                            

                            jpFileRow.add(jlFileName);
                            jPanel.add(jpFileRow);
                            jFrame.validate();
                        } else {

                            jpFileRow.setName((String.valueOf(fileId)));

                            

                            jpFileRow.add(jlFileName);
                            jPanel.add(jpFileRow);

                            jFrame.validate();
                        }

                        myFiles.add(new MyFile(fileId, fileName, fileContentBytes, getFileExtension(fileName)));

                        fileId++;

                        File fileToSave = new File("src/server_files/" + fileName);
                        try {

                            FileOutputStream fileOutputStream = new FileOutputStream(fileToSave);

                            fileOutputStream.write(fileContentBytes);

                            fileOutputStream.close();

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }

    public static String getFileExtension(String fileName) {

        int i = fileName.lastIndexOf('.');

        if (i > 0) {

            return fileName.substring(i + 1);
        } else {
            return "No extension found.";
        }
    }

    public static void fileNames() throws IOException {
        int fileId = 0;
        File dir = new File("src/server_files");
        File[] listOfFiles = dir.listFiles();
        for (File file : listOfFiles) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
                String fileName = file.getName();
                byte[] fileContentBytes = new byte[(int) file.length()];
                if ((int) file.length() > 0) {
                    fileInputStream.read(fileContentBytes);
                }

                MyFile newFile = new MyFile(fileId, fileName, fileContentBytes, getFileExtension(fileName));
                newFile.setData(fileContentBytes);
                allFiles.add(newFile);
                fileId++;

                System.out.println(newFile.getId() + " " + newFile.getName() + " " + newFile.getData().length + " " + newFile.getFileExtension());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
