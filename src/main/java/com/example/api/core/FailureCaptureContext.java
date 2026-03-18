package com.example.api.core;

public final class FailureCaptureContext {

    private static final ThreadLocal<FailureSnapshot> LAST_SNAPSHOT = new ThreadLocal<>();

    private FailureCaptureContext() {
    }

    public static void setSnapshot(FailureSnapshot snapshot) {
        LAST_SNAPSHOT.set(snapshot);
    }

    public static FailureSnapshot getSnapshot() {
        return LAST_SNAPSHOT.get();
    }

    public static void clear() {
        LAST_SNAPSHOT.remove();
    }
}
