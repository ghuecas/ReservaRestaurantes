package es.upm.dit.adsw.reservarestaurantes;

/**
 * Created by Gabriel Huecas <gabriel.huecas@upm.es> on 22/7/16.
 */

public class Restaurante {
    private String nombre;
    private String direccion;
    private String telefono;
    private String tipo;

    public Restaurante(String nombre, String direccion, String telefono,
                       String tipo)
    {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.tipo = tipo;
    }


    /**
     * @return the nombre
     */
    public String getNombre()
    {
        return nombre;
    }


    /**
     * @return the direccion
     */
    public String getDireccion()
    {
        return direccion;
    }


    /**
     * @return the telefono
     */
    public String getTelefono()
    {
        return telefono;
    }


    /**
     * @return the tipo
     */
    public String getTipo()
    {
        return tipo;
    }


    public int compareTo(Restaurante otro)
    {
        if (otro == null)
            return 1;
        if (this == otro)
            return 0;
        return this.toString().compareToIgnoreCase(otro.toString());
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        // TODO Auto-generated method stub
        return nombre + ";" + direccion;
    }
}
