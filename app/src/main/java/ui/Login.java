package ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestor_contrasea.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Activities.MainActivity;
import Activities.MenuUsuario;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btnIniciarSesion, btnVolver;
    private EditText txtContraseña, txtCorreo;
    private TextView LabelContraseña, LabelCorreo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingresar_sesion);

        mAuth = FirebaseAuth.getInstance();
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContraseña = findViewById(R.id.txtContraseña);
        LabelCorreo = findViewById(R.id.LabelCorreo);
        LabelContraseña = findViewById(R.id.LabelContraseña);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnVolver = findViewById(R.id.btnVolver);


        btnIniciarSesion.setOnClickListener(view -> {
            String email = txtCorreo.getText().toString();
            String contraseña = txtContraseña.getText().toString();
            signIn(email, contraseña);
        });

        btnVolver.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        });


    }
    private void signIn(String correo, String contraseña) {
        if (correo.isEmpty() || !correo.endsWith("@gmail.com")){
            Toast.makeText(Login.this, "Ingrese un correo de GMAIL válido", Toast.LENGTH_SHORT).show();
            return;
        }
        if (contraseña.isEmpty() || contraseña.length() < 10){
            Toast.makeText(Login.this, "Porfavor ingrese una contraseña valida", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(correo, contraseña).addOnCompleteListener(this, task -> {
           if (task.isSuccessful()) {
               Log.d("AUTH","signInWithEmail:success");
               FirebaseUser user = mAuth.getCurrentUser();
               if( user != null ){
                   String userId = user.getUid();
                   DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios").child(userId);

                   databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           if (dataSnapshot.exists()) {
                               String tipoUsuario = dataSnapshot.child("userType").getValue(String.class);
                               Intent intent = new Intent();

                               if (tipoUsuario != null) {
                                   switch (tipoUsuario) {
                                       case "usuario":
                                           Toast.makeText(Login.this, "Bienvenido Usuario", Toast.LENGTH_SHORT).show();
                                           intent = new Intent(Login.this, MenuUsuario.class);
                                           break;
                                   }
                                   intent.putExtra("userId",userId);
                                   startActivity(intent);
                                   finish();
                               }else {
                                   Toast.makeText(Login.this, "Error al obtener sus credenciales", Toast.LENGTH_SHORT).show();
                               }
                           }else {
                               Toast.makeText(Login.this, "Error al obtener sus credenciales", Toast.LENGTH_SHORT).show();
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {
                           Log.e("Data Base Error"," Errror al obtener dadtos del usuario", databaseError.toException());
                       }
                   });
               }
           }else{
               Log.w("AUTH","signInWithEmail:failure", task.getException());
               Toast.makeText(Login.this, "Credenciales Incorrectas", Toast.LENGTH_SHORT).show();
               updateUI(null);
           }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(Login.this, PerfilUsuario.class);
            startActivity(intent);
        } else {
            Toast.makeText(Login.this, "Inicio de sesión fallido. Por favor, inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
        }
    }
}
