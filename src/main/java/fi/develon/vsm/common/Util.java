package fi.develon.vsm.common;

import java.util.Objects;
import java.util.function.Supplier;

public class Util {
    public static <R, L> L checkIfNull(R input, Supplier<L> lazyOutput) {
        if (Objects.isNull(input)) {
            return null;
        }
        return lazyOutput.get();
    }
}
