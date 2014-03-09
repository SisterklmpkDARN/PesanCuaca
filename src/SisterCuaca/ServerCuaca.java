/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SisterCuaca;
import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author hades
 */
public class ServerCuaca {
     public static void main(String[] args) throws IOException {

    

        int port = 4444;
        boolean listening = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) { 
            while (listening) {
	            new ServerCuacaThread(serverSocket.accept()).start();
                    
	        }
	    } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            System.exit(-1);
        }
     }
}
  class ServerCuacaThread extends Thread {
    private Socket socket = null;
     private  ObjectOutputStream out;
     private  ObjectInputStream in;
     private BufferedReader fis = null;
     private  PesanCuaca pesan =null;
    public ServerCuacaThread(Socket socket) {
             super("ServerCuacaThread");
        this.socket = socket;
           
       
    }
    
    public void run() {
        try{
                    System.out.println("onok client");
       in  = new ObjectInputStream(socket.getInputStream());
      try {
                pesan = (PesanCuaca) in.readObject();
                System.out.println("From client: " + pesan.getString() );    
               
            } catch (ClassNotFoundException ex) {
                System.out.println("ClassNotFound: " + ex.getMessage());
            } 
        out = new ObjectOutputStream(socket.getOutputStream()); 
      
     // if(pesan.getString().equals("semua")){
            try {
            fis = new BufferedReader(new FileReader("C:\\Users\\hades\\Documents\\NetBeansProjects\\Sisster\\src\\sisster\\WeatherForecast.txt"));
            System.out.println("Server is running");
        } catch (FileNotFoundException e) {
            System.err.println("Could not open quote file. Serving time instead.");
        }
            ArrayList<String> ramalan = new ArrayList<String>();
            for(int i=0;i<5;i++){
                ramalan.add(fis.readLine());
            }
             if(pesan.getString().equals("senin")){
                out.writeObject(new PesanCuaca(ramalan.get(0)));
                System.out.println(ramalan.get(0));}
              if(pesan.getString().equals("selasa")){
                out.writeObject(new PesanCuaca(ramalan.get(1)));
                System.out.println(ramalan.get(1));}
                  if(pesan.getString().equals("rabu")){
                out.writeObject(new PesanCuaca(ramalan.get(2)));
                System.out.println(ramalan.get(2));}
                  if(pesan.getString().equals("kamis")){
                out.writeObject(new PesanCuaca(ramalan.get(3)));
                System.out.println(ramalan.get(3));}
                   if(pesan.getString().equals("jumat")){
                out.writeObject(new PesanCuaca(ramalan.get(4)));
                System.out.println(ramalan.get(4));}
                    if(pesan.getString().equals("semua")){
                        for (String s : ramalan)
                        {
                             int i = ramalan.indexOf(s);
                             out.writeObject(new PesanCuaca(s));
                             System.out.println(s);}
                        }
               
                        out.flush();
                        
                /*
                for(int i=0;i<5;i++)
                {ramalan = fis.readLine();
                if(pesan.getString().equals("senin")&&i==0){
                out.writeObject(new PesanCuaca(ramalan));
                System.out.println(ramalan);}
                 if(pesan.getString().equals("selasa")&&i==1){
                out.writeObject(new PesanCuaca(ramalan));
                System.out.println(ramalan);}
                  if(pesan.getString().equals("rabu")&&i==2){
                out.writeObject(new PesanCuaca(ramalan));
                System.out.println(ramalan);}
                  if(pesan.getString().equals("kamis")&&i==3){
                out.writeObject(new PesanCuaca(ramalan));
                System.out.println(ramalan);}
                   if(pesan.getString().equals("jumat")&&i==4){
                out.writeObject(new PesanCuaca(ramalan));
                System.out.println(ramalan);}
                out.flush();
                }*/
              //  }
          
            
        
        out.close();
        in.close();
        socket.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}


