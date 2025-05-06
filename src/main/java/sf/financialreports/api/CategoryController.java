package sf.financialreports.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sf.financialreports.api.dto.CategoryDto;
import sf.financialreports.api.dto.ErrorDto;
import sf.financialreports.dao.domain.RequestType;
import sf.financialreports.service.AuditService;
import sf.financialreports.service.CategoryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/transactions/categories")
@Tag(name = "Категории", description = "Операции с категориями транзакций")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final AuditService auditService;

    @Operation(summary = "Получить список категорий", description = "Возвращает все категории, созданные пользователем")
    @ApiResponse(responseCode = "200", description = "Список категорий успешно получен", content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class))
    ))
    @ApiResponse(responseCode = "500", description = "Internal server error", content =
            { @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorDto.class)) })
    @GetMapping()
    public List<CategoryDto> getCategories(
            @Parameter(description = "Уникальный идентификатор операции", required = true, example = "9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
            @RequestHeader("operUid") UUID operUid,
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(hidden = true) HttpServletResponse response
    ) throws JsonProcessingException {
        auditService.audit(auditService.prepareRequestAudit(
                operUid,
                request,
                RequestType.GET_CATEGORIES,
                null,
                null
        ));

        List<CategoryDto> categories = categoryService.getCategories();

        auditService.audit(auditService.prepareResponseAudit(
                operUid,
                response,
                RequestType.GET_CATEGORIES,
                request.getRequestURI(),
                categories
        ));

        return categories;
    }
}
