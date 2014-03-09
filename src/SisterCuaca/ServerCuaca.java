/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SisterCuaca;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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

//----------------------------------------------------------------------------------------------------------
class ServerCuacaThread extends Thread {
      private Socket socket = null;
      private ObjectOutputStream out;
      private ObjectInputStream in;
      private BufferedReader fis = null;
      private PesanCuaca pesan =null;
      ArrayList<String> ramalan = new ArrayList<>();
      ArrayList<String> cuaca = new ArrayList<>();
      
      public ServerCuacaThread(Socket socket) {
          super("ServerCuacaThread");
          this.socket = socket;
      }
    
    public void run() {      
        
              try {
                  System.out.println("Client connected..");
                  in  = new ObjectInputStream(socket.getInputStream());
                  pesan = (PesanCuaca) in.readObject();
                  //print perintah cuaca dari client
                  System.out.println("From client: " + pesan.getString() ); 
                  out = new ObjectOutputStream(socket.getOutputStream());
              } catch (IOException ex) {
                  Logger.getLogger(ServerCuacaThread.class.getName()).log(Level.SEVERE, null, ex);
              } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServerCuacaThread.class.getName()).log(Level.SEVERE, null, ex);
            }

           // if(pesan.getString().equals("semua")){
              try{
            FileInputStream fstream = new FileInputStream("E:\\Praktikum Semester 6\\Sister\\PesanCuaca\\src\\SisterCuaca\\WeatherForecast.txt");
            
            try (DataInputStream in = new DataInputStream(fstream)) {
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine = null;
                String[] splitHari;
                String[] splitCuaca;
                
                
                while ((strLine = br.readLine()) != null)  { 
                    splitHari = strLine.split(",");
                    splitCuaca = splitHari[1].split(" - ");
                    ramalan.add(splitHari[0]);
                    cuaca.add(splitCuaca[1]);
                }
  //              for(int i=0;i<ramalan.size();i++){
  //                  System.out.println(ramalan.get(i)); 
  //                  System.out.println(cuaca.get(i));
  //              }
            }
          }catch (Exception e){
              System.err.println("Error: " + e.getMessage());
          }
          try {
              int a = 0;
              while(true) {
                  if(pesan.getString().equals("exit")) break;
                  
                  while(true) {
                      if(a==0) {
                        a++;
                        int index=-1;
                        for(int i=0;i<ramalan.size();i++){
                            if(pesan.getString().equals(ramalan.get(i))){
                                index=i;
                            }
                        }

                        if(index == -1){
                            System.out.println("Hari yang anda pilih tidak ada");
                            pesan.setPesanCuaca("");
                            //break;
                        }
                            

                        else{
                          System.out.println("Cuaca hari ini : "+ cuaca.get(index));
                          try {
                                out.writeObject(new PesanCuaca("Cuaca hari ini : "+cuaca.get(index)));
                                //break;
                                pesan.setPesanCuaca("");
                                break;
                              } catch (IOException ex) {
                                Logger.getLogger(ServerCuacaThread.class.getName()).log(Level.SEVERE, null, ex);
                              }
                          }
                        a=0;
                        
                      }
                   }
              }
              
              out.writeObject(new PesanCuaca("you're exit"));
              System.out.println("Client disconnect..");
              out.close();
              in.close();
              socket.close();
                  } catch (IOException ex) {
                      Logger.getLogger(ServerCuacaThread.class.getName()).log(Level.SEVERE, null, ex);
                  }
        }   
    }


