import it.unisa.dia.gas.jpbc.Element;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author emilio
 * @date 2022-12-08 10:41
 */
public class Trapdoor {

    public static Element r=main.pairing.getZr().newRandomElement().getImmutable();

    public static void DataRequest(Element KHF ,String keywords) throws Exception {


        long user5=System.nanoTime();

        if (main.pairing.pairing(KHF,main.G_generator).isEqual(main.pairing.pairing(Hash.Big_hash(keywords),main.KeyServers[0].P_0))==true){
            //computes harden keywords
            System.out.println("KHF is valid");

            String Hash_KHF= String.valueOf(Hash.small_hash(String.valueOf(KHF)));
            //System.out.println("dd"+Hash_KHF);

            SecretKeySpec secretKeySpec=new SecretKeySpec(Hash_KHF.getBytes(StandardCharsets.UTF_8),"AES");
            String hk_w = PRF.encryptAES(keywords);
            //System.out.println("11ll"+hk_w);



            //compute the trapdoor of keywords

            Element td_w=Hash.Big_hash(hk_w).powZn(main.user.user_sk);
            //System.out.println(td_w);
            main.cloudServer.received_trapdoor=Hash.Big_hash(hk_w).powZn(main.user.user_sk);
            //System.out.println(main.cloudServer.received_trapdoor);

            //System.out.println("sq"+main.pairing.pairing(td_w,main.cloudServer.received_Ciphertext[0]));

            main.communication_cost+=td_w.getLengthInBytes();

            System.out.println("user has sent keyword trapdoor to the cloud server");


        }
        long user6=System.nanoTime();
        main.computational_cost_dataRequest_user=main.computational_cost_KHF+(user6-user5);
    }



}
