package gr.spinellis.ckjm;

public class Metrics {
    int wmc;
    int noc;
    int rfc;
    int cbo;
    int dit;
    int lcom;
    int npm;

    public Metrics() {
        wmc = 0;
        noc = 0;
        rfc = 0;
        cbo = 0;
        dit = 0;
        lcom = 0;
        npm = 0;
    }

    public int getWmc() {
        return wmc;
    }

    public int getNoc() {
        return noc;
    }

    public int getRfc() {
        return rfc;
    }

    public int getDit() {
        return dit;
    }

    public int getCbo() {
        return cbo;
    }

    public int getLcom() {
        return lcom;
    }

    public int getNpm() {
        return npm;
    }//setters

    public void setLcom(int l) {
        lcom = l;
    }

    public void setCbo(int c) {
        cbo = c;
    }

    public void setDit(int d) {
        dit = d;
    }

    public void setRfc(int r) {
        rfc = r;
    }

    public void incNoc() {
        noc++;
    }

    public void incWmc() {
        wmc++;
    }

    public void incNpm() {
        npm++;
    }
}