package sf.financialreports.dao.domain;

import lombok.Getter;

@Getter
public enum CategoryType {
    INCOME("Доход"),
    EXPENSE("Расход"),
    ;

    private final String title;

    CategoryType(String title) {
        this.title = title;
    }
}
