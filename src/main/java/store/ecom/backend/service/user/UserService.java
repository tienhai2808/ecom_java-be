package store.ecom.backend.service.user;

import store.ecom.backend.dto.UserDto;
import store.ecom.backend.model.User;
import store.ecom.backend.request.CreateUserRequest;
import store.ecom.backend.request.UserUpdateRequest;

public interface UserService {
  User getUserById(Long id);

  User createUser(CreateUserRequest req);

  User updateUser(UserUpdateRequest req, Long id);

  void deleteUser(Long id);

  UserDto convertToDto(User user);
}
