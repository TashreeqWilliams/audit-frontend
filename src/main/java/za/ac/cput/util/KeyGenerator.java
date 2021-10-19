/* KeyGenerator.java Class
 * Entity for Generating Random ID Key
 * Author: Athenkosi Zono (218030185)
 * Date: 4 June 2021
 */
package za.ac.cput.util;
import java.util.UUID;

public class KeyGenerator {
    public static String genratedId(){
        return UUID.randomUUID().toString();
    }
}
