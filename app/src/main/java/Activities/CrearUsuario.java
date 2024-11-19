package Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestor_contrasea.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CrearUsuario extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private Button btnCrear, btnVolver;
    private EditText txtNombreSitio, txtNombreUsuario, txtCorreo, txtContraseña, txtConfirmarContraseña;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_usuario);

        // Inicializar Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Aplicaciones"); // Nodo raíz

        // Referencias a los elementos de la interfaz
        btnCrear = findViewById(R.id.btnCrearAppWeb);
        btnVolver = findViewById(R.id.btnVolver);
        txtNombreSitio = findViewById(R.id.txtNombreSitio);
        txtNombreUsuario = findViewById(R.id.txtNombreUsuario);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContraseña = findViewById(R.id.txtContraseña);
        txtConfirmarContraseña = findViewById(R.id.txtConfirmarContraseña);

        // Evento del botón Crear
        btnCrear.setOnClickListener(view -> {
            String nombreApp = txtNombreSitio.getText().toString();
            String nombreUsuario = txtNombreUsuario.getText().toString();
            String correo = txtCorreo.getText().toString();
            String contraseña = txtContraseña.getText().toString();
            String confirmarContraseña = txtConfirmarContraseña.getText().toString();

            guardarAplicacion(nombreApp, nombreUsuario, correo, contraseña, confirmarContraseña);
        });

        // Evento del botón Volver
        btnVolver.setOnClickListener(view -> finish());
    }

    // Método para validar los datos y guardar una nueva aplicación
    private void guardarAplicacion(String nombreApp, String nombreUsuario, String correo, String contraseña, String confirmarContraseña) {
        if (TextUtils.isEmpty(nombreUsuario) || TextUtils.isEmpty(nombreApp) || TextUtils.isEmpty(correo) || TextUtils.isEmpty(contraseña) || TextUtils.isEmpty(confirmarContraseña)) {
            Toast.makeText(CrearUsuario.this, "Por favor, ingrese todos los datos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!correo.endsWith("@gmail.com")) {
            Toast.makeText(CrearUsuario.this, "Ingrese un correo válido (@gmail.com)", Toast.LENGTH_SHORT).show();
            return;
        }

        if (contraseña.length() < 6 || !contraseña.equals(confirmarContraseña)) {
            Toast.makeText(CrearUsuario.this, "Las contraseñas no coinciden o son menores a 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        guardarDatosEnBaseDeDatos(nombreApp, nombreUsuario, correo, contraseña);
    }

    // Guardar los datos en Firebase Database directamente bajo el nodo raíz
    private void guardarDatosEnBaseDeDatos(String nombreApp, String nombreUsuario, String correo, String contraseña) {
        String appId = databaseReference.push().getKey(); // Generar un ID único para la app

        if (appId == null) {
            Toast.makeText(CrearUsuario.this, "Error al generar ID de la aplicación", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> appData = new HashMap<>();
        appData.put("nombreApp", nombreApp);
        appData.put("nombreUsuario", nombreUsuario);
        appData.put("correo", correo);  // Agregar el correo
        appData.put("contraseña", contraseña);  // Agregar la contraseña

        // Guardar directamente bajo el nodo raíz "Aplicaciones"
        databaseReference.child(appId).setValue(appData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CrearUsuario.this, "Aplicación registrada exitosamente", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CrearUsuario.this, "Error al guardar los datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FirebaseError", "Error al guardar datos de la aplicación", e);
                });
    }

    // Limpiar los campos después de registrar
    private void limpiarCampos() {
        txtNombreSitio.setText("");
        txtNombreUsuario.setText("");
        txtCorreo.setText("");
        txtContraseña.setText("");
        txtConfirmarContraseña.setText("");
    }
}
