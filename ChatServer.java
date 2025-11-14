package utilx.ServerGUI;

import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class ChatServer extends JFrame implements ActionListener
{
    public JFrame fobj ;
    public JButton sendbtn;
    public JTextField text;
    public JLabel youLabel;
    public JTextArea chatArea;

    public ServerSocket ssobj;
    public Socket sobj ;
    public DataInputStream dis;
    public DataOutputStream dos;

    public ChatServer()
    {
        fobj = new JFrame("Marvellous Server");
        fobj.setLayout(null);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane sp = new JScrollPane(chatArea);
        sp.setBounds(20, 20, 330, 100);
        fobj.add(sp);

        youLabel = new JLabel("You :");
        youLabel.setBounds(20, 140, 50, 30);
        fobj.add(youLabel);

        text = new JTextField();
        text.setBounds(70, 140, 250, 30);
        fobj.add(text);

        sendbtn = new JButton("Send");
        sendbtn.setBounds(150, 200, 100, 35);
        sendbtn.addActionListener(this);
        fobj.add(sendbtn);

        fobj.setSize(400, 300);
        fobj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fobj.setVisible(true);

        // ✔ Lambda Thread
        Thread th = new Thread(() -> runServerTask());
        th.start();
    }

    // ✔ Send button action
    public void actionPerformed(ActionEvent aobj)
    {
        if(aobj.getSource() == sendbtn)
        {
            try
            {
                String msg = text.getText();
                chatArea.append("You : " + msg + "\n");

                dos.writeUTF(msg);
                text.setText("");
            }
            catch(Exception eobj)
            {
                chatArea.append("Error : " + eobj.getMessage() + "\n");
            }
        }
    }
    
    //  Server listening task in single method
    private void runServerTask()
    {
        try
        {
            ssobj = new ServerSocket(8080);
            chatArea.append("Server started. Waiting for client...\n");

            sobj = ssobj.accept();
            chatArea.append("Client connected.\n");

            dis = new DataInputStream(sobj.getInputStream());
            dos = new DataOutputStream(sobj.getOutputStream());

            String str = "";

            while(true)
            {
                str = dis.readUTF();

                if(str.equals("bye"))
                {
                    chatArea.append("Client : " + str + "\n");
                    chatArea.append("Client disconnected.\n");
                    sobj.close();
                    dis.close();
                    dos.close();
                    ssobj.close();
                    break;
                }

                chatArea.append("Client : " + str + "\n");
            }
        }
        catch(Exception eobj)
        {
            
            chatArea.append("Error : " + eobj.getMessage() + "\n");
        }
    }

}
