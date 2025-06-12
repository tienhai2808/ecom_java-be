package store.ecom.backend.service.user;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.ecom.backend.dto.UserDto;
import store.ecom.backend.exceptions.AlreadyExistsException;
import store.ecom.backend.exceptions.ResourceNotFoundException;
import store.ecom.backend.model.User;
import store.ecom.backend.repository.UserRepository;
import store.ecom.backend.request.CreateUserRequest;
import store.ecom.backend.request.UserUpdateRequest;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  @Override
  public User getUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
  }

  @Override
  public User createUser(CreateUserRequest req) {
    return Optional.of(req).filter(_ -> !userRepository.existsByEmail(req.getEmail())).map(_ -> {
      User newUser = new User();
      newUser.setEmail(req.getEmail());
      newUser.setPassword(req.getPassword());
      newUser.setFirstName(req.getFirstName());
      newUser.setLastName(req.getLastName());
      return userRepository.save(newUser);
    }).orElseThrow(() -> new AlreadyExistsException(req.getEmail() + " đã tồn tại"));
  }

  @Override
  public User updateUser(UserUpdateRequest req, Long id) {
    return userRepository.findById(id).map(existingUser -> {
      existingUser.setFirstName(req.getFirstName());
      existingUser.setLastName(req.getLastName());
      return userRepository.save(existingUser);
    }).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
  }

  @Override
  public void deleteUser(Long id) {
    userRepository.findById(id).ifPresentOrElse(userRepository::delete, () -> {
      throw new ResourceNotFoundException("Không tìm thấy người dùng");
    });
  }

  @Override
  public UserDto convertToDto(User user) {
    return modelMapper.map(user, UserDto.class);
  }
}
