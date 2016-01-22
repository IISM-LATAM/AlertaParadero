package cl.iism.alertaparadero.clases;

/**
 * Created by gvalles on 16-12-2015.
 */
public class Recorrido {
    private String codigo;
    private String direccion;
    private String color;
    private int id;


    public Recorrido(String codigo, String direccion, String color, int id){
        this.codigo = codigo;
        this.direccion = direccion;
        this.color = color;
        this.id = id;
    }

    public String getCodigo(){
        return this.codigo;
    }

    public String getDireccion(){
        return this.direccion;
    }

    public String getColor() { return this.color; }

    public int getId(){
        return this.id;
    }

    public static Recorrido getItem(int id){
        return null;
    }
}
