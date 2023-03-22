import it.unisa.dia.gas.jpbc.Element;

/**
 * @author emilio
 * @date 2023-03-07 15:50
 */
public class Blind {

    public static Element[] Blind(String keywords) throws Exception {
        Element r= (Element) RandomGen.RandomGen();
        it.unisa.dia.gas.jpbc.Element[] blind_keywords=new it.unisa.dia.gas.jpbc.Element[main.n];
        it.unisa.dia.gas.jpbc.Element[] blind_keywords_compute=new it.unisa.dia.gas.jpbc.Element[main.n];

        long user1=System.nanoTime();

        Hash.Big_hash(keywords).powZn( r);
        Hash.Big_hash(keywords).powZn( r);

        long user2=System.nanoTime();

        for (int i=0;i<main.n;i++){

            blind_keywords[i]=Hash.Big_hash(keywords).powZn( r);
            blind_keywords_compute[i]=Hash.Big_hash(keywords).powZn( r);
        }


        main.computational_cost_datastore_user+=user2-user1;
        main.computational_cost_dataRequest_user+=user2-user1;

        main.user.khf=new it.unisa.dia.gas.jpbc.Element[main.n];

        main.user.valid_khf=new it.unisa.dia.gas.jpbc.Element[main.threshold];

        return  blind_keywords;
    }
}
