package cl.iism.alertaparadero.utilidades;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by gvalles on 11-12-2015.
 */
public class Conexion extends AsyncTask<String, Void, JSONObject>{
    ProgressDialog progressDialog;
    Context context;

    private String baseURL = "http://api.alertaparadero.cl/v1/";
    // private String baseURL = "http://dev.api.alertaparadero.cl/v1/";

    public void setContext(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Buscando paradero");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        progressDialog.show();
    }

    protected JSONObject doInBackground(String... params) {
        HttpURLConnection conn = null;

        try{
            URL url = new URL(baseURL + params[0] + ".php?" + params[1]);
            Log.d("URL", url.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-length", "0");
            conn.setUseCaches(true);
            conn.setAllowUserInteraction(false);
            conn.setConnectTimeout(50000);
            conn.setReadTimeout(50000);
            conn.connect();

            int status = conn.getResponseCode();

            switch(status){
                case 200:
                case 201:
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while((line = bufferedReader.readLine()) != null){
                        stringBuilder.append(line);
                    }
                    bufferedReader.close();

                    try {
                        return new JSONObject(stringBuilder.toString());
                    }catch(Exception e){
                        return null;
                    }
            }
        }catch(MalformedURLException e){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }catch(IOException e){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }finally {
            if(conn != null){
                try{
                    conn.disconnect();
                }catch(Exception e){
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        progressDialog.dismiss();
    }
}
