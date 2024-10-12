package ru.practicum;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import ru.practicum.ewm.stats.dto.StatDto;
import ru.practicum.ewm.stats.dto.StatDtoResponse;
import ru.practicum.ewm.stats.dto.StatsRequestContext;
import ru.practicum.exceptions.JsonSerializeException;
import ru.practicum.exceptions.StatsServiceException;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
public class StatsClientImpl implements StatsClient {
    private final RestClient restClient;
    @Autowired
    private final ObjectMapper objectMapper;

    public StatsClientImpl(String uriBase) {
        this.objectMapper = new ObjectMapper();
        restClient = RestClient.builder()
                .baseUrl(uriBase)
                .build();
    }

    @Override
    public StatDto create(StatDto statDto) {
        String result = null;
        try {
            result = restClient.post()
                    .uri("/hit")
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(statDto))
                    .retrieve()
                    .body(String.class);

        } catch (JsonProcessingException e) {
            String message = String.format("Exception during serialize StatDto to json: %s", statDto);
            log.warn(message);
            throw new JsonSerializeException(message);

        } catch (RestClientResponseException e) {
            String responseMessage = e.getMessage().replace("\"", "");
            String message = String.format("Exception during create stat: %s", responseMessage);
            log.warn(message);
            throw new StatsServiceException(e.getStatusCode().value(), message);
        }

        try {
            return objectMapper.readValue(result, StatDto.class);

        } catch (JsonProcessingException e) {
            String message = String.format("Exception during deserialize json string to StatDto: %s", result);
            log.warn(message);
            throw new JsonSerializeException(message);
        }
    }

    @Override
    public List<StatDtoResponse> findStats(StatsRequestContext statsRequestContext) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/stats")
                            .queryParam("start", statsRequestContext.getStart())
                            .queryParam("end", statsRequestContext.getEnd())
                            .queryParam("uris", statsRequestContext.getUris())
                            .queryParam("unique", statsRequestContext.isUnique())
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<StatDtoResponse>>() {});

        } catch (RestClientResponseException e) {
            String responseMessage = e.getMessage().replace("\"", "");
            String message = String.format("Exception during search stats: %s", responseMessage);
            log.warn(message);
            throw new StatsServiceException(e.getStatusCode().value(), message);
        }
    }
}
