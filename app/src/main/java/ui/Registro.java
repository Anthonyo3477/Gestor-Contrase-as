package ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestor_contrasea.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Activities.MainActivity;
import modules.User;

public class Registro extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private EditText txtCorreo, txtContraseña, txtConfirmarContraseña;
    private Button btnRegistrar, btnVolver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("credenciales");
        mAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(this);

        // Reference views
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContraseña = findViewById(R.id.txtContraseña);
        txtConfirmarContraseña = findViewById(R.id.txtConfirmarContraseña);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVolver = findViewById(R.id.btnVolver);

        // Action for the back button
        btnVolver.setOnClickListener(view -> {
            Intent intent = new Intent(Registro.this, MainActivity.class);
            startActivity(intent);
        });

        // Action for the register button
        btnRegistrar.setOnClickListener(view -> {
            String email = txtCorreo.getText().toString().trim();
            String pass = txtContraseña.getText().toString().trim();
            String confirmPass = txtConfirmarContraseña.getText().toString().trim();

            guardarUsuario(email, pass, confirmPass);
        });
    }

    // Method to validate and save user
    private void guardarUsuario(String email, String pass, String confirmPass) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirmPass)) {
            Toast.makeText(Registro.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.endsWith("@gmail.com")) {
            Toast.makeText(Registro.this, "Ingrese un correo Gmail válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pass.length() < 6 || !pass.equals(confirmPass)) {
            Toast.makeText(Registro.this, "Las contraseñas no coinciden o son menores a 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        registroUsuario(email, pass);
    }

    // Method to register user in Firebase Authentication and save additional data in the database
    private void registroUsuario(String email, String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        guardarDatosCredenciales(user, email, pass);
                    } else {
                        Toast.makeText(Registro.this, "El registro falló: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to save additional data in Firebase database
    private void guardarDatosCredenciales(FirebaseUser user, String email, String pass) {
        if (user == null) {
            Toast.makeText(Registro.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set default or hardcoded values for username and app name
        String usuario = "NombreUsuarioPredeterminado";
        String nombreApp = "NombreAppPredeterminada";

        String id = user.getUid();
        // Ahora, no pasamos la contraseña en la creación de la clase User
        User credentials = new User("", pass, email,"" ,"", "" );

        // Guardamos los datos en la base de datos de Firebase
        databaseReference.child(id).setValue(credentials).addOnSuccessListener(aVoid -> {
                    Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Registro.this, Login.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Registro.this, "Error al guardar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FirebaseError","Error al guardar los datos",e);
                });
    }
}
