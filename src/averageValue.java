/**
 * @author emilio
 * @date 2022-12-26 11:41
 */
public class averageValue {

    public static long averageValue(){
        long result=0;
        for (int i=0;i<main.n;i++){
            result+=main.computational_cost_setup[i];
        }
        result=result/main.n;

        System.out.println("computation cost in Setup is "+result);
        return result;
    }
}
