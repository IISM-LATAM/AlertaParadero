package cl.iism.alertaparadero.clases;

/**
 * Created by gvalles on 24-12-2015.
 */
public class Paradero {
    private String codigo;
    private String nombre;
    private String distancia;
    private int Id;

    public Paradero(String codigo, String nombre, String distancia, int Id){
        this.codigo = codigo;
        this.nombre = nombre;
        this.distancia = distancia;
        this.Id = Id;
    }

    public String getCodigo(){
        return this.codigo;
    }

    public String getNombre(){
        return this.nombre;
    }

    public String getDistancia(){
        return this.distancia;
    }

    public int getId(){
        return this.Id;
    }

    public static Paradero getItem(int position){
        return null;
    }
}
