import it.unisa.dia.gas.jpbc.Element;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author emilio
 * @date 2023-03-07 15:39
 */
public class HFGen {

    public static void HFGen(Element KHF, String keywords) throws Exception {
        String Hash_KHF= String.valueOf(Hash.small_hash(String.valueOf(KHF)));
        //System.out.println("dd"+Hash_KHF);

        SecretKeySpec secretKeySpec=new SecretKeySpec(extractByte.extractByte(Hash_KHF.getBytes(StandardCharsets.UTF_8)),"AES");
        PRF.c.init(Cipher.ENCRYPT_MODE,secretKeySpec);
        String hk_w = PRF.encryptAES(keywords);
    }
}
