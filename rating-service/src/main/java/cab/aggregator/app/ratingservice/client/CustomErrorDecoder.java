package cab.aggregator.app.ratingservice.client;

import cab.aggregator.app.ratingservice.exception.CustomFeignException;
import cab.aggregator.app.ratingservice.dto.exception.ExceptionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    @SneakyThrows
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();
        ExceptionDto exceptionDto = objectMapper.readValue(readResponseBody(response), ExceptionDto.class);
        return new CustomFeignException(exceptionDto.message());
    }

    @SneakyThrows
    protected String readResponseBody(Response response) {
        if (Objects.nonNull(response.body())) {
            return new BufferedReader(
                    new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
        }
        return "";
    }
}
