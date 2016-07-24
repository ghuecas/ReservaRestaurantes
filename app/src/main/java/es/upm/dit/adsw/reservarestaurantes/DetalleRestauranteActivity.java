package es.upm.dit.adsw.reservarestaurantes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DetalleRestauranteActivity extends AppCompatActivity {

    private List listaRestaurantes = new ArrayList();

    private EditText nombreV;
    private EditText direccionV;
    private EditText telefonoV;
    private RadioGroup tipoV;

    private List<Restaurante> lista;

    private int itemPos;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_restaurante);

        Log.i(ListaRestaurantesActivity.TAG, "onCreate");

        nombreV = (EditText) findViewById(R.id.editNombre);
        direccionV = (EditText) findViewById(R.id.editDireccion);
        telefonoV = (EditText) findViewById(R.id.editTelefono);

        tipoV = (RadioGroup) findViewById(R.id.radioTipo);

        // restaurantes (singleton instance)
        lista = ListaRestaurantesSingleton.getInstance().getListaRestaurantes();

        // -1 indicates new element
        itemPos = getIntent().getIntExtra("POS", -1);

        if (itemPos != -1)
        {
            cargaCampos(itemPos);
        }
    }

    private void cargaCampos(int pos)
    {
        if (pos >= 0 && pos < lista.size())
        {
            Restaurante r = lista.get(pos);

            nombreV.setText(r.getNombre());
            direccionV.setText(r.getDireccion());
            telefonoV.setText(r.getTelefono());

            tipoV.clearCheck();
            String tipo = r.getTipo();
            if (tipo.equalsIgnoreCase("Tradicional"))
                tipoV.check(R.id.tradicional);
            else if (tipo.equalsIgnoreCase("Comida Rapida"))
                tipoV.check(R.id.comidaRapida);
            else if (tipo.equalsIgnoreCase("Internacional"))
                tipoV.check(R.id.internacional);
            else
                tipoV.clearCheck(); // cómo es posible?
        }
        else
        {
            limpiaRegistros();
        }
    }

    public void guardar(View v)
    {
        String nombreStr, direccionStr = "", telefonoStr = "", tipoStr;

        if (nombreV.getText().length() <= 0)
        {
            Toast.makeText(this, R.string.errorNombreVacio, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        nombreStr = nombreV.getText().toString();

        if (direccionV.getText().length() > 0)
        {
            direccionStr = direccionV.getText().toString();
        }
        if (telefonoV.getText().length() > 0)
        {
            telefonoStr = telefonoV.getText().toString();
        }

        int tipoId = tipoV.getCheckedRadioButtonId();
        switch (tipoId)
        {
            case R.id.tradicional:
                tipoStr = "Tradicional";
                break;
            case R.id.internacional:
                tipoStr = "Internacional";
                break;
            case R.id.comidaRapida:
                tipoStr = "Comida Rápida";
                break;
            default:
                Toast.makeText(this, R.string.errorElegirTipo, Toast.LENGTH_SHORT)
                        .show();
                return;
        }

        Restaurante nRestaurante = new Restaurante(nombreStr, direccionStr,
                telefonoStr, tipoStr);

        if (itemPos == -1)
        {
            lista.add(nRestaurante);
        }
        else
        {
            lista.remove(itemPos);
            lista.add(itemPos, nRestaurante);
        }

        limpiaRegistros();

        onBackPressed();
    }

    public void cancelar(View v)
    {
        finish();
    }

    private void limpiaRegistros()
    {
        nombreV.setText("");
        direccionV.setText("");
        telefonoV.setText("");

        tipoV.clearCheck();
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

        Log.i(ListaRestaurantesActivity.TAG, "onBackPressed");
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

        Log.i(ListaRestaurantesActivity.TAG, "onRestart");
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

        Log.i(ListaRestaurantesActivity.TAG, "onStart");
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

        Log.i(ListaRestaurantesActivity.TAG, "onResume");
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

        Log.i(ListaRestaurantesActivity.TAG, "onPause");
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

        Log.i(ListaRestaurantesActivity.TAG, "onStop");
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

        Log.i(ListaRestaurantesActivity.TAG, "onDestroy");
    }
}
