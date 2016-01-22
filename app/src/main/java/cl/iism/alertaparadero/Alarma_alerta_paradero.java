package cl.iism.alertaparadero;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class Alarma_alerta_paradero extends Activity {
    MediaPlayer mMediaPlayer;
    Ringtone r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma_alerta_paradero);
        setTitle("Alarma!!!");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle bundle = getIntent().getExtras();

        TextView txt_alarma = (TextView) findViewById(R.id.txt_texto_alarma);

        if(bundle.getInt("tipo", 0) == 1){
            txt_alarma.setText("Es hora de despertar");
        }
        if(bundle.getInt("tipo", 0) == 2){
            txt_alarma.setText("Es hora de pararse");
        }
        if(bundle.getInt("tipo", 0) == 3){
            txt_alarma.setText("Toca el timbre ya!!!");
        }

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        r.stop();
        finish();
    }

    @Override
    public void onPause(){
        super.onPause();
        r.stop();
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
