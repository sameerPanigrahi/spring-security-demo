package app.auth;

import static app.security.ApplicationUserRole.ADMIN;
import static app.security.ApplicationUserRole.ADMINTRAINEE;
import static app.security.ApplicationUserRole.STUDENT;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository(value = "UserDaoFakeImplementation")
public class UserDaoFakeImplementation implements UserDao {

	private List<User> users = new ArrayList<>();

	@Autowired
	public UserDaoFakeImplementation(PasswordEncoder passwordEncoder) {
		users.add(new User(	"annasmith", passwordEncoder.encode("password"),
							STUDENT.getGrantedAuthorities(), true, true, true,
							true));
		users.add(new User(	"linda", passwordEncoder.encode("adminpassword"),
							ADMIN.getGrantedAuthorities(), true, true, true,
							true));
		users.add(new User(	"tom", passwordEncoder.encode("adminpassword"),
							ADMINTRAINEE.getGrantedAuthorities(), true, true,
							true, true));
	}
	@Override
	public Optional<User> selectUserByUsername(String username) {
		return users.stream()
				.filter(user -> user.getUsername().equals(username))
				.findFirst();
	}

}
