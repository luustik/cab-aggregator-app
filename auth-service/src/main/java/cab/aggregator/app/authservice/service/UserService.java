package cab.aggregator.app.authservice.service;

import cab.aggregator.app.authservice.dto.request.RefreshTokenDto;
import cab.aggregator.app.authservice.dto.request.SignInDto;
import cab.aggregator.app.authservice.dto.request.SignUpDto;
import cab.aggregator.app.authservice.dto.response.UserResponse;
import cab.aggregator.app.authservice.dto.response.UserResponseTokenDto;

public interface UserService {

    UserResponse signUp(SignUpDto signUpDto);

    UserResponseTokenDto signIn(SignInDto signInDto);

    UserResponseTokenDto refreshToken(RefreshTokenDto refreshTokenDto);
}
