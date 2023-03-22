import it.unisa.dia.gas.jpbc.Element;

/**
 * @author emilio
 * @date 2023-03-08 14:52
 */
public class randomPoly {

    public static Element[] randomPoly(int degree, Element co_0){

        Element[] co=new Element[degree];
        co[0]=co_0;

        for (int i=1;i<degree;i++){
            co[i]= (Element) RandomGen.RandomGen();
        }
        return co;
    }
}
