package org.example.streamUtils;

import org.example.output.Error;
import org.example.output.Result;
import org.example.output.Success;

import java.util.concurrent.Future;

public final class StreamUtils {

    private StreamUtils() {}

    public static <R> Result liftWithValue(Future<R> future) {
        try {
            return new Success<>(future.get());
        } catch (Exception ex) {
            return new Error(ex.getMessage());
        }
    }
}
