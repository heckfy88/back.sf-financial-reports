package sf.financialreports.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sf.financialreports.api.dto.CategoryDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/transactions/categories")
public class CategoryController {


    @GetMapping()
    public List<CategoryDto> getCategories(@RequestHeader("operUid") UUID operUid) {
        // TODO: Implement this, Valyok
        return List.of();
    }
}
