import it.unisa.dia.gas.jpbc.Element;

/**
 * @author emilio
 * @date 2022-12-12 13:23
 */
public class constructPoly {

    public static Element[] construct_t_polynomial(int [] point_x,Element [] point_y, int order){

        //compute omega
        Element[] coefficient=new Element[order+1];
        //coefficient a_0,a_1...,a_t-1
        for (int i=0;i<order+1;i++){

            coefficient[i]=main.zero;

            int[] Lan_coefficient=new int[order+1];
            for (int k=0;k<order+1;k++) {
                //user computes Lagrange coefficient (t omega_k)
                Lan_coefficient[k] = 1;
                for (int j = 0; j < order+1 && j != k; j++) {
                    Lan_coefficient[k] = Lan_coefficient[k] * (j - point_x[i] / (j - k));
                }

                coefficient[i]=coefficient[i].add(point_y[i].mul(main.pairing.getZr().newElement(Lan_coefficient[k]).getImmutable()));


            }

//            System.out.println("kkkk:"+coefficient.length);
//            System.out.println("mmmm"+order);


        }


        return coefficient;

    }



}
