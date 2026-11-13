package com.winter.utils;

public class UserContext {
    private static final ThreadLocal<Integer> THREAD_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<Integer> ROLE = new ThreadLocal<>();

    // 存id
    public static void setUserId(Integer id) {
        THREAD_LOCAL.set(id);
    }
    // 取id
    public static Integer getUserId() {
        return THREAD_LOCAL.get();
    }
    public static void setRole(Integer role) {
        ROLE.set(role);
    }
    public static Integer getRole() {
        return ROLE.get();
    }

    // 清理！防止内存泄漏
    public static void remove() {
        THREAD_LOCAL.remove();
        ROLE.remove();
    }
}
