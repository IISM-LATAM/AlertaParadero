package cl.iism.alertaparadero;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by gvalles on 20-12-2015.
 */
public class AgregarParader_capture extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_paradero_captura);

        Bundle bundle = getIntent().getExtras();

        if(bundle.get("data") != null){
            Bitmap bitmap = (Bitmap) bundle.get("data");

            ImageView imageView = (ImageView) findViewById(R.id.img_agregar_paradero_captura);
            imageView.setImageBitmap(bitmap);
        }
    }
}
