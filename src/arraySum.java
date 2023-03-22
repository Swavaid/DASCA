import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author emilio
 * @date 2022-12-02 17:34
 */



//compute n polynomials with same order

public class arraySum {

    public static Element[] sum1(Element[][] a){


        int n=a.length;
        int order=a[0].length;

        Element[] result=new Element[order];

        for (int i=0;i<order;i++){
            result[i]= main.zero;
            for (int j=0;j<n;j++){
                if(a[j][i]!=null)
                    result[i]=result[i].add(a[j][i]);
            }

        }

    return result;

    }

    public static Element[] sum2(Element[] a,Element[] b){



        if(a.length!= b.length){
            return null;
        }

        Element[] result = new Element[a.length];
        for (int i=0;i<a.length;i++){
            result[i]=a[i].add(b[i]);
        }

        return result;

    }
}
