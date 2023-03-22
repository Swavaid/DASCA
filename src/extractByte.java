import javax.crypto.spec.SecretKeySpec;

/**
 * @author emilio
 * @date 2023-03-02 10:51
 */
public class extractByte {

    public static byte[] extractByte(byte[] arbByte){

        byte[] secret=new byte[32];


        if(arbByte.length>32){
            for (int i=0;i<32;i++){
                secret[i]=arbByte[i];
            }
        }

        return secret;

    }
}
