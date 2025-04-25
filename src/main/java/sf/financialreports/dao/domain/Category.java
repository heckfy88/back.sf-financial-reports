package sf.financialreports.dao.domain;

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
public class Category {
    private UUID id;
    private String name;
    private String description;
    private CategoryType type;
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