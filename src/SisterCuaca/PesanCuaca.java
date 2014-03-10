package ramalancuaca;

import java.io.Serializable;

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
