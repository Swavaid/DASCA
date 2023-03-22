/**
 * @author emilio
 * @date 2023-03-08 15:34
 */
public class polyCom {

    public static void polyCom(){
        for (int i=0;i<UpdateAPP.hand_off;i++){
            UpdateAPP.apps_handoff[i].C_Z_m_x=main.zero_point;
            for (int m=0;m<main.threshold-1;m++){
                UpdateAPP.apps_handoff[i].C_Z_m_x=UpdateAPP.apps_handoff[i].C_Z_m_x.
                        add(UpdateAPP.apps_handoff[i].cpk[m].powZn(UpdateAPP.apps_handoff[i].Z_m_x[m]));

                main.communication_cost+=UpdateAPP.apps_handoff[i].C_Z_m_x.getLengthInBytes();
            }
            //System.out.println(apps_handoff[i].C_Z_m_x);

        }
    }
}
