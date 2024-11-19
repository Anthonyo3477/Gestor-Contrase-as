package modules;

import com.google.firebase.auth.FirebaseUser;

public class User {

    private String nombreWeb;
    private String nombreUsuario;
    private String correo;
    private String Contraseña;

    public User() {
    }

    public User(FirebaseUser user, String nombreUsuario, String nombreApp) {

    }

    public User(String contraseña, String correo, String nombreUsuario, String nombreWeb) {
        this.Contraseña = contraseña;
        this.correo = correo;
        this.nombreUsuario = nombreUsuario;
        this.nombreWeb = nombreWeb;
    }

    public String getNombreWeb() {
        return nombreWeb;
    }

    public void setNombreWeb(String nombreWeb) {
        this.nombreWeb = nombreWeb;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    @Override
    public String toString() {
        return "User{" +
                "nombreWeb='" + nombreWeb + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", correo='" + correo + '\'' +
                ", Contraseña='" + Contraseña + '\'' +
                '}';
    }
}
