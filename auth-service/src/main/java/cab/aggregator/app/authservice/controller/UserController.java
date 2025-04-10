package cab.aggregator.app.authservice.controller;

import cab.aggregator.app.authservice.dto.request.RefreshTokenDto;
import cab.aggregator.app.authservice.dto.request.SignInDto;
import cab.aggregator.app.authservice.dto.request.SignUpDto;
import cab.aggregator.app.authservice.dto.response.UserResponse;
import cab.aggregator.app.authservice.dto.response.UserResponseTokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
@Tag(name = "User controller")
public interface UserController {

    @Operation(summary = "Sign In by user")
    UserResponseTokenDto signIn(@Valid @RequestBody SignInDto signInDto);

    @Operation(summary = "Sign Up for new user")
    UserResponse signUp(@Valid @RequestBody SignUpDto signUpDto);

    @Operation(summary = "Refresh Token")
    UserResponseTokenDto refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto);
}
