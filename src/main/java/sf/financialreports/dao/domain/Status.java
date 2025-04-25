package sf.financialreports.dao.domain;

import lombok.Getter;


@Getter
public enum Status {
    NEW("Новая", 0),
    CONFIRMED("Подтвержденная", 100),
    PROCESSING("В обработке", 200),
    COMPLETED("Платеж выполнен", 300),
    RETURNED("Возврат", 400),
    CANCELLED("Отменена", 500),
    DELETED("Платеж удален", 600),
    ;

    private final String title;
    private final int weight;

    Status(String title, int weight) {
        this.title = title;
        this.weight = weight;
    }

}
