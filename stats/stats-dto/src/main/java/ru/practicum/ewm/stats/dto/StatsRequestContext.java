package ru.practicum.ewm.stats.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsRequestContext {

    @NotBlank(message = "Start period of needed stats must be set")
    String start;

    @NotBlank(message = "End period of needed stats must be set")
    String end;

    //@NotEmpty(message = "Uris of needed stats must be set")
    List<String> uris;

    boolean unique = false;

    public String toParams() {
        StringBuilder sb = new StringBuilder();
        sb.append("?start=").append(this.start)
                .append("&end=").append(this.end)
                .append("&unique=").append(this.unique);

        if (uris != null) {
            sb.append("&uris=").append(String.join(", ", this.uris));
        }
        return sb.toString();
    }
}
