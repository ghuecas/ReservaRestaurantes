package es.upm.dit.adsw.reservarestaurantes;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ListaRestaurantesActivity extends AppCompatActivity {

    public final static String TAG= "ReservaRestaurantes";

    final static public int ACTIVITY_CREATE = 0;
    final static public int ACTIVITY_EDIT = 0;

    private ArrayAdapter<Restaurante> adaptador = null;
    private boolean estaCargando = false;

    private TextView progreso;
    private ListView listRestV;

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_restaurantes);

        Log.i(TAG, "onCreate");

        adaptador = new ArrayAdapter<Restaurante>(this,
                R.layout.fila_restaurante, ListaRestaurantesSingleton
                .getInstance().getListaRestaurantes());

        listRestV= (ListView) findViewById(R.id.listRestaurantsView);

        listRestV.setAdapter(adaptador);

        listRestV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), DetalleRestauranteActivity.class);
                i.putExtra("POS", position);

                startActivity(i);

            }
        });

        progreso = (TextView) findViewById(R.id.progreso);
        // progreso.setText("");

        // usamos menú contextual
        registerForContextMenu(listRestV);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);

        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (estaCargando)
        {
            Toast.makeText(
                    this,
                    "No se puede realizar la acción, se está cargando un fichero",
                    Toast.LENGTH_SHORT).show();
            return true;
        }

        switch (item.getItemId())
        {
            case R.id.addItem:
                Intent i = new Intent(this, DetalleRestauranteActivity.class);
                i.putExtra("POS", -1);
                startActivity(i);

                return true;
            case R.id.sortItems:
                ordenar();
                adaptador.notifyDataSetChanged();

                return true;
            case R.id.loadFile:
                if ( ! ListaRestaurantesSingleton.getInstance()
                        .isListaCargadaDeFichero())
                {
                    new CargarAsyncTask().execute();
                    ListaRestaurantesSingleton.getInstance().setListaCargadaDeFichero(true);
                    item.setEnabled(false);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void ordenar()
    {
        List<Restaurante> alr = ListaRestaurantesSingleton.getInstance()
                .getListaRestaurantes();

        quickSortGAB(alr, 0, alr.size() - 1);
    }

    // Intercambia los elementos i y j de la colección A
    public static void intercambia(List<Restaurante> A, int i, int j)
    {
        Restaurante swap = A.get(i);
        A.set(i, A.get(j));
        A.set(j, swap);
    }

    private int particionQuickSort(List<Restaurante> A, int izq, int der)
    {
        int posPivot = izq + (der - izq) / 2;
        Restaurante pivot = A.get(posPivot);

        while (izq <= der)
        {
            while (A.get(izq).compareTo(pivot) < 0)
                izq++;
            while (A.get(der).compareTo(pivot) > 0)
                der--;
            if (izq <= der)
            {
                intercambia(A, izq, der);
                izq++;
                der--;
            }
        }

        return izq;
    }

    private void quickSortGAB(List<Restaurante> A, int izq, int der)
    {
        int lim;

        if (izq < der)
        {
            lim = particionQuickSort(A, izq, der);

            quickSortGAB(A, izq, lim - 1);
            quickSortGAB(A, lim, der);
        }
    }

    private void quickSortPP(List<Restaurante> alr, int a, int z)
    {
        Restaurante pivote = alr.get(a + (z - a) / 2);

        int inf = a;
        int sup = z;
        while (inf < sup)
        {
            while (alr.get(inf).compareTo(pivote) < 0)
                inf++;
            while (pivote.compareTo(alr.get(sup - 1)) < 0)
                sup--;
            if (inf < sup)
            {
                Restaurante tmp = alr.get(inf);
                alr.set(inf, alr.get(sup - 1));
                alr.set(sup - 1, tmp);
                inf++;
                sup--;
            }
        }
        if (a < sup)
            quickSortPP(alr, a, sup);
        if (inf < z)
            quickSortPP(alr, inf, z);

    }

    private class CargarAsyncTask extends AsyncTask<Void, Integer, String>
    {
        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute()
        {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... arg0)
        {
            AssetManager am = getResources().getAssets();
            InputStream entrada = null;
            List<Restaurante> alr = ListaRestaurantesSingleton
                    .getInstance().getListaRestaurantes();

            try
            {
                entrada = am.open("restaurantes.txt");
                InputStreamReader ir = new InputStreamReader(entrada);
                BufferedReader bf = new BufferedReader(ir);
                String linea = bf.readLine();

                int numRestaurantes = Integer.parseInt(linea);

                for (int i = 0; i < numRestaurantes; i++)
                {
                    linea = bf.readLine();

                    // Procesar la línea
                    String[] args = linea.split(";");

                    if (args.length != 4)
                    {
                        return "ERROR: leyendo restaurante";
                    }

                    String nombre = args[0].trim();
                    String direccion = args[1].trim();
                    String telefono = args[2].trim();
                    String tipo = args[3].trim();
                    if (!tipo.equals("tradicional")
                            && !tipo.equals("internacional")
                            && !tipo.equals("comida rapida"))
                        return "ERROR: en tipo, linea: " + (i + 1);

                    Restaurante nr = new Restaurante(nombre, direccion,
                            telefono, tipo);

                    alr.add(nr);

                    int p = ((i + 1) * 100) / numRestaurantes;
                    publishProgress(new Integer(p));

                    Thread.sleep(2000);
                }
                entrada.close();
            }
            catch (IOException e)
            {
                Log.i("ListaRestaurantes", "Imposible abrir el fichero");
                return "ERROR: abriendo o leyendo el fichero";
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.i("ListaRestaurantes", "thread doInBackground interruped");
                return "ERROR: doInBackground interrumpido";
            }
            return "OK";
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);

            int porcentajeProgreso = values[0].intValue();
            progreso.setText(porcentajeProgreso + "% completado");

            adaptador.notifyDataSetChanged();
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String result)
        {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (!result.equals("OK"))
            {
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            adaptador.notifyDataSetChanged();
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Log.i(TAG, "onBackPressed");
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onRestart()
     */
    @Override
    protected void onRestart()
    {
        super.onRestart();

        Log.i(TAG, "onRestart");
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onStart()
     */
    @Override
    protected void onStart()
    {
        super.onStart();

        Log.i(TAG, "onStart");
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        Log.i(TAG, "onResume");

        adaptador.notifyDataSetChanged(); // siempre, qué poco estilo
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause()
    {
        super.onPause();

        Log.i(TAG, "onPause");
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onStop()
     */
    @Override
    protected void onStop()
    {
        super.onStop();

        Log.i(TAG, "onStop");
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        Log.i(TAG, "onDestroy");
    }
}
