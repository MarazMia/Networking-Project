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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Maraz Mia
 */
public class Client {

    static Socket socket;
    static ArrayList<MyFile> allFiles = new ArrayList<>();
    static File[] fileToSend = new File[1];
    static String hostNo;
    static int portNo;
    static String message;
    static JFrame jFramel;
    static JPanel jPanell;
    static JScrollPane jScrollPane;
    static JLabel jlTitle;
    static JPanel jpFileRow;
    static JLabel jlFileName;
    static JButton sendFile;
    static JButton chooseFile;
    static JButton seeFile;
    static JTextField conState;
    static DataOutputStream dataOutputStream = null;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //our project window
        JFrame window = new JFrame("Networking Project");
        window.setSize(450, 450);
        window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
        window.getContentPane().setBackground(Color.GREEN);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //introductory text
        JLabel intro = new JLabel("Client Side GUI");
        intro.setFont(new Font("Arial", Font.BOLD, 25));
        intro.setBorder(new EmptyBorder(5, 0, 5, 0));
        intro.setAlignmentX(Component.CENTER_ALIGNMENT);

        //host and port option
        JPanel option = new JPanel();
        option.setBorder(new EmptyBorder(5, 0, 0, 0));
        option.setFont(new Font("Arial", Font.BOLD, 20));
        option.setBackground(Color.RED);

        JLabel hl = new JLabel("host   ");
        JTextField hostOption = new JTextField("localhost", 10);

        JLabel pl = new JLabel("port   ");
        JTextField portOption = new JTextField("1234", 10);
        JButton connect = new JButton("connetct");

        option.add(hl);
        option.add(hostOption);
        option.add(pl);
        option.add(portOption);
        option.add(connect);

        JPanel conPane = new JPanel();
        conPane.setBorder(new EmptyBorder(5, 0, 0, 0));
        conPane.setFont(new Font("Arial", Font.BOLD, 20));
        conPane.setBackground(Color.GREEN);
        conPane.setAlignmentY(Component.CENTER_ALIGNMENT);
        JLabel con = new JLabel("connection status");
        conState = new JTextField("not connected", 18);

        conPane.add(con);
        conPane.add(conState);

        //file name label
        JPanel fN = new JPanel();
        JLabel fileName = new JLabel("choose a file to send...");
        fileName.setBorder(new EmptyBorder(5, 0, 0, 0));
        fileName.setFont(new Font("Arial", Font.BOLD, 20));
        fileName.setAlignmentX(Component.CENTER_ALIGNMENT);
        fN.setBackground(Color.RED);
        fN.add(fileName);

        //button part
        JPanel jButtons = new JPanel();
        jButtons.setBorder(new EmptyBorder(50, 0, 10, 0));
        jButtons.setAlignmentX(Component.CENTER_ALIGNMENT);
        jButtons.setBackground(Color.RED);

        sendFile = new JButton("upload");
        sendFile.setFont(new Font("Arial", Font.BOLD, 15));
        sendFile.setEnabled(false);

        chooseFile = new JButton("select");
        chooseFile.setFont(new Font("Arial", Font.BOLD, 15));
        chooseFile.setEnabled(false);

        seeFile = new JButton("seefile");
        seeFile.setFont(new Font("Arial", Font.BOLD, 15));
        seeFile.setEnabled(false);

        jButtons.add(sendFile);
        jButtons.add(chooseFile);
        jButtons.add(seeFile);

        //adding everything on our main window
        window.add(intro);
        window.add(option);
        window.add(conPane);
        window.add(fN);
        window.add(jButtons);
        window.setVisible(true);

        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                hostNo = hostOption.getText().toString().trim();
                String prtn = portOption.getText().toString().trim();
                portNo = Integer.parseInt(prtn);
                if (hostNo.isEmpty() || Integer.toString(portNo).isEmpty()) {
                    intro.setText("please select host and port address");
                } else {
                    try {
                        socket = new Socket(hostNo, portNo);

                    } catch (IOException ex) {
                        conState.setText("error occured");
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (socket.isConnected()) {
                        conState.setText("connected on port " + portNo);
                        sendFile.setEnabled(true);
                        chooseFile.setEnabled(true);
                        seeFile.setEnabled(true);

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
                            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                            String fileName = fileToSend[0].getName();

                            byte[] fileNameBytes = fileName.getBytes();

                            byte[] fileBytes = new byte[(int) fileToSend[0].length()];

                            fileInputStream.read(fileBytes);

                            byte[] operation = "upload".getBytes();
                            dataOutputStream.writeInt(operation.length);
                            dataOutputStream.write(operation);

                            dataOutputStream.writeInt(fileNameBytes.length);

                            dataOutputStream.write(fileNameBytes);

                            dataOutputStream.writeInt(fileBytes.length);

                            dataOutputStream.write(fileBytes);
                            allFiles.add(new MyFile(allFiles.size(), fileName, fileBytes, getFileExtension(fileName)));
                            fileInputStream.close();

                        }
                    } catch (IOException error) {

                        fileName.setText("error occured when sending the file " + fileToSend[0].getName());
                        error.printStackTrace();
                    }
                }

            }
        });

        seeFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                try {

                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    byte[] operation = "download".getBytes();
                    dataOutputStream.writeInt(operation.length);
                    dataOutputStream.write(operation);

                    InputStream is = socket.getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(is);
                    allFiles = (ArrayList<MyFile>) ois.readObject();
                    System.out.println(allFiles.size());
                    //dataOutputStream.close();

                } catch (ClassNotFoundException err) {
                    err.printStackTrace();
                    conState.setText("error occured");
                } catch (IOException ex) {

                    ex.printStackTrace();
                }

                jFramel = new JFrame("Lists");

                jFramel.setSize(400, 400);

                jFramel.setLayout(new BoxLayout(jFramel.getContentPane(), BoxLayout.Y_AXIS));

                //jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jPanell = new JPanel();

                jPanell.setLayout(new BoxLayout(jPanell, BoxLayout.Y_AXIS));

                jScrollPane = new JScrollPane(jPanell);

                jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                jlTitle = new JLabel("File Lists");

                jlTitle.setFont(new Font("Arial", Font.BOLD, 25));

                jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));

                jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

                jFramel.add(jlTitle);
                jFramel.add(jScrollPane);

                jFramel.setVisible(true);
                fileLists();
            }

        });

    }

    public static MouseListener getMyMouseListener(int id) {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                JPanel jPanel = (JPanel) e.getSource();

                int fileId = Integer.parseInt(jPanel.getName());

                for (MyFile myFile : allFiles) {
                    if (myFile.getId() == fileId) {
                        JFrame jfPreview = createFrame(myFile.getName(), myFile.getData(), myFile.getFileExtension(), id);
                        jfPreview.setVisible(true);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }

    public static JFrame createFrame(String fileName, byte[] fileData, String fileExtension, int id) {

        JFrame jFrame = new JFrame("File Downloader");

        jFrame.setSize(400, 400);

        JPanel jPanel = new JPanel();

        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        JLabel jlTitle = new JLabel("File Downloader");

        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        jlTitle.setFont(new Font("Arial", Font.BOLD, 25));

        jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));

        JLabel jlPrompt = new JLabel("download or delete" + fileName + "?");

        jlPrompt.setFont(new Font("Arial", Font.BOLD, 20));

        jlPrompt.setBorder(new EmptyBorder(20, 0, 10, 0));

        jlPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton jbYes = new JButton("download");
        jbYes.setPreferredSize(new Dimension(150, 75));

        jbYes.setFont(new Font("Arial", Font.BOLD, 20));

        JButton jbNo = new JButton("delete");

        jbNo.setPreferredSize(new Dimension(150, 75));

        jbNo.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel jlFileContent = new JLabel();
        jlFileContent.setBorder(new EmptyBorder(100, 0, 10, 0));

        jlFileContent.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jpButtons = new JPanel();

        jpButtons.setBorder(new EmptyBorder(20, 0, 10, 0));

        jpButtons.add(jbYes);
        jpButtons.add(jbNo);

        if (fileExtension.equalsIgnoreCase("txt")) {

            jlFileContent.setText("<html>" + new String(fileData) + "</html>");

        }

        jbYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                File fileToDownload = new File("../../" + fileName);
                try {

                    FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);

                    fileOutputStream.write(fileData);

                    fileOutputStream.close();

                    jFrame.dispose();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

        jbNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    byte[] operation = "delete".getBytes();
                    dataOutputStream.writeInt(operation.length);
                    dataOutputStream.write(operation);

                    dataOutputStream.writeInt(id);

                    for (int i = 0; i < allFiles.size(); i++) {
                        if (id == allFiles.get(i).id) {
                            allFiles.remove(allFiles.get(i));
                            System.out.println("after removal " + allFiles.size());
                            break;
                        }
                    }

                    jPanell.removeAll();
                    jFramel.revalidate();
                    jFramel.repaint();
                    fileLists();

                    jFrame.dispose();
                    //dataOutputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        jPanel.add(jlTitle);
        jPanel.add(jlPrompt);
        jPanel.add(jlFileContent);
        jPanel.add(jpButtons);

        jFrame.add(jPanel);

        return jFrame;

    }

    public static String getFileExtension(String fileName) {

        int i = fileName.lastIndexOf('.');

        if (i > 0) {

            return fileName.substring(i + 1);
        } else {
            return "No extension found.";
        }
    }

    public static void fileLists() {
        for (MyFile file : allFiles) {

            jpFileRow = new JPanel();
            jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.X_AXIS));

            jlFileName = new JLabel(file.name);
            jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
            jlFileName.setBorder(new EmptyBorder(10, 0, 10, 0));
            if (getFileExtension(file.name).equalsIgnoreCase("txt")) {

                jpFileRow.setName((String.valueOf(file.id)));
                jpFileRow.addMouseListener(getMyMouseListener(file.id));

                jpFileRow.add(jlFileName);
                jPanell.add(jpFileRow);
                jFramel.validate();
            } else {

                jpFileRow.setName((String.valueOf(file.id)));

                jpFileRow.addMouseListener(getMyMouseListener(file.id));

                jpFileRow.add(jlFileName);
                jPanell.add(jpFileRow);

                jFramel.validate();
            }
        }

    }

}
