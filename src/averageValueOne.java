/**
 * @author emilio
 * @date 2022-12-27 13:25
 */
public class averageValueOne {

    public static long averageValue(){
        long result=0;
        for (int i=0;i<2*main.threshold-1;i++){
            result+=main.computational_cost_dynamicUpdate[i];
        }
        result=result/(2*main.threshold-1);

        System.out.println("computation cost in dynamicUpdate is "+result);
        return result;
    }
}
