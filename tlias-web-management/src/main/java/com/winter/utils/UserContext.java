package com.winter.utils;

public class UserContext {
    private static final ThreadLocal<Integer> THREAD_LOCAL = new ThreadLocal<>();

    // 存id
    public static void setUserId(Integer id) {
        THREAD_LOCAL.set(id);
    }
    // 取id
    public static Integer getUserId() {
        return THREAD_LOCAL.get();
    }
    // 清理！防止内存泄漏
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
