package Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestor_contrasea.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import modules.User;

public class CrearUsuario extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private Button btnCrear, btnVolver;
    private EditText txtNombreSitio, txtNombreUsuario, txtCorreo, txtContraseña, txtConfirmarContraseña;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_usuario);

        // Inicializar Firebase Auth y Database
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("user"); // Nodo donde se almacenarán los datos

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

            guardarUsuario(nombreApp, nombreUsuario, correo, contraseña, confirmarContraseña);
        });

        // Evento del botón Volver
        btnVolver.setOnClickListener(view -> volver());
    }

    // Método para validar los datos y registrar al usuario
    private void guardarUsuario(String nombreApp, String nombreUsuario, String correo, String contraseña, String confirmarContraseña) {
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

        registrarUsuario(correo, contraseña, nombreUsuario, nombreApp);
    }

    // Método para crear un usuario en Firebase Authentication
    private void registrarUsuario(String correo, String contraseña, String nombreUsuario, String nombreApp) {
        mAuth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        guardarDatos(user, nombreUsuario, nombreApp);
                    } else {
                        Toast.makeText(CrearUsuario.this, "Error al crear usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Método para guardar datos adicionales en la base de datos de Firebase
    private void guardarDatos(FirebaseUser user, String nombreUsuario, String nombreApp) {
        if (user == null) {
            Toast.makeText(CrearUsuario.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        User usuario = new User( user ,nombreUsuario, nombreApp); // Asegúrate de que esta clase tenga un constructor adecuado

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // Guardar los datos en Firebase Database
        databaseReference.child(userId).setValue(usuario)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CrearUsuario.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                    volver();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CrearUsuario.this, "Error al guardar los datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FirebaseError", "Error al guardar sus datos", e);
                });
    }

    // Método para regresar a la pantalla anterior
    private void volver() {
        finish();
    }
}
