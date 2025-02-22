package cab.aggregator.app.authservice.client;

import cab.aggregator.app.authservice.exception.ExternalClientException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static cab.aggregator.app.authservice.util.Constants.MESSAGE;

@Component
public class ExternalClientErrorDecoder implements ErrorDecoder {

    @Override
    @SneakyThrows
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(readResponseBody(response));
        String message = jsonNode.get(MESSAGE).asText();
        return new ExternalClientException(message, response.status());
    }

    @SneakyThrows
    protected String readResponseBody(Response response) {
        @Cleanup
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8));
        return bufferedReader.lines()
                .collect(Collectors.joining("\n"));
    }
}
