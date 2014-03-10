/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SisterCuaca;
import java.io.*;
import java.net.*;

/**
 *
 * @author hades
 */

public class ClientCuacaSerial {
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
            System.out.println("Silahkan masukkan keyword nama hari atau ketik semua untuk seluruh perkiraan cuaca\n");
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
        while(active){
            fromUser = stdIn.readLine();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            if (fromUser != null) {
                // inputan dari console nantinya akan ditampilkan dari PrintWriter stream pada thread
                oos.writeObject(new PesanCuaca(fromUser));
            }
        }

        // socket dan I/O stream ditutup
        out.close();
        in.close();
        stdIn.close();
        socket.close();
    }
       
    private static class threads extends Thread {
        // tidak membutuhkan constructor, hanya berjalan menjalan fungsi run setiap diinstansiasi di fungsi utama
       public void run(){
            try{
                ObjectInputStream ois =new ObjectInputStream(new BufferedInputStream( socket.getInputStream() ));// membaca input dari server
                try {
                    while(true){
                        pesan = (PesanCuaca) ois.readObject();
                        System.out.println(pesan.getString());
                    }
                } 
                catch (ClassNotFoundException ex) {
                    System.out.println("ClassNotFound: " + ex.getMessage());
                }
                // ketika sudah berhenti membaca maka status thread diubah jadi false sehingga tidak dapat lagi membaca input dari user pada console
                active = false;
            } 
            catch(IOException e){
                 } 
             }
    }
}
