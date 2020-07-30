package com.example.progaleria.models;

public class FotoGaleria {

    private String id;
    private String nombreFOTO;
    private String url;
    private String latitud;
    private String longitud;

    public FotoGaleria() {
    }
    public FotoGaleria(String nombreFOTO, String urlIMG, String latitud, String longitud) {
        this.nombreFOTO = nombreFOTO;
        this.url = urlIMG;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getNombreFOTO() {
        return nombreFOTO;
    }

    public void setNombreFOTO(String nombreFOTO) {
        this.nombreFOTO = nombreFOTO;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String urlIMG) {
        this.url = urlIMG;
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
