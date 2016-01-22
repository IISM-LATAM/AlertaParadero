package cl.iism.alertaparadero.clases;

/**
 * Created by gvalles on 18-12-2015.
 */
public class Opciones {
    private String nombreOpcion;
    private int Id;

    public Opciones(String nombreOpcion, int Id){
        this.nombreOpcion = nombreOpcion;
        this.Id = Id;
    }

    public String getOpcion(){
        return this.nombreOpcion;
    }

    public int getId(){
        return this.Id;
    }
}
