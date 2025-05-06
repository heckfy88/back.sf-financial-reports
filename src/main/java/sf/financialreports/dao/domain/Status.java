package sf.financialreports.dao.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


@Getter
@Schema(description = "Статус транзакции")
public enum Status {
    @Schema(description = "Новая")
    NEW("Новая", 0),
    @Schema(description = "Подтвержденная")
    CONFIRMED("Подтвержденная", 100),
    @Schema(description = "В обработке")
    PROCESSING("В обработке", 200),
    @Schema(description = "Платеж выполнен")
    COMPLETED("Платеж выполнен", 300),
    @Schema(description = "Возврат")
    RETURNED("Возврат", 400),
    @Schema(description = "Отменена")
    CANCELLED("Отменена", 500),
    @Schema(description = "Транзакция удалена")
    DELETED("Платеж удален", 600),
    ;

    private final String title;
    private final int weight;

    Status(String title, int weight) {
        this.title = title;
        this.weight = weight;
    }

    @JsonCreator
    public static Status fromString(String value) {
        return Status.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
