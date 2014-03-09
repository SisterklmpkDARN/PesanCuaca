/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SisterCuaca;
import java.io.Serializable;

/**
 *
 * @author hades
 */
//nabilla tes
public class PesanCuaca implements Serializable{
        private String pesanCuaca;
    public PesanCuaca(String pesanCuaca) {
        this.pesanCuaca = pesanCuaca;
        
    }
    public void setPesanCuaca(String pesanCuaca) {
      this.pesanCuaca= pesanCuaca;
   }

    public String getString() {
        return pesanCuaca;
    }
    

}
