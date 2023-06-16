import it.unisa.dia.gas.jpbc.Element;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author emilio
 * @date 2022-12-06 18:22
 */
public class PEKS {

    public static Element r=main.pairing.getZr().newRandomElement().getImmutable();

    public static void Datastore(Element KHF, String keywords) throws Exception {




        //verify the validity of KHF

        long user5=System.nanoTime();



        if (main.pairing.pairing(KHF,main.G_generator).isEqual(main.pairing.pairing(Hash.Big_hash(keywords),main.KeyServers[0].P_0))==true){

            //computes harden keywords
            System.out.println("KHF is valid");

            String Hash_KHF= String.valueOf(Hash.small_hash(String.valueOf(KHF)));
            //System.out.println("dd"+Hash_KHF);

            SecretKeySpec secretKeySpec=new SecretKeySpec(extractByte.extractByte(Hash_KHF.getBytes(StandardCharsets.UTF_8)),"AES");
            PRF.c.init(Cipher.ENCRYPT_MODE,secretKeySpec);
            String hk_w = PRF.encryptAES(keywords);
            //System.out.println("11ll"+hk_w);


            //compute the ciphertext of keywords


            Element random_X=main.pairing.getZr().newRandomElement().getImmutable();

            Element Cipher_Cipher_factor = main.pairing.pairing(Hash.Big_hash(hk_w),main.user.user_pk.powZn(random_X));





            main.cloudServer.received_Ciphertext[0]=main.G_generator.powZn(random_X);
            main.cloudServer.received_Ciphertext[1]=Hash.Big_hash(String.valueOf(Cipher_Cipher_factor));

            main.communication_cost+=main.cloudServer.received_Ciphertext[0].getLengthInBytes()+main.cloudServer.received_Ciphertext[1].getLengthInBytes();

//            main.communication_cost_datastore=main.communication_cost_datastore+
//                    main.cloudServer.received_Ciphertext[0].getLengthInBytes()+main.cloudServer.received_Ciphertext[1].getLengthInBytes();



            System.out.println("user has sent keyword Ciphertext to the cloud server");

        }

        long user6=System.nanoTime();

        main.computational_cost_datastore_user=main.computational_cost_KHF+(user6-user5);









    }





}
