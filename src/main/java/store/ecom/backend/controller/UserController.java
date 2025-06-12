package store.ecom.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.ecom.backend.dto.UserDto;
import store.ecom.backend.exceptions.AlreadyExistsException;
import store.ecom.backend.exceptions.ResourceNotFoundException;
import store.ecom.backend.model.User;
import store.ecom.backend.request.CreateUserRequest;
import store.ecom.backend.request.UserUpdateRequest;
import store.ecom.backend.response.ApiResponse;
import store.ecom.backend.service.user.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
  private final UserService userService;

  @GetMapping("/{id}/user")
  public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
    try {
      User user = userService.getUserById(id);
      UserDto convertedUserDto = userService.convertToDto(user);
      return ResponseEntity.ok(new ApiResponse("Lấy người dùng thành công", convertedUserDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest req) {
    try {
      User newUser = userService.createUser(req);
      UserDto convertedUserDto = userService.convertToDto(newUser);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(new ApiResponse("Tạo người dùng thành công", convertedUserDto));
    } catch (AlreadyExistsException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/{id}/update")
  public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest req) {
    try {
      User updatedUser = userService.updateUser(req, id);
      UserDto convertedUserDto = userService.convertToDto(updatedUser);
      return ResponseEntity.ok(new ApiResponse("Cập nhật thồng tin người dùng thành công", convertedUserDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/{id}/delete")
  public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
    try {
      userService.deleteUser(id);
      return ResponseEntity.ok(new ApiResponse("Xóa người dùng thành công", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }
  }
}
