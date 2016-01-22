package cl.iism.alertaparadero;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cl.iism.alertaparadero.adaptadores.AdaptadorOpciones;
import cl.iism.alertaparadero.adaptadores.AdaptadorRecorrido;
import cl.iism.alertaparadero.clases.Opciones;
import cl.iism.alertaparadero.clases.Paradero;
import cl.iism.alertaparadero.clases.Recorrido;
import cl.iism.alertaparadero.database.BusquedaSQLiteHelper;
import cl.iism.alertaparadero.database.ParaderoSQLiteHelper;
import cl.iism.alertaparadero.utilidades.Conexion;
import cl.iism.alertaparadero.utilidades.ServicioAlerta;
import cl.iism.alertaparadero.utilidades.ServicioLocacion;

public class Home extends Activity {
    RelativeLayout layoutParadero;
    RelativeLayout layoutRecorrido;
    RelativeLayout layoutListaParadero;

    TextView txt_nombre_paradero;
    TextView txt_comuna_paradero;
    TextView txt_codigo_paradero;
    TextView txt_parada_paradero;

    TextView txt_cod_recorrido;
    TextView txt_hacia_recorrido;
    TextView txt_empresa_recorrido;

    TextView txt_lun_vie_recorrido;
    TextView txt_sab_recorrido;
    TextView txt_dom_recorrido;

    GridView grid_opciones_paradero;

    ArrayList<Recorrido> recorridos;
    ArrayList<Opciones> opciones;
    ArrayList<Paradero> Listaparaderos;

    GridView gridRecorridos;
    GridView gridListaParaderos;

    Conexion conexion;

    public Context context;

    Button btnIr;
    AutoCompleteTextView txtParadero;

    ImageView img_incono_recorrido;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public Home(){
        this.context = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Si el servicio alerta paradero está activado
        ActivityManager manager  = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(ServicioLocacion.class.getName().equals(service.service.getClassName())){
                Intent intent = new Intent(this, VistaAlertaParadero.class);
                startActivity(intent);
                finish();
            }
        }

        ParaderoSQLiteHelper paraderoSQL = new ParaderoSQLiteHelper(this, "AlertaParadero", null, 1);
        SQLiteDatabase db = paraderoSQL.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT DISTINCT codigo FROM paradero;", null);
        ArrayList<String> arrayParadero = new ArrayList<String>();

        while (cursor.moveToNext()){
            Log.d("BaseDatos", cursor.getString(cursor.getColumnIndex("codigo")));
            arrayParadero.add(cursor.getString(cursor.getColumnIndex("codigo")));
        }

        db.close();

        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("Alerta Paradero");

        context = this;


        txtParadero = (AutoCompleteTextView) findViewById(R.id.txt_paradero);

        // Se agregan los paraderos buscados recientemente
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Home.this, android.R.layout.simple_list_item_1, arrayParadero);
        txtParadero.setAdapter(adapter);

        btnIr = (Button) findViewById(R.id.btn_ir);

        layoutParadero = (RelativeLayout) findViewById(R.id.layaoutParadero);
        layoutRecorrido = (RelativeLayout) findViewById(R.id.layout_recorrido);

        TextView txt_cerrar_paradero = (TextView) findViewById(R.id.txt_cerrar_paradero);

        txt_nombre_paradero = (TextView) findViewById(R.id.txt_nombre_paradero);
        txt_comuna_paradero = (TextView) findViewById(R.id.txt_comuna_paradero);
        txt_codigo_paradero = (TextView) findViewById(R.id.txt_codigo_paradero);
        txt_parada_paradero = (TextView) findViewById(R.id.txt_parada_paradero);

        txt_cod_recorrido = (TextView) findViewById(R.id.lista_paradero_codigo);
        txt_hacia_recorrido = (TextView) findViewById(R.id.txt_hacia_recorrido);
        txt_empresa_recorrido = (TextView) findViewById(R.id.txt_empresa_recorrido);

        txt_lun_vie_recorrido = (TextView) findViewById(R.id.txt_lun_vie_recorrido);
        txt_sab_recorrido = (TextView) findViewById(R.id.txt_sab_recorrido);
        txt_dom_recorrido = (TextView) findViewById(R.id.txt_dom_recorrido);

        img_incono_recorrido = (ImageView) findViewById(R.id.img_icono_recorrido);

        gridRecorridos = (GridView) findViewById(R.id.girdRecorridos);
        gridListaParaderos = (GridView) findViewById(R.id.gridListaParaderos);

        grid_opciones_paradero = (GridView) findViewById(R.id.grid_opciones_paradero);

        layoutListaParadero = (RelativeLayout) findViewById(R.id.layoutListaParadero);

        // Se cargan las opciones del paradero
        opciones = new ArrayList<Opciones>();
        opciones.add(new Opciones("Activar Alerta Paradero", 0));
        opciones.add(new Opciones("¿Dónde tomo la micro?", 1));
        opciones.add(new Opciones("Ver más información", 2));
        opciones.add(new Opciones("", 3));
        opciones.add(new Opciones("Modo Offline", 4));
        opciones.add(new Opciones("", 5));
        opciones.add(new Opciones("Reportar un problema", 6));

        grid_opciones_paradero.setAdapter(new AdaptadorOpciones(this, opciones, txt_codigo_paradero.getText().toString()));
        grid_opciones_paradero.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Seleccion", "posicion: " + position);
                if (position == 0) {
                    /*Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();*/

                    AlertDialog alerta = new AlertDialog.Builder(Home.this)
                            .setIcon(R.mipmap.ic_icono_paradero)
                    .setTitle("Activando Alerta Paradero")
                    .setMessage("Al presionar Aceptar activarás Alerta Paradero: Cuando estemos llegando al paradero que seleccionaste, te avisaremos mediante una alarma. ")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent service = new Intent(context, ServicioLocacion.class);
                            Intent intent = new Intent(context, VistaAlertaParadero.class);
                            startService(service);
                            startActivity(intent);

                            finish();
                        }
                    })
                    .setNegativeButton("Cancelar", null).create();
                    alerta.show();
                }
            }
        });


        layoutParadero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (grid_opciones_paradero.getVisibility() == View.INVISIBLE) {
                    gridRecorridos.setVisibility(View.INVISIBLE);
                    layoutRecorrido.setVisibility(View.INVISIBLE);
                    grid_opciones_paradero.setVisibility(View.VISIBLE);

                    SharedPreferences preferences = getSharedPreferences("mode_offline", MODE_PRIVATE);
                    if (preferences.getString(txt_codigo_paradero.getText().toString(), null) != null) {
                        //View view = (View) grid_opciones_paradero.getItemAtPosition(4);
                        View view = (View) grid_opciones_paradero.getChildAt(4);
                        Switch switch_offline = (Switch) view.findViewById(R.id.switch_modo_offline);
                        switch_offline.setChecked(true);
                    } else {
                        // View view = (View) grid_opciones_paradero.getItemAtPosition(4);
                        View view = (View) grid_opciones_paradero.getChildAt(4);
                        Switch switch_offline = (Switch) view.findViewById(R.id.switch_modo_offline);
                        switch_offline.setChecked(false);
                    }
                } else {
                    gridRecorridos.setVisibility(View.VISIBLE);
                    grid_opciones_paradero.setVisibility(View.INVISIBLE);
                }
            }
        });

        layoutRecorrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridRecorridos.setVisibility(View.VISIBLE);
                layoutRecorrido.setVisibility(View.INVISIBLE);

                txtParadero.setEnabled(true);
                btnIr.setEnabled(true);
            }
        });

        // Agrego los Paraderos
        recorridos = new ArrayList<Recorrido>();


        txt_cerrar_paradero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutParadero.setVisibility(View.INVISIBLE);
                btnIr.setVisibility(View.VISIBLE);
                txtParadero.setVisibility(View.VISIBLE);
            }
        });
        btnIr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Se oculta el teclado
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtParadero.getWindowToken(), 0);

                // Se verifica que el texto del paradero tenga información
                if (txtParadero.getText().equals("")) {
                    AlertDialog alerta = new AlertDialog.Builder(Home.this)
                            .setTitle(R.string.home_dialog_title_nadaquebuscar)
                            .setMessage(R.string.home_dialog_message_error1)
                            .setPositiveButton(R.string.home_dialog_button_aceptar, null).create();
                    Vibrator vibrar = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    alerta.show();
                    vibrar.vibrate(500);
                    imm.showSoftInputFromInputMethod(txtParadero.getWindowToken(), 0);
                } else if (txtParadero.getText().length() <= 2) {
                    AlertDialog alerta = new AlertDialog.Builder(Home.this)
                            .setTitle(R.string.home_dialog_title_nadaquebuscar)
                            .setMessage(R.string.home_dialog_message_error2)
                            .setPositiveButton(R.string.home_dialog_button_aceptar, null).create();
                    Vibrator vibrar = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    alerta.show();
                    vibrar.vibrate(500);
                    imm.showSoftInputFromInputMethod(txtParadero.getWindowToken(), 0);
                } else {
                    Pattern pattern = Pattern.compile("^P[A-J]\\d{1,4}$");
                    Matcher matcher = pattern.matcher(txtParadero.getText().toString().toUpperCase());
                    if (matcher.find()) {
                        Log.i("AlertaParadero", "Se busca un paradero");
                        buscarParadero(txtParadero.getText().toString());
                    } else {
                        Log.i("AlertaParadero", "Se busca una dirección");
                        buscarDireccion(txtParadero.getText().toString());
                    }
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        // Verifica si debe mostrar un paradero deinmediato
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            buscarParadero(bundle.getString("codigoParadero"));
        }
    }


    private void buscarParadero(String texto) {
        Log.d("Búsqueda", "Buscando por código paradero");

        conexion = new Conexion();
        conexion.setContext(this);

        try {
            JSONObject json = conexion.execute("ws_obtiene_informacion_paradero", "codigo=" + texto).get();
            // Verifica la repsuesta
            JSONObject header = json.getJSONObject("header");
            if (header.getString("coderr").equals("00")) {
                Log.d("Respuesta", json.toString());
                layoutParadero.setVisibility(View.VISIBLE);
                gridRecorridos.setVisibility(View.VISIBLE);
                layoutRecorrido.setVisibility(View.INVISIBLE);
                grid_opciones_paradero.setVisibility(View.INVISIBLE);
                txtParadero.setVisibility(View.INVISIBLE);
                btnIr.setVisibility(View.INVISIBLE);

                // Parsea el json para completar la ficha del paradero
                JSONObject output = json.getJSONObject("output");

                // Se almacena la información del paradero seleccionado
                SharedPreferences.Editor sharedPreferences = getSharedPreferences("AlertaParadero_Paradero", MODE_PRIVATE).edit();
                sharedPreferences.putString("nombre", output.getString("nombre"));
                sharedPreferences.putString("codigo", output.getString("codigo"));
                sharedPreferences.putString("latitud", output.getString("latitud"));
                sharedPreferences.putString("longitud", output.getString("longitud"));
                sharedPreferences.putBoolean("alarma1", false);
                sharedPreferences.putBoolean("alarma2", false);
                sharedPreferences.putBoolean("alarma3", false);
                sharedPreferences.commit();

                txt_nombre_paradero.setText(output.getString("nombre"));
                txt_comuna_paradero.setText(output.getString("comuna"));
                txt_codigo_paradero.setText(output.getString("codigo"));

                // Verifica si el paradero tiene parada
                if ((!output.getString("parada").equals("0")) && (!output.isNull("parada"))) {
                    txt_parada_paradero.setText(output.getString("parada"));
                    txt_parada_paradero.setVisibility(View.VISIBLE);
                } else {
                    txt_parada_paradero.setVisibility(View.INVISIBLE);
                }


                // Incorpora los recorridos dentro del paradero
                JSONArray jsonRecorridos = output.getJSONArray("recorridos");
                recorridos.clear();
                for (int i = 0; i < jsonRecorridos.length(); i++) {
                    JSONObject jsonRecorrido = jsonRecorridos.getJSONObject(i);
                    recorridos.add(new Recorrido(jsonRecorrido.getString("codigo"), jsonRecorrido.getString("hacia"), jsonRecorrido.getString("color"), i));
                }
                gridRecorridos.removeAllViewsInLayout();
                gridRecorridos.setAdapter(new AdaptadorRecorrido(Home.this, recorridos));


                // Se le agrega la capacidad al GridView para seleccionar un recorrido
                gridRecorridos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        Log.d("Evento", "click sobre recorrido");

                        Recorrido recorrido = (Recorrido) recorridos.get(position);


                        conexion = new Conexion();
                        conexion.setContext(Home.this);

                        JSONObject json = null;
                        try {
                            json = conexion.execute("ws_obtiene_informacion_recorrido", "recorrido=" + recorrido.getCodigo()).get();
                        } catch (Exception e) {

                        }

                        try {
                            JSONObject output = json.getJSONObject("output");

                            txt_cod_recorrido.setText("Recorrido: " + output.getString("codigo"));
                            txt_hacia_recorrido.setText("Hacia " + recorrido.getDireccion());
                            txt_empresa_recorrido.setText(output.getString("empresa"));

                            txt_lun_vie_recorrido.setText(output.getString("lun_vie"));
                            txt_sab_recorrido.setText(output.getString("sab"));
                            txt_dom_recorrido.setText(output.getString("dom_fes"));

                            if (output.getString("color").equals("AZ")) {
                                img_incono_recorrido.setImageResource(R.mipmap.ic_recorrido_azul);
                            }
                            if (output.getString("color").equals("AM")) {
                                img_incono_recorrido.setImageResource(R.mipmap.ic_recorrido_amarillo);
                            }
                            if (output.getString("color").equals("CE")) {
                                img_incono_recorrido.setImageResource(R.mipmap.ic_recorrido_celeste);
                            }
                            if (output.getString("color").equals("NA")) {
                                img_incono_recorrido.setImageResource(R.mipmap.ic_recorrido_naranjo);
                            }
                            if (output.getString("color").equals("RO")) {
                                img_incono_recorrido.setImageResource(R.mipmap.ic_recorrido_rojo);
                            }
                            if (output.getString("color").equals("VE")) {
                                img_incono_recorrido.setImageResource(R.mipmap.ic_recorrido_verde);
                            }
                            if (output.getString("color").equals("XX")) {
                                img_incono_recorrido.setImageResource(R.mipmap.ic_recorrido_xx);
                            }


                            layoutRecorrido.setVisibility(View.VISIBLE);
                            gridRecorridos.setVisibility(View.INVISIBLE);
                        } catch (Exception e) {

                        }
                    }
                });


            } else {
                layoutParadero.setVisibility(View.INVISIBLE);
                AlertDialog alerta = new AlertDialog.Builder(this)
                        .setTitle("UPS! Paradero no encontrado")
                        .setMessage("Lamentablemente no pudimos encontrar el paradero que buscas.")
                        .setNegativeButton("Aceptar", null)
                        .setPositiveButton("Agregar paradero", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Home.this, AgregarParadadero.class);
                                intent.putExtra("codigo", txt_codigo_paradero.getText().toString());
                                startActivity(intent);
                            }
                        }).create();
                alerta.show();
            }
        } catch (Exception e) {

        }
    }


    private void buscarDireccion(String texto) {
        Log.d("Búsqueda", "Buscando por dirección");

        Intent intent = new Intent(Home.this, Lista_Paraderos_Cercanos.class);
        intent.putExtra("direccion", txtParadero.getText().toString());
        startActivity(intent);
        finish();
    }
}