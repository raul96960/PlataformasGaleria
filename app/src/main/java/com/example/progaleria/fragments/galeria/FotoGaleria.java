package com.example.progaleria.fragments.galeria;

public class FotoGaleria {
    private String nombreFOTO;
    private String urlIMG;
    private String latitud;
    private String longitud;

    public FotoGaleria() {
    }

    public FotoGaleria(String nombreFOTO, String urlIMG, String latitud, String longitud) {
        this.nombreFOTO = nombreFOTO;
        this.urlIMG = urlIMG;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getNombreFOTO() {
        return nombreFOTO;
    }

    public void setNombreFOTO(String nombreFOTO) {
        this.nombreFOTO = nombreFOTO;
    }

    public String getUrlIMG() {
        return urlIMG;
    }

    public void setUrlIMG(String urlIMG) {
        this.urlIMG = urlIMG;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
