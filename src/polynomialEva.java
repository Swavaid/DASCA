import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author emilio
 * @date 2022-12-09 12:08
 */
public class polynomialEva {

    public static Element polynomial_evaluation(Element[] coefficient, BigInteger x){

        int order =coefficient.length;
        Element result=main.zero;
        for (int i=0;i<order;i++){
            result=result.add(coefficient[i].mul(x.pow(i)));
        }


        return result;
    }
}
