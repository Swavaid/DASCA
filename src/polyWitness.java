import java.math.BigInteger;
import java.security.Key;

/**
 * @author emilio
 * @date 2023-03-08 15:52
 */
public class polyWitness {

    public static void polyWitness(){
        //the witness to evaluation of Z_m(x) at 0
        for (int i=0;i<KeyUpdate.hand_off;i++) {

            BigInteger sub_wit_1 = BigInteger.ZERO;
            BigInteger sub_wit_2 = BigInteger.ZERO;
            for (int m = 0; m < main.threshold-1; m++) {
                sub_wit_1 = sub_wit_1.add(KeyUpdate.KeyServers_handoff[i].Z_m_x[m].toBigInteger().multiply(KeyUpdate.KeyServers_handoff[i].csk.toBigInteger().pow(m)));
            }

            //System.out.println("big_sub_wit_1"+sub_wit_1);

            //System.out.println("big_sub_wit_2"+sub_wit_2);

            BigInteger top = sub_wit_1.subtract(sub_wit_2);

            BigInteger bottom = KeyUpdate.KeyServers_handoff[i].csk.toBigInteger();

            BigInteger hole = top.divide(bottom);
            KeyUpdate.KeyServers_handoff[i].W_z_m_0=main.G_generator.powZn
                    (main.pairing.getZr().newElement(hole).getImmutable());

            main.communication_cost+=KeyUpdate.KeyServers_handoff[i].W_z_m_0.getLengthInBytes();

            //System.out.println(apps_handoff[i].W_z_m_0);
        }
    }
}
