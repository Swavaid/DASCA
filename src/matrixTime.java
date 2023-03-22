import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author emilio
 * @date 2022-12-02 11:42
 */
public class matrixTime {

    public static Element[][] printMatrix(Element[][] a, Element[][] b) {

        if(a==null||a.length==0||(a.length==1&&a[0].length==0)||b==null||b.length==0||(b.length==1&&b[0].length==0)){
            return null;
        }


        int r = a.length;
        int c = b[0].length;

        Element result[][] = new Element[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                result[i][j]= main.zero;
                for (int k = 0; k < b.length; k++) {
                    if(a[i][k]!=null&&b[k][j]!=null)
                        result[i][j] = result[i][j].add(a[i][k].mul(b[k][j]));
                }
            }
        }
        return result;
    }


}
