package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestor_contrasea.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import modules.User;
import adapters.UsuarioAdapter;

public class MenuUsuario extends AppCompatActivity {

    private Button btnCrearUsuario;
    private RecyclerView recyclerView;
    private UsuarioAdapter usuarioAdapter;
    private List<User> listaUsuarios = new ArrayList<>();

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_usuario);

        // Inicializar elementos de la vista
        btnCrearUsuario = findViewById(R.id.CrearAplicacion);
        recyclerView = findViewById(R.id.recyclerViewUsuarios);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        usuarioAdapter = new UsuarioAdapter(this, listaUsuarios);
        recyclerView.setAdapter(usuarioAdapter);

        // Referencia a Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Aplicaciones");

        // Cargar datos de Firebase
        cargarDatosUsuario();

        // Configurar botón Crear Usuario
        btnCrearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuUsuario.this, CrearUsuario.class);
                startActivity(intent);
            }
        });
    }

    private void cargarDatosUsuario() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaUsuarios.clear(); // Limpia la lista para evitar duplicados
                if (dataSnapshot.exists()) {
                    for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                        User usuario = usuarioSnapshot.getValue(User.class);
                        if (usuario != null) {
                            listaUsuarios.add(usuario);
                            Log.d("MenuUsuario", "Usuario añadido: " + usuario.getNombreUsuario());
                        }
                    }
                    usuarioAdapter.notifyDataSetChanged(); // Notifica al adaptador
                } else {
                    Log.w("MenuUsuario", "No existen datos en el nodo aplicaciones.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("MenuUsuario", "Error al leer el valor.", databaseError.toException());
            }
        });
    }

}
