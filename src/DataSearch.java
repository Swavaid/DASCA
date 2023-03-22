import it.unisa.dia.gas.jpbc.Element;

/**
 * @author emilio
 * @date 2022-12-08 11:07
 */
public class DataSearch {

    public static boolean Search() throws Exception {

        long cs1=System.nanoTime();
        System.out.println(cs1);

        Element left_para=Hash.Big_hash(String.valueOf(main.pairing.pairing(main.cloudServer.received_trapdoor,
                main.cloudServer.received_Ciphertext[0])));

        Element right_para=main.cloudServer.received_Ciphertext[1];

        //System.out.println(left_para);


        //System.out.println(right_para);

        if (left_para.isEqual(right_para)==true){
            long cs2=System.nanoTime();
            System.out.println(cs2);
            System.out.println("cloud server has found corresponding data");
            main.computational_cost_dataSearch_CS+=cs2-cs1;
            return true;
        }
        System.out.println("cloud server has not found corresponding data");





        return false;



    }
}
