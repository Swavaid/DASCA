import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

/**
 * @author emilio
 * @date 2022-12-01 12:58
 */
public class main {
    //security Parameter
    public static int security_para=128;
    public static int n=30;
    public static int sum_pk_commitment=50;
    public static int threshold =14;
    public static int num_keywords=1;

    public static TypeACurveGenerator pg=new TypeACurveGenerator(256,512);
    public static PairingParameters typeAParams = pg.generate();
    public static Pairing pairing = PairingFactory.getPairing(typeAParams);


    public static Element G_generator = pairing.getG1().newRandomElement().getImmutable();
    public static Element zero = pairing.getZr().newElement(0).getImmutable();
    public static Element one = pairing.getZr().newElement(1).getImmutable();
    public static Element zero_point=pairing.getG1().newElement(0).getImmutable();

    public static APP[] apps=new APP[n];
    public static User user=new User();
    public static CloudServer cloudServer=new CloudServer();

    public static int communication_cost_datastore=0;
    public static long computational_cost_datastore_user=0;
    public static long computational_cost_dataRequest_user=0;
    public static long computational_cost_KHF=0;
    public static long[] computational_cost_datastore_apps=new long[n];
    public static long computational_cost_dataSearch_CS=0;
    public static long[] computational_cost_setup=new long[n];
    public static long[] computational_cost_dynamicUpdate=new long[2*threshold-1];

    public static int communication_cost=0;

    public static int Element_Byte=main.pairing.getZr().newRandomElement().getImmutable().getLengthInBytes();





    public static String[] keywords=new String[num_keywords];


    public static void main(String[] args) throws Exception {
        System.out.println("start");


        Setup.setup();




        //long time1=System.nanoTime();
        for (int i=0;i<num_keywords;i++){
            keywords[i]="happy"+i;
            Element KHF= KHFGen.KHFGen(keywords[i]);
            DataStore.Datastore(KHF,keywords[i]);
            DataRequest.DataRequest(KHF,keywords[i]);
            //DataSearch.Search();
        }
        //long time2=System.nanoTime();
        //computational_cost_datastore_user_n=time2-time1;




//        Element KHF=KHF_generator.generate("happy");
//        DataStore.Datastore(KHF,"happy");
//        //System.out.println("communication_cost_data_store:"+communication_cost_datastore);
//        DataRequest.DataRequest(KHF,"happy");
//        DataSearch.Search();

        UpdateAPP.DynamicUpdate();

        averageValue.averageValue();
        averageValueOne.averageValue();
        System.out.println("computational_cost_datastore_user:"+computational_cost_datastore_user);
        System.out.println("computational_cost_dataRequest_user:"+computational_cost_dataRequest_user);
        System.out.println("computational_cost_KHF:"+computational_cost_KHF);
//        System.out.println(computational_cost_dataSearch_CS);

        System.out.println("communication cost is "+main.communication_cost/(1024));

    }
}
