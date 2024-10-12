package ru.practicum;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.stats.dto.StatDto;
import ru.practicum.ewm.stats.dto.StatDtoRequest;
import ru.practicum.ewm.stats.dto.StatsRequestContext;
import ru.practicum.service.StatsService;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class StatsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private StatsService statsService;

    @SneakyThrows
    @Test
    void create() {
        StatDto prepareStatDto = new StatDto();
        prepareStatDto.setApp("app");
        prepareStatDto.setUri("uri");
        prepareStatDto.setIp("ip");
        prepareStatDto.setTimestamp(Instant.now());

        StatDto statDto = objectMapper.readValue(objectMapper.writeValueAsString(prepareStatDto), StatDto.class);
        when(statsService.create(statDto)).thenReturn(statDto);

        String result = mockMvc.perform(post("/hit")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(statDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(statDto), result);
    }

    @SneakyThrows
    @Test
    void findStats() {
        StatsRequestContext statsRequestContext = new StatsRequestContext();
        statsRequestContext.setStart("2024-10-08 23:06:12");
        statsRequestContext.setEnd("2024-10-08 23:16:12");
        StatDtoRequest statDtoRequest;
        try {
            String jsObject = objectMapper.writeValueAsString(statsRequestContext);
            statDtoRequest = objectMapper.readValue(jsObject, StatDtoRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации");
        }

        mockMvc.perform(get("/stats" + statsRequestContext.toParams()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(statsService).findStats(statDtoRequest);
    }
}