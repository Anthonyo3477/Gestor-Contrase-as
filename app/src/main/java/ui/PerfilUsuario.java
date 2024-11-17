package ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestor_contrasea.R;
import com.google.firebase.database.DatabaseReference;


public class PerfilUsuario extends AppCompatActivity {

    private Button btnEditar, btnVolver;
    private EditText txtNombreSitio,txtNombreUsuario, txtCorreo, txtContrasena, txtConfirmarContrasena;
    private TextView LabelConfirmarContrasena, LabelContrasena, LabelCorreo, LabelNombreUsuario;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_usuario);

    }
}
