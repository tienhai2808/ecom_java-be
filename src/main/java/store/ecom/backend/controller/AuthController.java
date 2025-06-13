package store.ecom.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.ecom.backend.request.LoginRequest;
import store.ecom.backend.response.ApiResponse;
import store.ecom.backend.response.JwtResponse;
import store.ecom.backend.security.jwt.JwtUtils;
import store.ecom.backend.security.user.ShopUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest req) {
    try {
      Authentication authentication = authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = jwtUtils.generaTokenForUser(authentication);
      ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
      JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);
      return ResponseEntity.ok(new ApiResponse("Đăng nhập thành công", jwtResponse));
    } catch (AuthenticationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Email hoặc mật khẩu không chính xác", null));
    }
  }
}
