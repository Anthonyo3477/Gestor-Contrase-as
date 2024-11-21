package adapters;

import android.os.Build;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encrypation {

    // Método para encriptar la contraseña con SHA-256 y sal (salt)
    public static String encriptarContraseña(String contraseña) {
        try {
            // Crear un MessageDigest para el algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Aquí puedes agregar una sal personalizada, por ejemplo, un valor único por usuario
            String salt = "mi_salt_unico"; // Se recomienda usar una sal diferente por usuario en la vida real

            // Concatenar la sal a la contraseña
            String contraseñaConSal = salt + contraseña;

            // Generar el hash de la contraseña + sal
            byte[] hash = digest.digest(contraseñaConSal.getBytes());

            // Codificar el hash en Base64 para almacenar la contraseña de manera segura
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder().encodeToString(hash);
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return contraseña;
    }
}
