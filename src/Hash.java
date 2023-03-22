/**
 * @author emilio
 * @date 2022-08-02 20:05
 */
import it.unisa.dia.gas.jpbc.Element;

import java.security.MessageDigest;

public class Hash {
    //SHA256
    public static byte[] getSHA256(String str) throws Exception{
        MessageDigest messageDigest;

        messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(str.getBytes("UTF-8"));

        return messageDigest.digest();
    }

    private static String byte2Hex(byte[] bytes) throws Exception{
        StringBuffer stringBuffer = new StringBuffer();
        for (int i=0;i<bytes.length;i++){
            String temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    public static Element Big_hash(String a) throws Exception {

        Element Hash_to_Z=main.pairing.getG1().newElement().setFromHash(getSHA256(a), 0, getSHA256(a).length);

        return Hash_to_Z;
    }

    public static Element small_hash(String a) throws Exception {
        Element Hash_to_G=main.pairing.getZr().newElement().setFromHash(getSHA256(a), 0, getSHA256(a).length);

        return Hash_to_G;
    }

}
