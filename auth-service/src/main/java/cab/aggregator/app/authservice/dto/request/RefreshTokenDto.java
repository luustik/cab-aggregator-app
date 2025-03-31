package cab.aggregator.app.authservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "SignUp DTO")
public record RefreshTokenDto(
        @Schema(description = "Refresh Token")
        @NotBlank
        String refreshToken
) {
}
