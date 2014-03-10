package org.sister.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sister.domain.PesanCuaca;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
              JSONParser parser = new JSONParser();
              ArrayList<PesanCuaca> arrayPesan = new ArrayList<>();
              
              Object obj = parser.parse(new FileReader("E:\\Praktikum Semester 6\\PesanCuaca\\weather.json"));
 
                JSONArray ja = (JSONArray) obj;
                for (int i = 0; i < ja.size(); ++i) {
                    JSONObject jo = (JSONObject) ja.get(i);
                    String hari = (String) jo.get("hari");
                    String tgl = (String) jo.get("tgl");
                    String cuaca = (String) jo.get("cuaca");
                    PesanCuaca pesan = new PesanCuaca(hari, tgl, cuaca);
                    arrayPesan.add(pesan);
                    //System.out.println(arrayPesan.get(i).getHari());
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
                        for (int i=0; i<arrayPesan.size(); i++) {
                            out.writeObject(new PesanCuaca("Cuaca hari : " + arrayPesan.get(i).getHari()+", " +arrayPesan.get(i).getTgl()));
                            out.writeObject(new PesanCuaca("             " + arrayPesan.get(i).getCuaca()));
                            pesan.setPesanCuaca("");
                        }     
                  }
                  else {
                      int index=-1;
                      for(int i=0;i<arrayPesan.size();i++){
                        if(pesan.getString().equalsIgnoreCase(arrayPesan.get(i).getHari())){
                            index=i;
                        }
                      }

                      if(index == -1){
                          System.out.println("Perintah tidak dikenali");
                          out.writeObject(new PesanCuaca("Perintah tidak dikenali"));
                      }
                      else{
                          System.out.println("Cuaca hari "+ arrayPesan.get(index).getHari() +" : "+ arrayPesan.get(index).getCuaca());
                          try {
                              out.writeObject(new PesanCuaca("Cuaca hari "+ arrayPesan.get(index).getHari() +" : "+ arrayPesan.get(index).getCuaca()));
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
      }   catch (IOException ex) {
              Logger.getLogger(ServerCuacaThread.class.getName()).log(Level.SEVERE, null, ex);
          } catch (ParseException ex) {
              Logger.getLogger(ServerCuacaThread.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
}