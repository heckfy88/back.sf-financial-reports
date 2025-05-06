package sf.financialreports.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sf.financialreports.api.dto.CategoryDto;
import sf.financialreports.dao.domain.RequestType;
import sf.financialreports.service.AuditService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/transactions/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final AuditService auditService;

    @GetMapping()

    public List<CategoryDto> getCategories(
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

        // TODO: getCategories logic

        auditService.audit(auditService.prepareResponseAudit(
                operUid,
                response,
                RequestType.GET_CATEGORIES,
                request.getRequestURI(),
                List.of()
        ));

        return List.of();
    }
}
