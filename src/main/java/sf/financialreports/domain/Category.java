package sf.financialreports.domain;

import sf.financialreports.api.dto.CategoryDto;

import java.util.UUID;

public record Category(
        UUID id,
        String name,
        String description,
        CategoryType type
) {
    public static Category from(CategoryDto dto) {
        return new Category(
                dto.id(),
                dto.name(),
                dto.description(),
                dto.type()
        );
    }
}