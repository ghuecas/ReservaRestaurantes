package es.upm.dit.adsw.reservarestaurantes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabriel Huecas <gabriel.huecas@upm.es> on 22/7/16.
 */

public class ListaRestaurantesSingleton {

    private static ListaRestaurantesSingleton instance= new ListaRestaurantesSingleton();

    private List<Restaurante> lista= null;
    private boolean listaCargadaDeFichero= false;


    private ListaRestaurantesSingleton()
    {
        lista= new ArrayList<Restaurante>();
    }

    public static synchronized ListaRestaurantesSingleton getInstance ()
    {
        return instance;
    }

    public synchronized List<Restaurante> getListaRestaurantes()
    {
        return lista;
    }

    /**
     * @return the listaCargadaDeFichero
     */
    public synchronized boolean isListaCargadaDeFichero()
    {
        return listaCargadaDeFichero;
    }

    /**
     * @param listaCargadaDeFichero the listaCargadaDeFichero to set
     */
    public synchronized void setListaCargadaDeFichero(boolean listaCargadaDeFichero)
    {
        this.listaCargadaDeFichero = listaCargadaDeFichero;
    }
}
