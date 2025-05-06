package sf.financialreports.dao.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sf.financialreports.api.dto.CategoryDto;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Категория транзакции, используемая для классификации доходов и расходов")
public class Category {

    @Schema(description = "Уникальный идентификатор категории", example = "c1e9f12b-2d49-4c0e-b7a7-e0baf7a1d456")
    private UUID id;

    @Schema(description = "Название категории", example = "Продукты")
    private String name;

    @Schema(description = "Описание категории", example = "Расходы на продукты питания")
    private String description;

    @Schema(description = "Тип категории (Доход или Расход)", example = "EXPENSE")
    private CategoryType type;

    @Schema(description = "Идентификатор пользователя, которому принадлежит категория", example = "f58d0724-654a-43fc-9b7e-b0ff9fc1c0b1")
    private UUID userId;

    public static Category from(CategoryDto dto, UUID userId) {
        return new Category(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getType(),
                userId
        );
    }
}