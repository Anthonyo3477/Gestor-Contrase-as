package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestor_contrasea.R;

public class MenuUsuario extends AppCompatActivity {

    // Declaración de botones
    private Button btnCrearUsuario, btnEditarUsuario, btnEliminarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_usuario);  // Establecer el layout de esta actividad

        // Inicializa los botones mediante findViewById
        btnCrearUsuario = findViewById(R.id.CrearAplicacion);
        btnEditarUsuario = findViewById(R.id.EditarUsuario);
        btnEliminarUsuario = findViewById(R.id.EliminarUsuario);

        // Configura el comportamiento del botón Crear Usuario
        btnCrearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige a la actividad CrearUsuario
                Intent intent = new Intent(MenuUsuario.this, CrearUsuario.class);
                startActivity(intent);
            }
        });

        // Configura el comportamiento del botón Editar Usuario
        btnEditarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige a la actividad EditarUsuario
                Intent intent = new Intent(MenuUsuario.this, EditarUsuario.class);
                startActivity(intent);
            }
        });

        // Configura el comportamiento del botón Eliminar Usuario
        btnEliminarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige a la actividad EliminarUsuario
                Intent intent = new Intent(MenuUsuario.this, EliminarUsuario.class);
                startActivity(intent);
            }
        });
    }
}
