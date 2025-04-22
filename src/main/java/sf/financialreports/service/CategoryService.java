package sf.financialreports.service;

import sf.financialreports.api.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategories();
}
