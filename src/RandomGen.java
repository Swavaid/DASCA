import javax.xml.bind.Element;

/**
 * @author emilio
 * @date 2023-03-07 15:43
 */
public class RandomGen {

    public static Element RandomGen(){
        Element random= (Element) main.pairing.getZr().newRandomElement().getImmutable();

        return random;
    }
}
