package app.security;

import java.util.HashSet;
import java.util.Set;

public enum ApplicationUserRole {

	STUDENT(),
	ADMIN("COURSE_READ", "COURSE_WRITE", "STUDENT_READ", "STUDENT_WRITE"),
	ADMINTRAINEE("COURSE_READ", "STUDENT_READ");

	private final Set<ApplicationUserPermission> permissions;

	ApplicationUserRole(String... permissions) {
		this.permissions = new HashSet<>();
		if (permissions == null) {
			return;
		}
		for (String permission : permissions) {
			this.permissions.add(ApplicationUserPermission.valueOf(permission));
		}
	}

	public Set<ApplicationUserPermission> getPermissions() {
		return permissions;
	}

}