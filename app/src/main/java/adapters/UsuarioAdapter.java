package adapters;

import android.app.AlertDialog;
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
import com.google.firebase.database.FirebaseDatabase;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private Context context;
    private List<User> usuarios;

    // Constructor del adaptador
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

        // Asignar valores del modelo User a los campos de la tarjeta
        holder.nombreWeb.setText(usuario.getNombreApp() != null ? usuario.getNombreApp() : "Sin nombre");
        holder.nombreUsuario.setText(usuario.getNombreUsuario() != null ? usuario.getNombreUsuario() : "Sin usuario");
        holder.correo.setText(usuario.getCorreo() != null ? usuario.getCorreo() : "Sin correo");
        holder.contraseña.setText(usuario.getContraseña() != null ? usuario.getContraseña() : "Sin contraseña");

        // Configurar acción para el botón Editar
        holder.editarButton.setOnClickListener(v -> {
            Log.d("UsuarioAdapter", "Editando usuario: " + usuario.getAppId());

            // Configurar Intent con los datos del usuario
            Intent intent = new Intent(context, EditarUsuario.class);
            intent.putExtra("appId", usuario.getAppId());
            intent.putExtra("nombreApp", usuario.getNombreApp() != null ? usuario.getNombreApp() : "");
            intent.putExtra("nombreUsuario", usuario.getNombreUsuario() != null ? usuario.getNombreUsuario() : "");
            intent.putExtra("correo", usuario.getCorreo() != null ? usuario.getCorreo() : "");
            intent.putExtra("contraseña", usuario.getContraseña() != null ? usuario.getContraseña() : "");

            // Iniciar la actividad de edición
            context.startActivity(intent);
        });

        // Configurar acción para el botón Eliminar
        holder.eliminarButton.setOnClickListener(v -> {
            // Mostrar un diálogo de confirmación
            new AlertDialog.Builder(context)
                    .setTitle("Eliminar Usuario")
                    .setMessage("¿Estás seguro de que deseas eliminar este usuario?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        Log.d("UsuarioAdapter", "Eliminando usuario: " + usuario.getAppId());

                        // Lógica para eliminar de la base de datos aquí
                        eliminarUsuario(position, usuario.getAppId());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    // Método para eliminar un usuario
    private void eliminarUsuario(int position, String appId) {
        if (appId == null || appId.isEmpty()) {
            Log.e("UsuarioAdapter", "appId es nulo o vacío. No se puede eliminar el usuario.");
            return;  // No realizar ninguna acción si el appId es inválido
        }

        // Verificar que la posición esté dentro del rango válido
        if (position < 0 || position >= usuarios.size()) {
            Log.e("UsuarioAdapter", "Posición fuera de los límites: " + position);
            return;  // No intentar eliminar si la posición es inválida
        }

        // Usar FirebaseDatabase para eliminar el usuario de la base de datos
        FirebaseDatabase.getInstance().getReference("Aplicaciones").child(appId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Si la eliminación fue exitosa en Firebase, eliminar el usuario de la lista local
                        // Primero verificamos que la posición sigue siendo válida
                        if (position >= 0 && position < usuarios.size()) {
                            usuarios.remove(position);  // Eliminar de la lista local
                            // Notificar al RecyclerView que la vista ha sido eliminada
                            notifyItemRemoved(position); // Notificar al RecyclerView que el item ha sido eliminado
                        } else {
                            Log.e("UsuarioAdapter", "Posición inválida después de eliminar en Firebase: " + position);
                        }

                        Log.d("UsuarioAdapter", "Usuario eliminado con éxito de la base de datos.");
                    } else {
                        // Si hubo un error al eliminar en Firebase, mostrar un mensaje
                        Log.e("UsuarioAdapter", "Error al eliminar usuario de la base de datos.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("UsuarioAdapter", "Error al eliminar usuario de Firebase: " + e.getMessage());
                });
    }


    // Clase interna para manejar las vistas de cada tarjeta
    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView nombreWeb, nombreUsuario, correo, contraseña;
        Button editarButton, eliminarButton; // Botón eliminar agregado

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreWeb = itemView.findViewById(R.id.txtNombreWeb);
            nombreUsuario = itemView.findViewById(R.id.txtNombreUsuario);
            correo = itemView.findViewById(R.id.txtCorreo);
            contraseña = itemView.findViewById(R.id.txtContraseña);
            editarButton = itemView.findViewById(R.id.EditarUsuario);
            eliminarButton = itemView.findViewById(R.id.EliminarUsuario); // Referencia al botón Eliminar
        }
    }
}
