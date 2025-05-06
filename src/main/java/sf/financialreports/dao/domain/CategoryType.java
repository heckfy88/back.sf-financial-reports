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

    public static sf.financialreports.dao.jooq.enums.CategoryType fromDomain(CategoryType userType) {
        return sf.financialreports.dao.jooq.enums.CategoryType.valueOf(userType.name());
    }

    public static CategoryType fromDb(sf.financialreports.dao.jooq.enums.CategoryType userType) {
        return CategoryType.valueOf(userType.name());
    }
}
