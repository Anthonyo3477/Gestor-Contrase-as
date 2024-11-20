package modules;

public class User {
    private String id; // Identificador único
    private String nombreApp;
    private String nombreUsuario;
    private String correo;
    private String contraseña;
    private String confirmarContraseña; // Nuevo campo

    // Constructor vacío requerido por Firebase
    public User() {
    }

    // Constructor con todos los campos
    public User(String id, String nombreApp, String nombreUsuario, String correo, String contraseña, String confirmarContraseña) {
        this.id = id;
        this.nombreApp = nombreApp;
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contraseña = contraseña;
        this.confirmarContraseña = confirmarContraseña;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConfirmarContraseña() {
        return confirmarContraseña;
    }

    public void setConfirmarContraseña(String confirmarContraseña) {
        this.confirmarContraseña = confirmarContraseña;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
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

    public String getNombreApp() {
        return nombreApp;
    }

    public void setNombreApp(String nombreApp) {
        this.nombreApp = nombreApp;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", nombreApp='" + nombreApp + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", correo='" + correo + '\'' +
                ", contraseña='" + contraseña + '\'' +
                ", confirmarContraseña='" + confirmarContraseña + '\'' +
                '}';
    }
}