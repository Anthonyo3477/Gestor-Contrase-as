package Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestor_contrasea.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import modules.User;

public class EditarUsuario extends AppCompatActivity {

    private EditText txtNombreSitio, txtNombreUsuario, txtCorreo, txtContrasena;
    private Button btnEditar, btnVolver;
    private DatabaseReference databaseReference;
    private String usuarioId;  // Almacenar el ID del usuario para editar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_usuario);  // Asegúrate de que coincida con el archivo de diseño XML

        // Inicializar vistas
        txtNombreSitio = findViewById(R.id.txtNombreSitio);
        txtNombreUsuario = findViewById(R.id.txtNombreUsuario);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContrasena = findViewById(R.id.txtContrasena);
        btnEditar = findViewById(R.id.btnEditar);
        btnVolver = findViewById(R.id.btnVolver);

        // Referencia de Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // Configurar listeners de botones
        btnEditar.setOnClickListener(view -> buscarUsuarioPorCorreo(txtCorreo.getText().toString().trim()));
        btnVolver.setOnClickListener(view -> guardarCambios());
    }

    // Obtener datos del usuario basado en el correo y poblar campos
    private void buscarUsuarioPorCorreo(String correo) {
        if (!correo.isEmpty()) {
            Query query = databaseReference.orderByChild("mail").equalTo(correo);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                            User usuario = usuarioSnapshot.getValue(User.class);
                            if (usuario != null) {

                                usuarioId = usuarioSnapshot.getKey();  // Guardar ID para editar
                                txtNombreSitio.setText(usuario.getNombreWeb());
                                txtNombreUsuario.setText(usuario.getNombreUsuario());
                                txtCorreo.setText(usuario.getCorreo());
                                txtContrasena.setText(usuario.getContraseña());  // Solo si el campo de contraseña es visible
                            }
                        }
                    } else {
                        Toast.makeText(EditarUsuario.this, "Usuario no encontrado.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(EditarUsuario.this, "Error al buscar usuario.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Ingrese un correo para buscar.", Toast.LENGTH_SHORT).show();
        }
    }

    // Actualizar datos del usuario en Firebase
    private void guardarCambios() {
        if (usuarioId != null) {
            String nombreSitio = txtNombreSitio.getText().toString().trim();
            String nombreUsuario = txtNombreUsuario.getText().toString().trim();
            String correo = txtCorreo.getText().toString().trim();
            String contrasena = txtContrasena.getText().toString().trim();

            // Preparar datos para actualizar
            Map<String, Object> updates = new HashMap<>();
            updates.put("nombreSitio", nombreSitio);
            updates.put("userName", nombreUsuario);
            updates.put("mail", correo);
            updates.put("password", contrasena);  // Almacenar contraseña de forma segura si es necesario

            // Actualizar en Firebase
            databaseReference.child(usuarioId).updateChildren(updates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(EditarUsuario.this, "Usuario actualizado correctamente.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditarUsuario.this, "Error al actualizar usuario.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Primero busque un usuario para editar.", Toast.LENGTH_SHORT).show();
        }
    }
}
