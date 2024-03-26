package dev.filochowski.springdemo.user.auth;

public interface AuthenticationService {
    String register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    String confirmToken(String token);
}
