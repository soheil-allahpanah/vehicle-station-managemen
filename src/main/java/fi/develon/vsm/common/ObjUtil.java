package fi.develon.vsm.common;

import java.util.Objects;
import java.util.function.Supplier;

public class ObjUtil {

    public static <R, L> L checkIfNotNull(R input, Supplier<L> lazyOutput) {
        if (Objects.isNull(input)) {
            return null;
        }
        return lazyOutput.get();
    }
}
