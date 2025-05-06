package sf.financialreports.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import sf.financialreports.dao.domain.Category;
import sf.financialreports.dao.domain.CategoryType;

import java.util.UUID;

@Builder
@Data
@Schema(description = "Категория транзакции")
public class CategoryDto {
    @Schema(description = "Идентификатор категории", example = "d1a78e9b-cbd9-4f6b-b614-5c9c2f5153ba")
    private UUID id;
    @Schema(description = "Название категории", example = "Зарплата")
    private String name;
    @Schema(description = "Описание категории", example = "Регулярный доход от работодателя")
    private String description;
    @Schema(description = "Тип категории (INCOME — доход, EXPENSE — расход)", example = "INCOME")
    private CategoryType type;

    public static CategoryDto from(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getType()
        );
    }
}