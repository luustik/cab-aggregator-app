package cab.aggregator.app.authservice.controller.impl;

import cab.aggregator.app.authservice.controller.UserController;
import cab.aggregator.app.authservice.dto.request.RefreshTokenDto;
import cab.aggregator.app.authservice.dto.request.SignInDto;
import cab.aggregator.app.authservice.dto.request.SignUpDto;
import cab.aggregator.app.authservice.dto.response.UserResponseTokenDto;
import cab.aggregator.app.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    @PostMapping("/sign-in")
    public UserResponseTokenDto signIn(@Valid @RequestBody SignInDto signInDto) {
        return userService.signIn(signInDto);
    }

    @Override
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@Valid @RequestBody SignUpDto signUpDto) {
        userService.signUp(signUpDto);
    }

    @Override
    @PostMapping("/refresh-token")
    public UserResponseTokenDto refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        return userService.refreshToken(refreshTokenDto);
    }
}
