package sf.financialreports.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import sf.financialreports.dao.domain.Status;

@Schema(description = "Статус транзакции")
public record TransactionStatusDto(

        @Schema(description = "Имя статуса (enum name)", example = "CONFIRMED")
        String name,

        @Schema(description = "Отображаемое название статуса", example = "Подтвержденная")
        String title,

        @Schema(description = "Приоритет статуса", example = "100")
        int weight
) {
    public static TransactionStatusDto from(Status status) {
        return new TransactionStatusDto(
                status.name(),
                status.getTitle(),
                status.getWeight()
        );
    }

    public static TransactionStatusDto from(String status) {
        Status statusEnum = Status.fromString(status);
        return from(statusEnum);
    }

    @JsonCreator
    public TransactionStatusDto(
            @JsonProperty("name") String name,
            @JsonProperty("title") String title,
            @JsonProperty("weight") int weight
    ) {
        this.name = name;
        this.title = title;
        this.weight = weight;
    }
}
