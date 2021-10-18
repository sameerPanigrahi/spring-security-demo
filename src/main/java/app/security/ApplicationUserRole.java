package app.security;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

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

	public Set<SimpleGrantedAuthority> getGrantedAuthorities() {

		// Add authorities for fine-grained control
		Set<SimpleGrantedAuthority> grantedAuthorities = permissions
				.stream()
					.map(permission -> new SimpleGrantedAuthority(
							permission.name()))
					.collect(Collectors.toSet());

		// Add roles as authorities for coarse-grained control.
		String role = "ROLE_" + this.name(); // If added via authorities, Roles
												// need to be added with prefix
												// 'ROLE_'
		grantedAuthorities.add(new SimpleGrantedAuthority(role));

		// the final list of authorities has both authorities(permissions) and
		// role
		return grantedAuthorities;
	}
}