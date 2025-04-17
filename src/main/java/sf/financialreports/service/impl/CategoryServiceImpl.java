package sf.financialreports.service.impl;

import org.springframework.stereotype.Service;
import sf.financialreports.api.dto.CategoryDto;
import sf.financialreports.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {


    @Override
    public List<CategoryDto> getCategories() {
        return List.of();
    }
}
