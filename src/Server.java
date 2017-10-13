
/**
 *
 * @author chaitanyareddy.me
 */

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.Scanner;



public class Server extends Applet implements ActionListener, Runnable
{
        
        ServerSocket ss;
        Socket s;
                BufferedReader br;
                BufferedWriter bw;
                TextArea msg;
        Button sb,eb,sfb,rfb;
        List msgs;
    
public void init(){
    setForeground(Color.blue);
  
   
}
    
        public void start(){
                
               
               
                setSize(450, 200);
               
               
                setBackground(new Color(52, 73, 94));
                this.setLayout(new GridLayout(2,1));

                Panel pnl[]=new Panel[2];
                pnl[0]=new Panel();
                pnl[1]=new Panel();
                pnl[0].setLayout(new BorderLayout());
                pnl[1].setLayout(new FlowLayout(FlowLayout.LEFT));

                sb=new Button("Send");
                eb=new Button("Close");
                sfb=new Button("Send File");
                rfb=new Button("Receive File");

                sb.addActionListener(this);
                eb.addActionListener(this);
                sfb.addActionListener(this);
                rfb.addActionListener(this);
                

                msgs=new List();
                msgs.select(msgs.getItemCount()-1);
               
                msg=new TextArea("Enter your message here...",3,30);

                pnl[0].add(msgs);
                pnl[1].add(msg);
                pnl[1].add(sb);
                pnl[1].add(eb);
                pnl[1].add(sfb);
                pnl[1].add(rfb);

                add(pnl[0]);
                add(pnl[1]);
                showStatus("......");
                setVisible(true);

                try
                {       showStatus("Ready for connection...");
                        msgs.add("Waiting for client to conenct and reply...");
                        ss=new ServerSocket(1698);
                        s=ss.accept();
                        br=new BufferedReader(new InputStreamReader(s.getInputStream()));
                        bw=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                        bw.write("We are now Connected...!");                       
                        bw.newLine();
                        bw.flush();
                        Thread tr;
                        tr=new Thread(this);
                        tr.start();
                                               
                                               
                                }catch(Exception ex){System.out.println("Error :"+ex);}

                }

        public void run()
                {
                while(true){
                                
                        try                      
                        {       showStatus("Connected...!");
                                msgs.add("Reply: "+br.readLine());
                                msgs.select(msgs.getItemCount()-1);
                              
                        }catch (Exception ex){System.out.println("Error :"+ex);}
                                }
                }

       
               
        public void actionPerformed(ActionEvent ae)
                {
                 if(ae.getSource().equals(eb))
                                                 System.exit(0);
                 else if(ae.getSource().equals(sfb))
                 {
                     SendFiles c=new SendFiles();
                     c.sf();
                     
                 }
                 else if(ae.getSource().equals(rfb))
                 {
                     ReceiveFiles r=new ReceiveFiles();
                     r.rf();
                 }
                 else
                 {
                        try
                        {                               
                                bw.write(msg.getText());
                                msgs.add("Sent: "+msg.getText());
                                showStatus("Message Delivered...");
                                msgs.select(msgs.getItemCount()-1);
                                bw.newLine();bw.flush();                               
                                msg.setText("");
                        }catch(Exception x){System.out.println("Error :"+x);}
                                 }
                                                                 
                }
               
}




class SendFiles {
    
    

public void sf() {
        
        int serverPort=1699;
        
        Scanner scan=new Scanner(System.in);  
        System.out.println("Enter the path: ");
        String fileToSend=scan.nextLine(); 
        
        System.out.println("Share the following information with the receiver: ");
        System.out.println("---------------------------------------------");
        System.out.println("File's Orginal Path: "+fileToSend);
       
        System.out.println("---------------------------------------------");
        
        
        while(true){
            ServerSocket welcomeSocket=null;
            Socket connectionSocket=null;
            BufferedOutputStream outToClient=null;

            try{
                welcomeSocket=new ServerSocket(serverPort);
                connectionSocket=welcomeSocket.accept();
                outToClient=new BufferedOutputStream(connectionSocket.getOutputStream());
            }catch(IOException ex){
                System.out.println("Please rectify this Error: "+ex);
            }

            if(outToClient!=null){
                File myFile=new File( fileToSend );
                byte[] mybytearray=new byte[(int) myFile.length()];

                FileInputStream fis=null;

                try{
                    fis=new FileInputStream(myFile);
                }catch(FileNotFoundException ex){
                    System.out.println("The specified file not found : "+ex);
                }
                BufferedInputStream bis=new BufferedInputStream(fis);

                try{
                    bis.read(mybytearray,0,mybytearray.length);
                    outToClient.write(mybytearray,0,mybytearray.length);
                    outToClient.flush();
                    outToClient.close();
                    connectionSocket.close();

                    
                    return;
                }catch(IOException ex){
                    System.out.println("Please rectify this Error: "+ex);
                }
            }
        }
    }
}





class ReceiveFiles{
    

    public void rf(){
        
       
      String serverIP="127.0.0.1";  
      
        
      int serverPort=1699;
        
        
        Scanner scan1 = new Scanner(System.in);  
        System.out.println("Enter the path: ");
        String fileOutput = scan1.nextLine(); 
       
        byte[] aByte = new byte[1];
        int bytesRead;

        Socket clientSocket = null;
        InputStream is = null;

        try{
            clientSocket = new Socket( serverIP , serverPort );
            is = clientSocket.getInputStream();
        }catch(IOException ex){
            System.out.println("Please rectify this Error: "+ex);
        }

        ByteArrayOutputStream baos=new ByteArrayOutputStream();

        if(is!=null){

            FileOutputStream fos=null;
            BufferedOutputStream bos=null;
            try{
                fos=new FileOutputStream(fileOutput );
                bos=new BufferedOutputStream(fos);
                bytesRead=is.read(aByte,0,aByte.length);

                do{
                        baos.write(aByte);
                        bytesRead=is.read(aByte);
                }while(bytesRead!=-1);

                bos.write(baos.toByteArray());
                bos.flush();
                bos.close();
                clientSocket.close();
            }catch(IOException ex){
                System.out.println("Please rectify this Error: "+ex);
            }
        }
    }
}

