package app.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceFromDb implements UserDetailsService {

	@Autowired
	@Qualifier("UserDaoFakeImplementation")
	private UserDao _userDaoService;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		return _userDaoService.selectUserByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(String
						.format("Username %s not found", username)));
	}

}
