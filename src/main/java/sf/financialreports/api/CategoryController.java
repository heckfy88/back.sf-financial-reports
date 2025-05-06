package sf.financialreports.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sf.financialreports.api.dto.CategoryDto;
import sf.financialreports.api.dto.ErrorDto;
import sf.financialreports.api.dto.TransactionDto;
import sf.financialreports.service.CategoryService;
import sf.financialreports.service.TransactionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/transactions/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Получение списка категорий", description = "Возвращает все категории, созданные пользователем")
    @ApiResponse(responseCode = "200", description = "Список категорий успешно получен", content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class))
    ))
    @ApiResponse(responseCode = "500", description = "Internal server error", content =
            { @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorDto.class)) })
    @GetMapping()
    public List<CategoryDto> getTransactions(
            @Parameter(description = "Уникальный идентификатор оператора", required = true, example="9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
            @RequestHeader("operUid") UUID operUid
    ) {
        return categoryService.getCategories();
    }
}
