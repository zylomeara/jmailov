package main.java.utils;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class Try {
    <T> Optional<T> getOptional(Supplier<T> fn) {
        Optional<T> result;

        try {
            result = Optional.of(fn.get());
            return result;
        } catch (Exception e) {
            result = Optional.empty();
            return result;
        }
    }
}
