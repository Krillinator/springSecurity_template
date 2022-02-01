package com.krillinator.springSecurityIntro.Security;

public enum ApplicationUserPermission {
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    COURSE_READ("course:read"),
    COURSE_WRITE("course:write");

    // INIT
    private final String permission;

    // CONSTRUCTOR
    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    // GETTER
    public String getPermission() {
        return permission;
    }
}
