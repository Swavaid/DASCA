import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author emilio
 * @date 2022-12-08 13:36
 */
public class KeyUpdate {

    //new KeyServers would join the committee

    public static int old_committee=27;
    public static KeyServer[] KeyServers_old_committee=new KeyServer[old_committee];

    public static int new_join=1;
    public static KeyServer[] KeyServers_new_join=new KeyServer[new_join];

    public static int n_1=old_committee+new_join;
    public static KeyServer[] KeyServers_new=new KeyServer[n_1];

    public static int leave_KeyServer=main.n-old_committee;

    public static int hand_off=2*main.threshold-1;
    public static KeyServer[] KeyServers_handoff=new KeyServer[hand_off];

    public static int[] index=new int[old_committee];



    //select 2t-1 KeyServers from n_1 to join handoff
    //at least t KeyServers are come from old_committee

    public static KeyServer[] SelectKeyServersHandoff(){
        //select 2t-1 KeyServers for handoff from KeyServers_new
        int num_in_old=0;
        for (int i=0;i<hand_off;i++){
            Random random = new Random();
            int r=random.nextInt(n_1);
            //random number from 0 to n_1-1
            KeyServers_handoff[i]=KeyServers_new[r];
            if(r<old_committee){
                num_in_old++;
            }

        }
        if(num_in_old>=main.threshold){
            return KeyServers_handoff;
        }
        else {
            SelectKeyServersHandoff();
        }
        return null;
    }



    public static void DynamicUpdate(){

        for (int i=0;i<hand_off;i++){
            main.computational_cost_dynamicUpdate[i]=0;
        }

        //index of KeyServers who leaved
        int[] leave_index=new int[]{3,5,8};
        int a=0;
        int j=0;
        int k=leave_index[a];
        int[] index=new int[old_committee];


        //construct old_committee
        for (int i=0;i<main.n;i++){
            if(i!=k){
                KeyServers_old_committee[j]=main.KeyServers[i];
                index[j]=i;
                //System.out.println(KeyServers_old_committee[j].secret_share);
                j++;
            }
            else{
                if (a<leave_index.length-1){
                    a++;
                    k=leave_index[a];
                }
            }
        }
        for (int i=0;i<old_committee;i++){
            KeyServers_new[i]=KeyServers_old_committee[i];
        }

        //initialize new KeyServers
        for (int i=0;i<new_join;i++){
            KeyServers_new_join[i]=new KeyServer();
            KeyServers_new[i+old_committee]=KeyServers_new_join[i];
            KeyServers_new_join[i].csk=main.pairing.getZr().newRandomElement().getImmutable();
            KeyServers_new_join[i].cpk=new Element[main.sum_pk_commitment];
            KeyServers_new_join[i].commit_reducedShares=main.KeyServers[i].commit_reducedShares;
            for (int m=0;m<main.sum_pk_commitment;m++){
                KeyServers_new_join[i].cpk[m]=main.G_generator.powZn(main.KeyServers[i].csk.pow(BigInteger.valueOf(m)));
            }
            if (KeyServers_new_join[i]!=null){
                //System.out.println("mm");
            }
            //System.out.println(KeyServers_new_join[i].csk);
        }
//        for (int l=0;l<n_1;l++){
//            if(KeyServers_new[l]!=null){
//                System.out.println("hh");
//            }
//        }
        SelectKeyServersHandoff();
//        for (int i=0;i<hand_off;i++){
//           if(KeyServers_handoff[i]!=null){
//               System.out.println("d");
//           }
//           else {
//               System.out.println("h");
//           }
//        }
        //KeyServers in hand of are divided into old and new
        for (int m=0;m<hand_off;m++){
            KeyServers_handoff[m].witness_for_update=new Element[old_committee];
            KeyServers_handoff[m].reduced_share_update_y=new Element[old_committee];
            KeyServers_handoff[m].reduced_share_update_x=new int[old_committee];

        }

        for (int i=0;i<old_committee;i++){
            //System.out.println(index[i]);
            if(KeyServers_old_committee[i].full_share!=null){
                for (int m=0;m<hand_off;m++){

                    long time1=System.nanoTime();

                    //compute B(i_k,m) to KeyServers_m in handoff
                    Element B_i_m=polynomialEva.polynomial_evaluation(main.KeyServers[index[i]].full_share, BigInteger.valueOf(m));
                    KeyServers_handoff[m].reduced_share_update_y[i]=B_i_m;
                    KeyServers_handoff[m].reduced_share_update_x[i]=index[i];
                    //extracts W_B(i_k,m) to KeyServers_m in handoff with main.KeyServer[m].cpk
                    //System.out.println(i+"l"+m+"s"+KeyServers_handoff[m].reduced_share_update[i]);

                    Element witness_update=main.KeyServers[m].witness[index[i]][m];
                    KeyServers_handoff[m].witness_for_update[i]=witness_update;
                   // System.out.println(KeyServers_handoff[m].witness_for_update[i]);

                    main.communication_cost+=B_i_m.getLengthInBytes()+witness_update.getLengthInBytes();

                    long time2=System.nanoTime();

                    main.computational_cost_dynamicUpdate[m]+=time2-time1;
                }
            }

        }
        //KeyServers_handoff verifies the validity of above
        for (int m=0;m<hand_off;m++){
            long time3=System.nanoTime();
            for (int i=0;i<old_committee;i++){

                Element left=main.pairing.pairing(KeyServers_handoff[m].commit_reducedShares[m],main.G_generator);
                //System.out.println("left"+left);

                Element right_1_one=KeyServers_handoff[m].witness_for_update[i];



                Element right_1_two=main.G_generator.
                        powZn(main.pairing.getZr().newElement(main.KeyServers[index[i]].csk.toBigInteger().subtract(BigInteger.valueOf(index[i]))).getImmutable());


                Element right_1=main.pairing.pairing(right_1_one,right_1_two);

                //System.out.println("right_1"+right_1);



                Element right_2_one=main.G_generator;
                Element right_2_two= main.G_generator.powZn(KeyServers_handoff[m].reduced_share_update_y[i]);

                Element right_2=main.pairing.pairing(right_2_one,right_2_two);

                Element right=right_1.mul(right_2);

                //System.out.println();

                //System.out.println("right_2"+right_2);
            }

            long time4=System.nanoTime();
            main.computational_cost_dynamicUpdate[m]+=time4-time3;
        }

        //reduced share generation
        //construct a t-1 polynomial with reduced_share_update
        for (int m=0;m<hand_off;m++){
            long time5=System.nanoTime();
            if (KeyServers_handoff[m].reduced_share_update_y.length<main.threshold){
                System.out.println("error");
            }
            else{

                //construct polynomial with reduced_share_update
//                for (int fg=0;fg<KeyServers_handoff[m].reduced_share_update_x.length;fg++){
//                    System.out.println("index"+KeyServers_handoff[m].reduced_share_update_x[fg]);
//                    System.out.println("y:"+KeyServers_handoff[m].reduced_share_update_y[fg]);
//                }

                KeyServers_handoff[m].reduced_Share_reconstruct= constructPoly.
                        construct_t_polynomial(KeyServers_handoff[m].reduced_share_update_x,KeyServers_handoff[m].reduced_share_update_y,main.threshold-1);
            }

            long time6=System.nanoTime();

            main.computational_cost_dynamicUpdate[m]+=time6-time5;
        }


        //refresh
        //randomly select 2t-2 number

        for (int i=0;i<hand_off;i++) {


            KeyServers_handoff[i].sub_P_x_for_update = new Element[hand_off];
            KeyServers_handoff[i].new_reduced_share = new Element[main.threshold];
            KeyServers_handoff[i].received_P_x_for_update=new Element[hand_off];
            KeyServers_handoff[i].s_m=main.zero;
            KeyServers_handoff[i].s_m_P=main.zero_point;
            KeyServers_handoff[i].R_x_for_update=new Element[main.threshold];
            KeyServers_handoff[i].Z_m_x=new Element[main.threshold-1];
            KeyServers_handoff[i].W_z_m_0=main.zero_point;
        }

        for (int i=0;i<hand_off;i++){

            long time7=System.nanoTime();

            //P_m(x) with P_m(0)=0
            KeyServers_handoff[i].sub_P_x_for_update[0]=main.zero;

            for (int i_1=1;i_1<2*main.threshold-1;i_1++){
                KeyServers_handoff[i].sub_P_x_for_update[i_1]=
                        main.pairing.getZr().newRandomElement().getImmutable();
            }
            //System.out.println(KeyServers_handoff[i].reduced_Share_reconstruct.length);

            //sends P_m(m') to others
            for (int m=0;m<hand_off;m++){
                KeyServers_handoff[m].received_P_x_for_update[i]=
                        polynomialEva.polynomial_evaluation(KeyServers_handoff[i].sub_P_x_for_update,BigInteger.valueOf(m));

                main.communication_cost+=KeyServers_handoff[m].received_P_x_for_update[i].getLengthInBytes();
            }

            long time8=System.nanoTime();
            main.computational_cost_dynamicUpdate[i]+=time8-time7;

        }

        //compute s_m
        for (int i=0;i<hand_off;i++){
            long time9=System.nanoTime();
            //System.out.println(KeyServers_handoff[i].received_P_x_for_update.length);
            for (int m=0;m<hand_off;m++){
                KeyServers_handoff[i].s_m=KeyServers_handoff[i].s_m.
                        add(KeyServers_handoff[i].received_P_x_for_update[m]);
                KeyServers_handoff[i].s_m_P=main.G_generator.powZn(KeyServers_handoff[i].s_m);

                main.communication_cost+=KeyServers_handoff[i].s_m_P.getLengthInBytes();
            }

            //System.out.println("ooo:"+KeyServers_handoff[i].s_m);
            long time10=System.nanoTime();
            main.computational_cost_dynamicUpdate[i]+=time10-time9;
        }

        //compute R_m(x) where R_m(0)=s_m;
        for (int i=0;i<hand_off;i++){
            long time11=System.nanoTime();

            KeyServers_handoff[i].R_x_for_update[0]=KeyServers_handoff[i].s_m;
            for (int m=1;m<main.threshold;m++){
                KeyServers_handoff[i].R_x_for_update[m]=
                        main.pairing.getZr().newRandomElement().getImmutable();
            }
            long time12=System.nanoTime();
            main.computational_cost_dynamicUpdate[i]+=time12-time11;

        }

        //B'(x,m) = B(x,m)+Rm(x)

        for (int i=0;i<hand_off;i++){
            long time13=System.nanoTime();
            KeyServers_handoff[i].new_reduced_share=arraySum.sum2(KeyServers_handoff[i].reduced_Share_reconstruct,
                    KeyServers_handoff[i].R_x_for_update);

//            for (int m=0;m<main.threshold;m++){
//                System.out.println("vvv:"+KeyServers_handoff[i].new_reduced_share[m]);
//            }

            long time14=System.nanoTime();
            main.computational_cost_dynamicUpdate[i]+=time14-time13;
        }

        //Z_m(x)
        for (int i=0;i<hand_off;i++){
            for (int m=0;m<main.threshold-1;m++){
                KeyServers_handoff[i].Z_m_x[m]=KeyServers_handoff[i].R_x_for_update[m+1];
                //System.out.println("null:"+KeyServers_handoff[i].Z_m_x[m]);
            }
        }
        for (int i=0;i<hand_off;i++){
            for (int m=0;m<main.sum_pk_commitment;m++){
                //System.out.println("ppppppp:"+KeyServers_handoff[i].cpk[m]);
            }
        }

        //computes the commitment of Z_m(x)
        for (int i=0;i<hand_off;i++){
            long time15=System.nanoTime();
            KeyServers_handoff[i].C_Z_m_x=main.zero_point;
            for (int m=0;m<main.threshold-1;m++){
                KeyServers_handoff[i].C_Z_m_x=KeyServers_handoff[i].C_Z_m_x.
                        add(KeyServers_handoff[i].cpk[m].powZn(KeyServers_handoff[i].Z_m_x[m]));

                main.communication_cost+=KeyServers_handoff[i].C_Z_m_x.getLengthInBytes();
            }
            //System.out.println(KeyServers_handoff[i].C_Z_m_x);
            long time16=System.nanoTime();
            main.computational_cost_dynamicUpdate[i]+=time16-time15;
        }


        //computes the commitment of B'(x,m)
        for (int i=0;i<hand_off;i++){
            long time17=System.nanoTime();
            KeyServers_handoff[i].C_new_B_x_m=main.zero_point;
            for (int m=0;m<main.threshold;m++){
                KeyServers_handoff[i].C_new_B_x_m=KeyServers_handoff[i].C_new_B_x_m.
                        add(KeyServers_handoff[i].cpk[m].powZn(KeyServers_handoff[i].new_reduced_share[m]));

                main.communication_cost+=KeyServers_handoff[i].C_new_B_x_m.getLengthInBytes();
            }
            //System.out.println(KeyServers_handoff[i].C_new_B_x_m);

            long time18=System.nanoTime();

            main.computational_cost_dynamicUpdate[i]+=time18-time17;
        }

        //the witness to evaluation of Z_m(x) at 0
        for (int i=0;i<hand_off;i++) {

            long time19=System.nanoTime();
            BigInteger sub_wit_1 = BigInteger.ZERO;
            BigInteger sub_wit_2 = BigInteger.ZERO;
            for (int m = 0; m < main.threshold-1; m++) {
                sub_wit_1 = sub_wit_1.add(KeyServers_handoff[i].Z_m_x[m].toBigInteger().multiply(KeyServers_handoff[i].csk.toBigInteger().pow(m)));
            }

            //System.out.println("big_sub_wit_1"+sub_wit_1);

            //System.out.println("big_sub_wit_2"+sub_wit_2);

            BigInteger top = sub_wit_1.subtract(sub_wit_2);

            BigInteger bottom = KeyServers_handoff[i].csk.toBigInteger();

            BigInteger hole = top.divide(bottom);
            KeyServers_handoff[i].W_z_m_0=main.G_generator.powZn
                    (main.pairing.getZr().newElement(hole).getImmutable());

            main.communication_cost+=KeyServers_handoff[i].W_z_m_0.getLengthInBytes();

            //System.out.println(KeyServers_handoff[i].W_z_m_0);
            long time20=System.nanoTime();

            main.computational_cost_dynamicUpdate[i]+=time20-time19;
        }



        //check 1
        for (int i=0;i<hand_off;i++){
            Element left_1 = KeyServers_handoff[i].C_new_B_x_m;
            Element right_1 = KeyServers_handoff[i].commit_reducedShares[i];
            Element right_2 = KeyServers_handoff[i].C_Z_m_x;
            Element right_3 = KeyServers_handoff[i].s_m_P;
        }

        //check 2
        int[] lemda=new int[hand_off];
        for (int i=0;i<hand_off;i++){

            long time21=System.nanoTime();

            lemda[i]=1;
            for (int m=0;m<2*main.threshold-1&&m!=i;m++){
                lemda[i]=lemda[i]*(m/(m-i));
            }

            long time22=System.nanoTime();

            main.computational_cost_dynamicUpdate[i]+=time22-time21;
        }

        Element result_test=main.zero_point;

        for (int i=0;i<hand_off;i++){
            long time23=System.nanoTime();
            Element test=KeyServers_handoff[i].s_m_P.powZn(main.pairing.getZr().newElement(lemda[i]).getImmutable());
            result_test=result_test.add(test);

            long time24=System.nanoTime();

            main.computational_cost_dynamicUpdate[i]+=time24-time23;
        }

        if(result_test.isEqual(main.zero_point)){
            //store
        }


        //compute W_B'(j,m)

        for (int i=0;i<n_1;i++){
            KeyServers_new[i].final_witness_reduced_share=new Element[hand_off];
            KeyServers_new[i].final_sub_reduced_share=new Element[hand_off];
            KeyServers_new[i].final_x=new int[hand_off];
            KeyServers_new[i].new_full_share=new Element[2*main.threshold-1];

        }


        for (int i=0;i<hand_off;i++) {

            long time25=System.nanoTime();
            Element[] witness_new=new Element[n_1];
            for (int i_1 = 0; i_1<n_1; i_1++) {
                BigInteger sub_wit_1 = BigInteger.ZERO;
                BigInteger sub_wit_2 = BigInteger.ZERO;
                for (int m = 0; m < main.threshold; m++) {
                    sub_wit_1 = sub_wit_1.add(KeyServers_handoff[i].new_reduced_share[m].toBigInteger().
                            multiply(KeyServers_handoff[i].csk.toBigInteger().pow(m)));
                    sub_wit_2 = sub_wit_2.add(KeyServers_handoff[i].new_reduced_share[m].toBigInteger().
                            multiply(BigInteger.valueOf(i).pow(m)));
                }



                BigInteger top = sub_wit_1.subtract(sub_wit_2);
                //System.out.println("top"+top);

                BigInteger bottom = main.KeyServers[i].csk.toBigInteger().subtract(BigInteger.valueOf(j));
                //System.out.println("bottom"+bottom);

                BigInteger hole = top.divide(bottom);
                //System.out.println("hole"+hole);
                witness_new[i_1] = main.G_generator.powZn(main.pairing.getZr().newElement(hole).getImmutable());
                main.communication_cost+=witness_new[i_1].getLengthInBytes();
                KeyServers_new[i_1].final_witness_reduced_share[i]=witness_new[i_1];
            }

            long time26=System.nanoTime();
            main.computational_cost_dynamicUpdate[i]+=time26-time25;

        }

        //sends B'(j,m) and W_B'(j,m) to all KeyServers in e epoch
        for (int i=0;i<hand_off;i++){
            for (int m=0;m<n_1;m++){
                //compute B'(j,m)
                KeyServers_new[m].final_sub_reduced_share[i]=polynomialEva.polynomial_evaluation
                        (KeyServers_handoff[i].new_reduced_share, BigInteger.valueOf(m));
                KeyServers_new[m].final_x[i]=i;
            }
        }

        for (int i=0;i<n_1;i++){
            KeyServers_new[i].new_full_share=
                    constructPoly.construct_t_polynomial(KeyServers_new[i].final_x,
                    KeyServers_new[i].final_sub_reduced_share,2*main.threshold-2);
            main.communication_cost+=KeyServers_new[i].new_full_share.length*main.Element_Byte;

        }

        System.out.println("update finished");

    }
}
