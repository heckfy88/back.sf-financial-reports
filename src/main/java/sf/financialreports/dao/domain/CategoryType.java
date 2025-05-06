package sf.financialreports.dao.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Тип категории")
public enum CategoryType {
    @Schema(description = "Доход")
    INCOME("Доход"),
    @Schema(description = "Расход")
    EXPENSE("Расход"),
    ;

    @Schema(description = "Название категории")
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
