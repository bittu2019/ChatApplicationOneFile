/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientFolder;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javazoom.jl.player.Player;

/**
 *
 * @author HCL
 */
public class Client implements ActionListener{

    JFrame jf;
    JTextArea jta;
    JTextField jtf;
    JScrollPane jsp;
    ServerSocket ss;
    InetAddress inet_Address;
    String ipaddress;
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    Thread thread=new Thread()
    {
        int count=0;
        public void run()
        {
           while(true)
           {
                readMessage();
                count++;
                System.out.println(count);
           }
           
        }
    };

    public Client(JFrame jf, JTextArea jta, JTextField jtf, JScrollPane jsp, ServerSocket ss, InetAddress inet_Address, String ipaddress) {
        this.jf = jf;
        this.jta = jta;
        this.jtf = jtf;
        this.jsp = jsp;
        this.ss = ss;
        this.inet_Address = inet_Address;
        this.ipaddress = ipaddress;
    }
    public Client() {
        ipaddress=JOptionPane.showInputDialog("Enter the Ip address");
        if(ipaddress!=null)
        {
            if(!ipaddress.equals(""))
            {
               
                connectToServer();
                jf=new JFrame("Client");
                jf.setSize(600, 500);
                jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Font font=new Font("Arial", 1, 20);
                jta=new JTextArea();
                jta.setFont(font);
                jta.setEditable(false);
                jsp=new JScrollPane(jta);
                jf.add(jsp);
                jtf=new JTextField();
                jtf.addActionListener(this);
                jf.add(jtf, BorderLayout.SOUTH);
                jf.setVisible(true);
            }
        }
            
    }
    public void connectToServer()
    {
        try {
            socket=new Socket(ipaddress, 5050);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
           
            sendMessage(jtf.getText());
            jta.append(jtf.getText()+"\n");
            jtf.setText("");
            
    }
    public void setInputOutput()
    {
        try {
             dis=new DataInputStream(socket.getInputStream());
             dos=new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println(e);
        }
        
        thread.start();
    }
    public void readMessage()
    {
        try {
            String mymessage=dis.readUTF();
            showMessage(mymessage);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void showMessage(String myMessage) {
       jta.append(myMessage+"\n");
       soundplay();
      
    }

    public void sendMessage(String text) {
          try {
              dos.writeUTF(text);
              dos.flush();
//              dos.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    private void soundplay() {
        try {
            File file_name=new File("src\\Sound\\tune.mp3");
            FileInputStream fis=new FileInputStream(file_name.getAbsolutePath());
            Player p=new Player(fis);
            p.play();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}