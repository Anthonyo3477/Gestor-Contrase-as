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

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private Context context;
    private List<User> usuarios;

    public UsuarioAdapter(Context context, List<User> usuarios) {
        this.context = context;
        this.usuarios = usuarios;
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        User usuario = usuarios.get(position);

        // Asignar valores del modelo User a los campos de la tarjeta
        holder.nombreWeb.setText(usuario.getNombreWeb());
        holder.nombreUsuario.setText(usuario.getNombreUsuario());
        holder.correo.setText(usuario.getCorreo());
        holder.contraseña.setText(usuario.getContraseña());
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView nombreWeb, nombreUsuario, correo, contraseña;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreWeb = itemView.findViewById(R.id.txtNombreWeb);
            nombreUsuario = itemView.findViewById(R.id.txtNombreUsuario);
            correo = itemView.findViewById(R.id.txtCorreo);
            contraseña = itemView.findViewById(R.id.txtContraseña);
        }
    }
}
