package org.healthafrica.shared.tenant;

/**
 * Thread-local holder for the active tenant identifier on each request.
 */
public final class TenantContextHolder {

    private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();

    private TenantContextHolder() {
    }

    public static void setTenant(String tenant) {
        HOLDER.set(tenant);
    }

    public static String getTenant() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }
}
