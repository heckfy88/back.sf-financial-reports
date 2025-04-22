package sf.financialreports.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sf.financialreports.api.dto.CategoryDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/transactions/categories")
public class CategoryController {


    @GetMapping()
    public List<CategoryDto> getCategories() {
        // TODO: Implement this, Valyok
        return List.of();
    }
}
