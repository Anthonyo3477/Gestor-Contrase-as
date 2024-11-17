package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestor_contrasea.R;

import ui.Login;
import ui.Registro;

public class MainActivity extends AppCompatActivity {
    private Button btnIngresar, btnCrearSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ventana_inicio);

        btnIngresar = findViewById(R.id.btnIngresar);
        btnCrearSesion = findViewById(R.id.btnCrearSesion);

        if (btnIngresar == null) {
            Log.e("MainActivity", "btnIngresar is null");

        }
        if (btnCrearSesion == null) {
            Log.e("MainActivity", "btnCrearSesion is null");
        }

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent); // Corrección aquí
            }
        });

        btnCrearSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Registro.class);
                startActivity(intent); // Corrección aquí
            }
        });
    }
}