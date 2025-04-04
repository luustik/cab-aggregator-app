package cab.aggregator.app.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenDto(
        @NotBlank
        String refreshToken
) {
}
