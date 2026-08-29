package mainpackage;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {

    public static String generateSalt() {

        byte[] salt = new byte[16];

        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String password, String salt)
            throws Exception {

        byte[] saltBytes =
                Base64.getDecoder().decode(salt);

        PBEKeySpec spec =
                new PBEKeySpec(
                        password.toCharArray(),
                        saltBytes,
                        65536,
                        256
                );

        SecretKeyFactory factory =
                SecretKeyFactory.getInstance(
                        "PBKDF2WithHmacSHA256"
                );

        byte[] hash =
                factory.generateSecret(spec).getEncoded();

        return Base64.getEncoder().encodeToString(hash);
    }

    public static boolean checkPassword(
            String password,
            String storedHash,
            String storedSalt)
            throws Exception {

        String newHash =
                hashPassword(password, storedSalt);

        return newHash.equals(storedHash);
    }
}