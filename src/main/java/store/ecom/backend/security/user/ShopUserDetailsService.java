package store.ecom.backend.security.user;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.ecom.backend.model.User;
import store.ecom.backend.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ShopUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = Optional.ofNullable(userRepository.findByEmail(email))
        .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng"));
    return ShopUserDetails.buildUserDetails(user);
  }
}
