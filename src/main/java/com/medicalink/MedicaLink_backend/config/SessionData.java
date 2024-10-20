package com.medicalink.MedicaLink_backend.config;

public class SessionData {
    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> sessionIdHolder = new ThreadLocal<>();

    public static void setToken(String token) {
        tokenHolder.set(token);
    }

    public static String getToken() {
        return tokenHolder.get();
    }

    public static void clearToken() {
        tokenHolder.remove();
    }

    public static void setSessionId(String sessionId) {
        sessionIdHolder.set(sessionId);
    }

    public static String getSessionId() {
        return sessionIdHolder.get();
    }

    public static void clearSessionId() {
        sessionIdHolder.remove();
    }

    public static void clearAll() {
        clearToken();
        clearSessionId();
    }
}
