import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


public class PRF {

    public static Cipher c;

    static {
        try {
            c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    //AES symmetric algorithm
    public static String encryptAES(String plaintext) throws Exception{
        byte[] bytes=c.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        String ciphertext= Base64.getEncoder().encodeToString(bytes);
        return ciphertext;

    }
}
