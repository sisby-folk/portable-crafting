package folk.sisby.portable_crafting_standalone.network;

import java.lang.annotation.*;

/**
 * Use this to annotate server and client network requests so that they can communicate as a pair.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Id {
    String value();
}
