package Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestor_contrasea.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import adapters.Encrypation; // Importar la clase de encriptación
import modules.User;

public class EditarUsuario extends AppCompatActivity {

    private EditText txtNombreWeb, txtNombreUsuario, txtCorreo, txtContraseña;
    private Button btnGuardarCambios, btnCancelar;
    private DatabaseReference databaseReference;
    private String appId; // ID único del usuario en Firebase
    private String contraseñaOriginal; // Contraseña original encriptada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_usuario);

        // Referencias de los elementos de la vista
        txtNombreWeb = findViewById(R.id.txtNombreSitio);
        txtNombreUsuario = findViewById(R.id.txtNombreUsuario);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContraseña = findViewById(R.id.txtContraseña);
        btnGuardarCambios = findViewById(R.id.btnEditar);
        btnCancelar = findViewById(R.id.btnVolver);

        // Inicializar Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Aplicaciones");

        // Obtener datos del Intent
        appId = getIntent().getStringExtra("appId");
        String nombreApp = getIntent().getStringExtra("nombreApp");
        String nombreUsuario = getIntent().getStringExtra("nombreUsuario");
        String correo = getIntent().getStringExtra("correo");
        contraseñaOriginal = getIntent().getStringExtra("contraseña"); // Contraseña encriptada

        Log.d("EditarUsuario", "Datos recibidos: appId=" + appId +
                ", nombreApp=" + nombreApp +
                ", nombreUsuario=" + nombreUsuario +
                ", correo=" + correo +
                ", contraseña=" + contraseñaOriginal);

        // Cargar datos en los campos (manejar nulos)
        txtNombreWeb.setText(nombreApp != null ? nombreApp : "");
        txtNombreUsuario.setText(nombreUsuario != null ? nombreUsuario : "");
        txtCorreo.setText(correo != null ? correo : "");
        txtContraseña.setText(desencriptarContraseña(contraseñaOriginal)); // Desencripta para mostrar

        btnGuardarCambios.setOnClickListener(v -> actualizarUsuario());
        btnCancelar.setOnClickListener(v -> finish()); // Cierra la actividad actual
    }

    // Método para desencriptar la contraseña antes de mostrarla
    private String desencriptarContraseña(String contraseñaEncriptada) {
        if (TextUtils.isEmpty(contraseñaEncriptada)) {
            return ""; // Si no hay contraseña encriptada, retorna vacío
        }
        String contraseñaDesencriptada = Encrypation.desencriptarContraseñaAES(contraseñaEncriptada);
        if (contraseñaDesencriptada == null) {
            Toast.makeText(this, "Error al desencriptar la contraseña", Toast.LENGTH_SHORT).show();
            return ""; // Manejo de errores, retorna vacío en caso de fallo
        }
        return contraseñaDesencriptada;
    }

    // Método para actualizar el usuario en Firebase
    private void actualizarUsuario() {
        String nombreApp = txtNombreWeb.getText().toString().trim();
        String nombreUsuario = txtNombreUsuario.getText().toString().trim();
        String correo = txtCorreo.getText().toString().trim();
        String contraseña = txtContraseña.getText().toString().trim();

        // Validar campos
        if (TextUtils.isEmpty(nombreApp) || TextUtils.isEmpty(nombreUsuario) ||
                TextUtils.isEmpty(correo) || TextUtils.isEmpty(contraseña)) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Encriptar la contraseña solo si ha sido modificada
        String contraseñaEncriptada;
        if (contraseña.equals(desencriptarContraseña(contraseñaOriginal))) {
            // Si la contraseña no cambió, usamos la original encriptada
            contraseñaEncriptada = contraseñaOriginal;
        } else {
            // Si la contraseña cambió, la encriptamos
            contraseñaEncriptada = Encrypation.encriptarContraseñaAES(contraseña);
            if (contraseñaEncriptada == null) {
                Toast.makeText(this, "Error al encriptar la contraseña", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Crear un objeto actualizado basado en la clase User
        User usuarioActualizado = new User(appId, nombreApp, nombreUsuario, correo, contraseñaEncriptada);

        // Actualizar en Firebase
        databaseReference.child(appId).setValue(usuarioActualizado)
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
