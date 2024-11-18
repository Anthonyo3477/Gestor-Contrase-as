package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.gestor_contrasea.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import modules.User;

public class MenuUsuario extends AppCompatActivity {

    private Button btnCrearUsuario, btnEditarUsuario, btnEliminarUsuario;

    private CardView cardViewUsuario;
    private TextView txtNombreUsuario, txtNombreApp, txtNombreCorreo;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_usuario);

        // Inicializa los botones mediante findViewById
        btnCrearUsuario = findViewById(R.id.CrearAplicacion);
        btnEditarUsuario = findViewById(R.id.EditarUsuario);
        btnEliminarUsuario = findViewById(R.id.EliminarUsuario);

        // Inicializar CardView y TextViews
        cardViewUsuario = findViewById(R.id.CardUsuario);
        txtNombreUsuario = findViewById(R.id.txtNombreUsuario);
        txtNombreApp = findViewById(R.id.txtNombreApp);
        txtNombreCorreo = findViewById(R.id.txtNombreCorreo);


        // Obtén una referencia a la base de datos de Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios"); // Reemplaza "usuarios" con el nombre de tu nodo

        // Obtén los datos y rellena la CardView
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                    // Suponiendo que tus datos de usuario se almacenan como un objeto personalizado (por ejemplo, Usuario)
                    User usuario = usuarioSnapshot.getValue(User.class);

                    // Actualiza la CardView con la información de usuario
                    if (usuario != null) {
                        // Aquí puedes actualizar los TextViews dentro del CardView
                        txtNombreUsuario.setText(usuario.getNombreUsuario());
                        txtNombreApp.setText(usuario.getNombreWeb());
                        txtNombreCorreo.setText(usuario.getCorreo());
                    } else {
                        Log.e("MenuUsuario", "usuario es nulo");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Maneja los errores
                Log.w("MenuUsuario", "Error al leer el valor.", databaseError.toException());
            }
        });

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
