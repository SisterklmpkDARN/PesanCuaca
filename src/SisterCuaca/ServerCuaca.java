package SisterCuaca;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerCuaca {
     public static void main(String[] args) throws IOException {

        int port = 4444;
        boolean listening = true;
        System.out.println("Server is running.");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (listening) {
                new ServerCuacaThread(serverSocket.accept()).start();
            }
        } 
        catch (IOException e) {
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
      ArrayList<String> tanggal = new ArrayList<>();
      
      public ServerCuacaThread(Socket socket) {
          super("ServerCuacaThread");
          this.socket = socket;
      }
    
      public void run() {
          try {
              System.out.println("Client "+ socket.getInetAddress().getHostName() + " connected.");
              in  = new ObjectInputStream(socket.getInputStream());
              out = new ObjectOutputStream(socket.getOutputStream());
          } 
          catch (IOException ex) {
              Logger.getLogger(ServerCuacaThread.class.getName()).log(Level.SEVERE, null, ex);
          }
          
          // Mengisi arraylist hari dan ramalannya
          try {
              FileInputStream fstream = new FileInputStream("C:\\Users\\Prameswari\\Documents\\PesanCuaca\\src\\SisterCuaca\\WeatherForecast.txt");
            
              try (DataInputStream in = new DataInputStream(fstream)) {
                  BufferedReader br = new BufferedReader(new InputStreamReader(in));
                  String strLine = null;
                  String[] splitHari, splitCuaca, splitTanggal;
                  
                  while ((strLine = br.readLine()) != null)  {
                      //System.out.println("strLine:" + strLine);
                      splitHari = strLine.split(",");
                      splitTanggal = strLine.split(" - ");
                      splitCuaca = splitHari[1].split(" - ");
                      ramalan.add(splitHari[0]);
                      tanggal.add(splitTanggal[0]);
                      cuaca.add(splitCuaca[1]);
                  }
              }
          }
          catch (Exception e){
              System.err.println("Error: " + e.getMessage());
          }
          
          try {
              while(true) {
                  pesan = (PesanCuaca) in.readObject();
                  if (pesan.getString() == null) break;
                  else System.out.println("From "+ socket.getInetAddress().getHostName() +": " + pesan.getString());
                  if (pesan.getString().equals("exit")) {
                      break;
                  }
                  else if(pesan.getString().equalsIgnoreCase("semua")) {
                        for (int i=0; i<cuaca.size(); i++) {
                            out.writeObject(new PesanCuaca("Cuaca hari : " + tanggal.get(i)));
                            out.writeObject(new PesanCuaca("             " + cuaca.get(i)));
                            pesan.setPesanCuaca("");
                        }     
                  }
                  else {
                      int index=-1;
                      for(int i=0;i<ramalan.size();i++){
                        if(pesan.getString().equalsIgnoreCase(ramalan.get(i))){
                            index=i;
                        }
                      }

                      if(index == -1){
                          System.out.println("Perintah tidak dikenali");
                          out.writeObject(new PesanCuaca("Perintah tidak dikenali"));
                      }
                      else{
                          System.out.println("Cuaca hari "+ ramalan.get(index) +" : "+ cuaca.get(index));
                          try {
                              out.writeObject(new PesanCuaca("Cuaca hari "+ ramalan.get(index) +" : "+ cuaca.get(index)));
                          } 
                          catch (IOException ex) {
                              Logger.getLogger(ServerCuacaThread.class.getName()).log(Level.SEVERE, null, ex);
                          }
                      }
                  }
              }
              
              System.out.println("Client "+ socket.getInetAddress().getHostName() +" disconnected.");
              out.writeObject(new PesanCuaca("bye"));
              out.close();
              in.close();
              socket.close();
          } 
          catch (IOException ex) {
              Logger.getLogger(ServerCuacaThread.class.getName()).log(Level.SEVERE, null, ex);
          } catch (ClassNotFoundException ex) {
              Logger.getLogger(ServerCuacaThread.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
}