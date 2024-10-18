package cab.aggregator.app.ratingservice.client;

import cab.aggregator.app.ratingservice.dto.client.ClientException;
import cab.aggregator.app.ratingservice.dto.exception.ExceptionDto;
import cab.aggregator.app.ratingservice.exception.ExternalClientException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ExternalClientErrorDecoder implements ErrorDecoder {

    @Override
    @SneakyThrows
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(readResponseBody(response));
        String message = jsonNode.get("message").asText();
        ClientException clientException = ClientException.builder()
                .status(HttpStatus.valueOf(response.status()))
                .message(message)
                .build();
        return new ExternalClientException(clientException);
    }

    @SneakyThrows
    protected String readResponseBody(Response response) {
        if (Objects.nonNull(response.body())) {
            @Cleanup
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8));
            return bufferedReader.lines()
                    .collect(Collectors.joining("\n"));
        }
        return "";
    }
}
