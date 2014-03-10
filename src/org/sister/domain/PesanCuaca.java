package org.sister.domain;

import java.io.Serializable;

public class PesanCuaca implements Serializable{
        private String pesanCuaca;
        private String hari;
        private String tgl;
        private String cuaca;
        
    public PesanCuaca(String pesanCuaca) {
        this.pesanCuaca = pesanCuaca;   
    }
    
    public PesanCuaca(String hari, String tgl, String cuaca) {
        this.hari=hari;
        this.tgl=tgl;
        this.cuaca=cuaca;
    }
    
    public void setPesanCuaca(String pesanCuaca) {
      this.pesanCuaca= pesanCuaca;
   }

    public String getString() {
        return pesanCuaca;
    }

    /**
     * @return the hari
     */
    public String getHari() {
        return hari;
    }

    /**
     * @param hari the hari to set
     */
    public void setHari(String hari) {
        this.hari = hari;
    }

    /**
     * @return the tgl
     */
    public String getTgl() {
        return tgl;
    }

    /**
     * @param tgl the tgl to set
     */
    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    /**
     * @return the cuaca
     */
    public String getCuaca() {
        return cuaca;
    }

    /**
     * @param cuaca the cuaca to set
     */
    public void setCuaca(String cuaca) {
        this.cuaca = cuaca;
    }
}
