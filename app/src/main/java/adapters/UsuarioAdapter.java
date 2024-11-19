package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestor_contrasea.R;
import java.util.List;
import modules.User;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.ViewHolder> {

    private Context context;
    private List<User> listaUsuarios;

    // Constructor para inicializar el contexto y la lista de usuarios
    public UsuarioAdapter(Context context, List<User> listaUsuarios) {
        this.context = context;
        this.listaUsuarios = listaUsuarios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño de cada elemento (card_usuario.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.card_usuario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtener el usuario de la posición actual
        User usuario = listaUsuarios.get(position);

        // Asignar datos a las vistas del ViewHolder
        holder.txtNombreUsuario.setText(usuario.getNombreUsuario());
        holder.txtNombreApp.setText(usuario.getNombreWeb());
        holder.txtCorreo.setText(usuario.getCorreo());
        holder.txtContraseña.setText(usuario.getContraseña());
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size(); // Devuelve el tamaño de la lista de usuarios
    }

    // Clase ViewHolder que representa cada elemento visual
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreApp, txtNombreUsuario, txtCorreo, txtContraseña;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Inicializar las vistas del diseño card_usuario.xml
            txtNombreApp = itemView.findViewById(R.id.txtNombreApp);
            txtNombreUsuario = itemView.findViewById(R.id.txtNombreUsuario);
            txtCorreo = itemView.findViewById(R.id.txtNombreCorreo);
            txtContraseña = itemView.findViewById(R.id.txtContraseña);
        }
    }
}
