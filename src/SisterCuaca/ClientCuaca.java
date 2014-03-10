/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SisterCuaca;

/**
 *
 * @author bey0nd
 */
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientCuaca {  
     // deklarasi socket
    private static Socket socket;
    private  static PesanCuaca pesan;
        
    // deklarasi I/O stream
    private static BufferedReader in;
    private static PrintWriter out;
    
    // deklarasi boolean untuk status, diletakkan dalam scope private static agar dapat diakses keseluruhan class
    private static boolean active;
    
    public static void main(String[] args) throws IOException{
        
        // inisialisasi socket dan I/O
        socket = null;
        out = null;
        in = null;

        try {
            // mengkoneksikan client dengan socket yang sudah ada di port 4444 untuk host: localhost
            socket = new Socket("localhost",4444);
            System.out.println("Silahkan masukkan keyword nama hari atau ketik 'semua' untuk seluruh perkiraan cuaca");
            System.out.println("Contoh: 'semua' atau 'senin' atau 'selasa' (tanpa tanda petik)");
            // menghubungkan I/O stream dengan socket
            out = new PrintWriter(socket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } 
        catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost.");
            System.exit(1);
        } 
        catch (IOException e) {
            System.err.println("Couldn't get I/O for " + "the connection to: localhost.");
            System.exit(1);
        }
        
        // deklarasi input stream untuk membaca input dari console
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromUser;

       // inisialisasi awal adalah true dimana setiap client terhubung berarti client aktif
        active = true;

       // class threads diinstansiasi kemudian dijalankan agar setiap thread yg terhubung dpt merima pesan dari server
       new threads().start();

       // selama thread masih aktif akan meminta input dari console
       ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        while(active){
            fromUser = stdIn.readLine();
            if (fromUser != null && active) {
                // inputan dari console nantinya akan ditampilkan dari PrintWriter stream pada thread
                oos.writeObject(new PesanCuaca(fromUser));
            }
        }

        // socket dan I/O stream ditutup
        oos.close();
        out.close();
        in.close();
        stdIn.close();
        socket.close();
    }
       
    private static class threads extends Thread {
        // tidak membutuhkan constructor, hanya berjalan menjalan fungsi run setiap diinstansiasi di fungsi utama
       @Override
       public void run(){
           ObjectInputStream ois = null;
           try {
               ois = new ObjectInputStream(new BufferedInputStream( socket.getInputStream() ));
               try {
                   while(true){
                       pesan = (PesanCuaca) ois.readObject();
                       if (pesan != null)
                           if (pesan.getString().equals("bye")) break;
                           else System.out.println(pesan.getString());
                       else break;
                   }
               }
               catch (ClassNotFoundException ex) {
                   System.out.println("ClassNotFound: " + ex.getMessage());
               }
               // ketika sudah berhenti membaca maka status thread diubah jadi false sehingga tidak dapat lagi membaca input dari user pada console
               active = false;
               System.out.println("Terputus dari server (ENTER to continue..)");
           } catch (IOException ex) {
               Logger.getLogger(ClientCuaca.class.getName()).log(Level.SEVERE, null, ex);
           } finally {
               try {
                   ois.close();
               } catch (IOException ex) {
                   Logger.getLogger(ClientCuaca.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
       }
    }
}
