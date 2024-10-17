package cab.aggregator.app.rideservice.client;

import cab.aggregator.app.rideservice.dto.exception.ExceptionDto;
import cab.aggregator.app.rideservice.exception.CustomFeignException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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