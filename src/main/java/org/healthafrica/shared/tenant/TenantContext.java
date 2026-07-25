package org.healthafrica.shared.tenant;

/**
 * @deprecated Use {@link TenantContextHolder} instead.
 */
@Deprecated
public final class TenantContext {

    private TenantContext() {
    }

    public static void setTenant(String tenant) {
        TenantContextHolder.setTenant(tenant);
    }

    public static String getTenant() {
        return TenantContextHolder.getTenant();
    }

    public static void clear() {
        TenantContextHolder.clear();
    }
}
