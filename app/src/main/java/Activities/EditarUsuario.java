package Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestor_contrasea.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import modules.User;

public class EditarUsuario extends AppCompatActivity {

    private EditText txtNombreWeb, txtNombreUsuario, txtCorreo, txtContraseña, txtConfirmarContraseña;
    private Button btnGuardarCambios, btnCancelar;
    private DatabaseReference databaseReference;
    private String usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_usuario);

        // Referencias de los elementos de la vista
        txtNombreWeb = findViewById(R.id.txtNombreSitio);
        txtNombreUsuario = findViewById(R.id.txtNombreUsuario);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContraseña = findViewById(R.id.txtContraseña);
        txtConfirmarContraseña = findViewById(R.id.txtConfirmarContraseña);
        btnGuardarCambios = findViewById(R.id.btnEditar);
        btnCancelar = findViewById(R.id.btnVolver);

        // Inicializar Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("credenciales");

        // Obtener datos del intent
        usuarioId = getIntent().getStringExtra("usuarioId");
        String nombreWeb = getIntent().getStringExtra("nombreWeb");
        String nombreUsuario = getIntent().getStringExtra("nombreUsuario");
        String correo = getIntent().getStringExtra("correo");
        String contraseña = getIntent().getStringExtra("contraseña");

        Log.d("EditarUsuario", "Datos recibidos: usuarioId=" + usuarioId +
                ", nombreWeb=" + nombreWeb +
                ", nombreUsuario=" + nombreUsuario +
                ", correo=" + correo +
                ", contraseña=" + contraseña);

        // Cargar datos en los campos (manejar nulos)
        txtNombreWeb.setText(nombreWeb != null ? nombreWeb : "");
        txtNombreUsuario.setText(nombreUsuario != null ? nombreUsuario : "");
        txtCorreo.setText(correo != null ? correo : "");
        txtContraseña.setText(contraseña != null ? contraseña : "");

        // Configurar botón Guardar Cambios
        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarUsuario();
            }
        });

        // Configurar botón Cancelar
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cierra la actividad actual
            }
        });
    }

    // Método para actualizar el usuario en Firebase
    private void actualizarUsuario() {
        String nombreWeb = txtNombreWeb.getText().toString().trim();
        String nombreUsuario = txtNombreUsuario.getText().toString().trim();
        String correo = txtCorreo.getText().toString().trim();
        String contraseña = txtContraseña.getText().toString().trim();
        String confirmarContraseña = txtConfirmarContraseña.getText().toString().trim();

        // Validar campos
        if (TextUtils.isEmpty(nombreWeb) || TextUtils.isEmpty(nombreUsuario) ||
                TextUtils.isEmpty(correo) || TextUtils.isEmpty(contraseña) || TextUtils.isEmpty(confirmarContraseña)) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contraseña.equals(confirmarContraseña)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un objeto actualizado
        User usuarioActualizado = new User(usuarioId, nombreWeb, nombreUsuario, correo, contraseña, confirmarContraseña);

        // Actualizar en Firebase
        databaseReference.child(usuarioId).setValue(usuarioActualizado)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditarUsuario.this, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();
                    finish(); // Cierra la actividad actual
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditarUsuario.this, "Error al actualizar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("EditarUsuario", "Error al actualizar: ", e);
                });
    }
}
