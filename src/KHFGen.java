import it.unisa.dia.gas.jpbc.Element;

/**
 * @author emilio
 * @date 2022-12-08 12:01
 */
public class KHFGen {

    public static Element r=main.pairing.getZr().newRandomElement().getImmutable();

    public static Element KHFGen(String keywords) throws Exception {



        Element[] blind_keywords=new Element[main.n];
        Element[] blind_keywords_compute=new Element[main.n];

        long user1=System.nanoTime();

        Hash.Big_hash(keywords).powZn(r);
        Hash.Big_hash(keywords).powZn(r);

        long user2=System.nanoTime();

        main.computational_cost_KHF+=(user2-user1);

        for (int i=0;i<main.n;i++){

            blind_keywords[i]=Hash.Big_hash(keywords).powZn(r);
            blind_keywords_compute[i]=Hash.Big_hash(keywords).powZn(r);
        }




        main.user.khf=new Element[main.n];

        main.user.valid_khf=new Element[main.threshold];

        int[] index=new int[main.threshold];


        //send w' to apps
        for (int i=0;i<main.n;i++) {
            //user sends w' to APPs
            main.apps[i].received_blind_keywords = blind_keywords[i];
            //System.out.println(main.apps[i].received_blind_keywords);
            //the communication cost of w'
            main.communication_cost_datastore=main.communication_cost_datastore+main.apps[i].received_blind_keywords.getLengthInBytes();

            main.communication_cost+=main.apps[i].received_blind_keywords.getLengthInBytes();
        }

        for (int i=0;i<main.n;i++){
            main.computational_cost_datastore_apps[i]=0;
        }

        //apps computes the sigma_i
        for (int i=0;i<main.n;i++) {



            long apps1=System.nanoTime();


            main.user.khf[i] = blind_keywords_compute[i].powZn(main.apps[i].secret_share);


            long apps2=System.nanoTime();



            main.computational_cost_datastore_apps[i]=apps2-apps1;
            System.out.println("main.computational_cost_datastore_apps[i]::"+main.computational_cost_datastore_apps[i]);

        }


        //user
        long user3=System.nanoTime();

        int validity_num=0;
        for (int i=0;i<main.n;i++){

            //System.out.println("sigma_i"+main.user.khf[i]);

            //System.out.println("w'B(i,0)"+main.apps[i].received_blind_keywords.powZn(main.apps[i].secret_share));


            //user verifies the validity of khf
            if(main.pairing.pairing(main.user.khf[i],main.G_generator).isEqual(main.pairing.pairing(main.apps[i].received_blind_keywords,main.apps[i].public_share))==true){
                System.out.println("SKHF is valid");

                index[validity_num]=i;

                //System.out.println(i);

                main.communication_cost_datastore=main.communication_cost_datastore+main.user.khf[i].getLengthInBytes();
                main.communication_cost+=main.user.khf[i].getLengthInBytes();

                main.user.valid_khf[validity_num]=main.user.khf[i];
                validity_num++;
                System.out.println(validity_num);
                if(validity_num==main.threshold){
                    break;
                }
            }


        }

        //user receives t valid skhfs
        Element r_inverse=r.invert();
        Element KHF=main.zero_point;
        int[] Lan_coefficient=new int[main.threshold];

        for (int k=0;k<main.threshold;k++){
            //user computes Lagrange coefficient (t omega_k)
            Lan_coefficient[k]=1;
            for (int j=0;j<main.threshold&&index[j]!=k;j++){
                Lan_coefficient[k]=Lan_coefficient[k]*(index[j]/(index[j]-k));
            }


            if(main.user.valid_khf[index[k]]!=null && Lan_coefficient!=null){
                //KHF=KHF.add(main.pairing.getZr().newElement(Lan_coefficient[k]).getImmutable().mul(main.user.valid_khf[index[k]]));
                KHF=KHF.add(main.user.valid_khf[index[k]].powZn(main.pairing.getZr().newElement(Lan_coefficient[k]).getImmutable()));

            }

        }

        KHF=KHF.powZn(r_inverse);

        long user4=System.nanoTime();

        main.computational_cost_KHF+=(user4-user3);



        return KHF;

    }


}
