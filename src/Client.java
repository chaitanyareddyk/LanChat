
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


public class Client extends Applet implements ActionListener, Runnable
{

      

                Socket s;
                BufferedReader br;
                BufferedWriter bw;
                TextArea text;
        Button sendBut,exitBut,sendFile,recFile;
                List list;
    
public void init(){
setForeground(Color.blue);


}
    
        public void start()
                {
              
                setSize(450,200);
                                
                                
              
                setBackground(new Color(44, 62, 80));
                this.setLayout(new GridLayout(2,1));

                Panel panels[]=new Panel[2];
                panels[0]=new Panel();
                panels[1]=new Panel();
                panels[0].setLayout(new BorderLayout());
                panels[1].setLayout(new FlowLayout(FlowLayout.LEFT));

                sendBut=new Button("Send");
                exitBut=new Button("Close");
                sendFile=new Button("Send File");
                recFile=new Button("Receive File");

                sendBut.addActionListener(this);
                exitBut.addActionListener(this);
                sendFile.addActionListener(this);
                recFile.addActionListener(this);

                list=new List();

                text=new TextArea("Enter your message here",3,30);

                panels[0].add(list);
                panels[1].add(text);
                panels[1].add(sendBut);
                panels[1].add(exitBut);   
                panels[1].add(sendFile);
                panels[1].add(recFile);
                


                add(panels[0]);
                add(panels[1]);
               
                                setVisible(true);
               

                try
                {
                        

                        s=new Socket("127.0.0.1",1698);
                        br=new BufferedReader(new InputStreamReader(s.getInputStream()));
                        bw=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                                                Thread th;
                                                th=new Thread(this);
                                                th.start();
                                               
                                }catch(Exception e){System.out.println("Error:"+e);}
                               
                }

     
        public void run()
                {
                while(true){
                                
                     try{
                        
                                list.add("Reply: "+br.readLine());
                                list.select(list.getItemCount()-1);
                                showStatus("Connected...!");
                                                }catch(Exception h){System.out.println("Error:"+h);}
                                }
                }
               

        public void actionPerformed(ActionEvent ae)
                {
                 if(ae.getSource().equals(exitBut))
                                                 System.exit(0);
                 else if(ae.getSource().equals(sendFile))
                 {
                     CSendFiles c=new CSendFiles();
                     c.sf();
                     
                 }
                 else if(ae.getSource().equals(recFile))
                 {
                     CReceiveFiles r=new CReceiveFiles();
                     r.rf();
                 }
                 else
                 {
                        try
                        {
                                bw.write(text.getText());
                                showStatus("Connected...!");
                                list.add("Sent: "+text.getText());
                                showStatus("Messsage Delivered...");
                                list.select(list.getItemCount()-1);
                                bw.newLine();
                                bw.flush();
                                text.setText("");
                        }catch(Exception m){System.out.println("Error:"+m);}
                                 }
                                                                 
                }
               
}





class CSendFiles{
    
    

public void sf(){
        
        int serverPort=1699;
        
        Scanner scan = new Scanner(System.in);  
        System.out.println("Enter the path: ");
        String fileToSend = scan.nextLine(); 
        
        System.out.println("Share the following information with your friend: ");
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

            if(outToClient!=null) {
                File myFile=new File(fileToSend );
                byte[] mybytearray=new byte[(int) myFile.length()];

                FileInputStream fis=null;

                try{
                    fis=new FileInputStream(myFile);
                } catch(FileNotFoundException ex){
                   System.out.println("The specified file is not found : "+ex);
                }
                BufferedInputStream bis=new BufferedInputStream(fis);

                try{
                    bis.read(mybytearray, 0, mybytearray.length);
                    outToClient.write(mybytearray, 0, mybytearray.length);
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





class CReceiveFiles{
    

    public void rf(){
        
       
      String serverIP="127.0.0.1";  
      
       
        int serverPort=1699;
        
        
        Scanner scan1=new Scanner(System.in);  
        System.out.println("Enter the path: ");
        String fileOutput=scan1.nextLine(); 
       
        byte[] aByte=new byte[1];
        int bytesRead;

        Socket clientSocket=null;
        InputStream is=null;

        try{
            clientSocket=new Socket( serverIP , serverPort );
            is=clientSocket.getInputStream();
        }catch(IOException ex){
            System.out.println("Please rectify this Error: "+ex);
        }

        ByteArrayOutputStream baos=new ByteArrayOutputStream();

        if(is!=null){

            FileOutputStream fos=null;
            BufferedOutputStream bos=null;
            try{
                fos=new FileOutputStream( fileOutput );
                bos=new BufferedOutputStream(fos);
                bytesRead=is.read(aByte, 0, aByte.length);

                do{
                        baos.write(aByte);
                        bytesRead = is.read(aByte);
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

