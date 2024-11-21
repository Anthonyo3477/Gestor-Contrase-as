package adapters;

import android.util.Base64;  // Asegúrate de importar android.util.Base64

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encrypation {

    // Clave fija para AES (cifrado reversible)
    private static final String ALGORITHM = "AES";
    private static final String KEY = "MySuperSecretKey"; // Debe ser de 16 caracteres exactos

    // ==========================================
    // MÉTODOS PARA HASH (SHA-256 CON SALT ÚNICO)
    // ==========================================

    /**
     * Encripta una contraseña usando SHA-256 con un salt único.
     * @param contraseña Contraseña a encriptar
     * @return Hash concatenado con el salt en formato "salt:hashBase64"
     */
    public static String encriptarContraseñaSHA(String contraseña) {
        try {
            // Generar un salt único
            String salt = generarSalt();

            // Concatenar el salt con la contraseña
            String contraseñaConSal = salt + contraseña;

            // Crear un hash con SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(contraseñaConSal.getBytes(StandardCharsets.UTF_8));

            // Convertir el hash y el salt a Base64
            String hashBase64 = convertirABase64(hash);
            String saltBase64 = convertirABase64(salt.getBytes(StandardCharsets.UTF_8));

            // Retornar el salt y el hash concatenados para almacenarlos
            return saltBase64 + ":" + hashBase64;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Genera un salt aleatorio.
     * @return Salt único generado
     */
    private static String generarSalt() {
        Random random = new Random();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return new String(salt, StandardCharsets.UTF_8);
    }

    /**
     * Convierte un array de bytes a Base64.
     * @param data Bytes a convertir
     * @return Cadena en Base64
     */
    private static String convertirABase64(byte[] data) {
        // Usamos la implementación de Base64 proporcionada por Android
        return Base64.encodeToString(data, Base64.DEFAULT).trim();
    }

    // ==========================================
    // MÉTODOS PARA CIFRADO (AES REVERSIBLE)
    // ==========================================

    /**
     * Cifra una contraseña usando AES.
     * @param contraseña Contraseña en texto plano
     * @return Contraseña cifrada en Base64
     */
    public static String encriptarContraseñaAES(String contraseña) {
        try {
            Key key = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedBytes = cipher.doFinal(contraseña.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);  // Usamos Base64 de Android
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Descifra una contraseña cifrada con AES.
     * @param contraseñaEncriptada Contraseña cifrada en Base64
     * @return Contraseña en texto plano
     */
    public static String desencriptarContraseñaAES(String contraseñaEncriptada) {
        try {
            Key key = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decodedBytes = Base64.decode(contraseñaEncriptada, Base64.DEFAULT);
            return new String(cipher.doFinal(decodedBytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
