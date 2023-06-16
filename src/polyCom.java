/**
 * @author emilio
 * @date 2023-03-08 15:34
 */
public class polyCom {

    public static void polyCom(){
        for (int i=0;i<KeyUpdate.hand_off;i++){
            KeyUpdate.KeyServers_handoff[i].C_Z_m_x=main.zero_point;
            for (int m=0;m<main.threshold-1;m++){
                KeyUpdate.KeyServers_handoff[i].C_Z_m_x=KeyUpdate.KeyServers_handoff[i].C_Z_m_x.
                        add(KeyUpdate.KeyServers_handoff[i].cpk[m].powZn(KeyUpdate.KeyServers_handoff[i].Z_m_x[m]));

                main.communication_cost+=KeyUpdate.KeyServers_handoff[i].C_Z_m_x.getLengthInBytes();
            }
            //System.out.println(apps_handoff[i].C_Z_m_x);

        }
    }
}
