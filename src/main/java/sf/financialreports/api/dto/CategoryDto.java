package sf.financialreports.api.dto;

import lombok.Builder;
import lombok.Data;
import sf.financialreports.dao.domain.Category;
import sf.financialreports.dao.domain.CategoryType;

import java.util.UUID;

@Builder
@Data
public class CategoryDto {
    private UUID id;
    private String name;
    private String description;
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