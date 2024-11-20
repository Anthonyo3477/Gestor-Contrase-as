package adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestor_contrasea.R;
import java.util.List;
import modules.User;
import Activities.EditarUsuario;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private Context context;
    private List<User> usuarios;

    // Constructor del adaptador, recibe contexto y lista de usuarios
    public UsuarioAdapter(Context context, List<User> usuarios) {
        this.context = context;
        this.usuarios = usuarios;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar la vista del diseño de tarjeta (card_usuario.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.card_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        // Obtener el usuario actual según la posición
        User usuario = usuarios.get(position);

        // Asignar valores del modelo User a los campos de la tarjeta, verificando nulos
        holder.nombreWeb.setText(usuario.getNombreApp() != null ? usuario.getNombreApp() : "Sin nombre");
        holder.nombreUsuario.setText(usuario.getNombreUsuario() != null ? usuario.getNombreUsuario() : "Sin usuario");
        holder.correo.setText(usuario.getCorreo() != null ? usuario.getCorreo() : "Sin correo");
        holder.contraseña.setText(usuario.getContraseña() != null ? usuario.getContraseña() : "Sin contraseña");

        // Configurar acción para el botón Editar
        holder.editarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Registro para depuración
                Log.d("UsuarioAdapter", "Editando usuario: " + usuario.getId());

                // Configurar Intent con los datos del usuario
                Intent intent = new Intent(context, EditarUsuario.class);
                intent.putExtra("usuarioId", usuario.getId());
                intent.putExtra("nombreWeb", usuario.getNombreApp() != null ? usuario.getNombreApp() : "");
                intent.putExtra("nombreUsuario", usuario.getNombreUsuario() != null ? usuario.getNombreUsuario() : "");
                intent.putExtra("correo", usuario.getCorreo() != null ? usuario.getCorreo() : "");
                intent.putExtra("contraseña", usuario.getContraseña() != null ? usuario.getContraseña() : "");

                // Iniciar la actividad de edición
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usuarios.size(); // Número total de elementos en la lista
    }

    // Clase interna para manejar las vistas de cada tarjeta
    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView nombreWeb, nombreUsuario, correo, contraseña;
        Button editarButton;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar vistas de la tarjeta
            nombreWeb = itemView.findViewById(R.id.txtNombreWeb);
            nombreUsuario = itemView.findViewById(R.id.txtNombreUsuario);
            correo = itemView.findViewById(R.id.txtCorreo);
            contraseña = itemView.findViewById(R.id.txtContraseña);
            editarButton = itemView.findViewById(R.id.EditarUsuario);
        }
    }
}
