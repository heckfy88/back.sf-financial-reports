package sf.financialreports.api.dto;

import lombok.Builder;
import sf.financialreports.domain.Category;
import sf.financialreports.domain.CategoryType;

import java.util.UUID;

@Builder
public record CategoryDto(
        UUID id,
        String name,
        String description,
        CategoryType type
) {
    public static CategoryDto from(Category category) {
        return new CategoryDto(
                category.id(),
                category.name(),
                category.description(),
                category.type()
        );
    }
}