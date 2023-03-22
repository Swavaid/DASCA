import Jama.Matrix;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;

import javax.crypto.Cipher;
import javax.swing.*;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author emilio
 * @date 2022-07-30 16:49
 */
public class Setup {


    //vector of unknown number
    //1*t
    public static Element[][] ukn_X(BigInteger x){
        Element a=main.pairing.getZr().newElement(x).getImmutable();
        Element[][] ukn=new Element[1][main.threshold];
        for (int i=0;i<main.threshold;i++){
            ukn[0][i]= a.pow(BigInteger.valueOf(i));
        }
        return ukn;
    }
    //2t-1*1
    public static Element[][] ukn_Y(BigInteger y){
        Element a=main.pairing.getZr().newElement(y).getImmutable();
        Element[][] ukn=new Element[2*main.threshold-1][1];
        for (int i=0;i<2*main.threshold-1;i++){
            ukn[i][0]=a.pow(BigInteger.valueOf(i));
        }
        return ukn;
    }

    public static Element[][] generate_Matrix(){
        Element[][] polyMatrix = new Element[main.threshold][2*main.threshold-1];
        for(int j=0;j<main.threshold;j++){
            for (int k=0;k<2*main.threshold-1;k++){
                polyMatrix[j][k]=main.pairing.getZr().newRandomElement().getImmutable();
            }
        }
        return polyMatrix;
    }

//    public static Element[] P_x_generate(Element[][] a_Matrix){
//        Element[] P_x=new Element[main.threshold];
//        for (int i=0;i<main.threshold;i++){
//            P_x[i]=main.G_generator.powZn(a_Matrix[i][0]);
//        }
//        return P_x;
//    }


//    public static Element[] P_y_generate(Element[][] a_Matrix){
//        Element[] P_y=new Element[2*main.threshold-1];
//        for (int i=0;i<2*main.threshold-1;i++){
//            P_y[i]=main.G_generator.powZn(a_Matrix[0][i]);
//        }
//        return P_y;
//    }

//    public static Element[][] reverse_x(Element[][][] a){
//        Element[][] x_arr=new Element[main.n][main.threshold];
//        for (int i=0;i<main.n;i++){
//            for (int j=0;j<main.threshold;j++){
//                x_arr[i][j]=a[i][j][0];
//            }
//        }
//        return x_arr;
//    }

//    public static Element[][] reverse_y(Element[][][] a){
//        Element[][] y_arr=new Element[main.n][2*main.threshold-1];
//        for (int i=0;i<main.n;i++){
//            for (int j=0;j<2*main.threshold-1;j++){
//                y_arr[i][j]=a[i][0][j];
//            }
//        }
//        return y_arr;
//    }





    public static void setup (){

        for (int i=0;i<main.n;i++){
            main.computational_cost_setup[i]=0;
        }

        //Apps
        for (int i=0;i<main.n;i++){




            main.apps[i]=new APP();

            //parameter
            main.apps[i].a_Matrix=generate_Matrix();

            //not null
            //System.out.println(apps[i].a_Matrix[0][0]);

            main.apps[i].coefficient_y=new Element[1][2*main.threshold-1];
            main.apps[i].coefficient_x=new Element[main.threshold][1];

            main.apps[i].P_0=main.G_generator.powZn(main.apps[i].a_Matrix[0][0]);
            main.apps[i].P_x=new Element[main.threshold];
            main.apps[i].P_y=new Element[2*main.threshold-1];

            main.apps[i].coefficient_x_arr = new Element[main.n][main.threshold];
            main.apps[i].coefficient_y_arr =new Element[main.n][2*main.threshold-1];

            main.apps[i].received_poly_x=new Element[main.n][main.threshold][1];
            main.apps[i].received_poly_y=new Element[main.n][1][2*main.threshold-1];
            main.apps[i].full_share=new Element[main.threshold];
            main.apps[i].reduced_share=new Element[2*main.threshold-1];

            main.apps[i].commit_reducedShares=new Element[main.n];

            main.apps[i].witness=new Element[main.n][main.n];
        }

        for (int i=0;i<main.n;i++){



            long setup1=System.nanoTime();


            if(main.apps[i]!=null){

                //start initialization
                for(int j=0;j<main.n;j++){
                    if(main.apps[j]!=null){
                        //where x=j, coefficient of y B_i(j,y) send B_i(j,y) to APP_j

                        main.apps[i].coefficient_y = matrixTime.printMatrix(ukn_X(BigInteger.valueOf(j)),main.apps[i].a_Matrix);
                        main.apps[j].received_poly_y[i]=main.apps[i].coefficient_y;




                        main.communication_cost+=main.Element_Byte*2*main.threshold-1;





                        //where y=j, coefficient of x B_i(x,j) send B_i(x,j) to APP_j
                        main.apps[i].coefficient_x =matrixTime.printMatrix(main.apps[i].a_Matrix,ukn_Y(BigInteger.valueOf(j)));
                        main.apps[j].received_poly_x[i]=main.apps[i].coefficient_x;




                        main.communication_cost+=main.Element_Byte*main.threshold;
                    }
                }


                for (int k=0;k<main.n;k++){
                    for (int j=0;j<main.threshold;j++){
                        main.apps[i].coefficient_x_arr[k][j]=main.apps[i].received_poly_x[k][j][0];
                    }
                }

                for (int k=0;k<main.n;k++){
                    for (int j=0;j<2*main.threshold-1;j++){
                        main.apps[i].coefficient_y_arr[k][j]=main.apps[i].received_poly_y[k][0][j];
                    }
                }



                //each APP_i constructs full share and reduced share
                //B(x,i)'s coefficient
                main.apps[i].reduced_share=arraySum.sum1(main.apps[i].coefficient_x_arr);

                //B(i,y)'s coefficient
                main.apps[i].full_share=arraySum.sum1(main.apps[i].coefficient_y_arr);

                //sk of commitment
                main.apps[i].csk=main.pairing.getZr().newRandomElement().getImmutable();

                //pk of commitment
                main.apps[i].cpk=new Element[main.sum_pk_commitment];
                if(main.apps[i].csk.isEqual(main.zero)!=true){
                    for (int k=0;k<main.sum_pk_commitment;k++){
                        main.apps[i].cpk[k]=main.G_generator.powZn(main.apps[i].csk.pow(BigInteger.valueOf(k)));
                    }
                }

                //each APP_i computes commitment of reduced share B(x,i)
                //Point C_reducedShare = apps[i].cpk[0].multiplication(apps[i].reduced_share[0]);

                if(main.apps[i].cpk!=null) {
                    if(main.apps[i].reduced_share[0]!=null){
                        Element C_reducedShare = main.apps[i].cpk[0].powZn(main.apps[i].reduced_share[0]);

                        for (int k=1;k<main.apps[i].reduced_share.length;k++){
                            if(main.apps[i].cpk[k]!=null&&C_reducedShare!=null)
                                C_reducedShare=C_reducedShare.add(main.apps[i].cpk[k].powZn(main.apps[i].reduced_share[k]));
                        }

                        //accept commitment from others
                        for (int j=0;j<main.n;j++){
                            main.apps[j].commit_reducedShares[i]=C_reducedShare;
                        }
                        //System.out.println("dd");
                    }
                    //System.out.println("jj");
                }

                //each APP_i computes the witness to evaluation
                Element[] witness=new Element[main.n];

                for (int j=0;j<main.n;j++) {
                    BigInteger sub_wit_1 = BigInteger.ZERO;
                    BigInteger sub_wit_2 = BigInteger.ZERO;
                    for (int k = 0; k < main.apps[i].reduced_share.length; k++) {
                        sub_wit_1 = sub_wit_1.add(main.apps[i].reduced_share[k].toBigInteger().multiply(main.apps[i].csk.toBigInteger().pow(k)));
                        sub_wit_2 = sub_wit_2.add(main.apps[i].reduced_share[k].toBigInteger().multiply(BigInteger.valueOf(j).pow(k)));
                    }

                    //System.out.println("big_sub_wit_1"+sub_wit_1);

                    //System.out.println("big_sub_wit_2"+sub_wit_2);

                    BigInteger top = sub_wit_1.subtract(sub_wit_2);
                    //System.out.println("top"+top);

                    BigInteger bottom = main.apps[i].csk.toBigInteger().subtract(BigInteger.valueOf(j));
                    //System.out.println("bottom"+bottom);

                    BigInteger hole = top.divide(bottom);
                    //System.out.println("hole"+hole);
                    witness[j] = main.G_generator.powZn(main.pairing.getZr().newElement(hole).getImmutable());
                    //System.out.println(i+"witness"+witness[j]);
                    //send to APP_j
                    main.apps[j].witness[i][j]=witness[j];
                }

//                for (int j=0;j<main.n;j++){
//
//                    System.out.println(main.apps[j].witness[i][j]);
//
//                }





                //each APP_i sends C_B(x,i) to all apps
                main.apps[i].secret_share=main.apps[i].full_share[0];

                main.communication_cost+=main.apps[i].secret_share.getLengthInBytes();

                //System.out.println(main.apps[i].secret_share);

                if(main.apps[i].secret_share!=null){
                    main.apps[i].public_share=main.G_generator.powZn(main.apps[i].secret_share);
                    //System.out.println("kk");
                }


            }
            //System.out.println("apps"+i+"finished instantiation");

            long setup2=System.nanoTime();

            main.computational_cost_setup[i]+=setup2-setup1;
            //System.out.println("computational cost in setup is:"+main.computational_cost_setup[i]);
        }



        //user's public key and private key
        main.user.user_sk=main.pairing.getZr().newRandomElement().getImmutable();
        main.user.user_pk=main.G_generator.powZn(main.user.user_sk);

        main.cloudServer.received_Ciphertext=new Element[2];


        System.out.println("setup finished");
    }


}
