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
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sf.financialreports.api.dto.ErrorDto;
import sf.financialreports.api.dto.TransactionDto;
import sf.financialreports.api.dto.TransactionStatusDto;
import sf.financialreports.api.dto.dashboard.DashboardDto;
import sf.financialreports.api.dto.dashboard.TransactionFilterDto;
import sf.financialreports.dao.domain.Audit;
import sf.financialreports.dao.domain.MessageType;
import sf.financialreports.dao.domain.RequestType;
import sf.financialreports.service.AuditService;
import sf.financialreports.service.TransactionService;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Транзакции", description = "Операции с финансовыми транзакциями")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final AuditService auditService;

    @Operation(summary = "Создание транзакции", description = "Создает новую транзакцию")
    @ApiResponse(responseCode = "200", description = "Транзакция успешно создана", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = TransactionDto.class)
    ))
    @ApiResponse(responseCode = "422", description = "Ошибка парсинга тела json", content =
            { @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorDto.class)) })
    @ApiResponse(responseCode = "500", description = "Internal server error", content =
            { @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorDto.class)) })
    @PostMapping()
    public TransactionDto save(
            @Parameter(description = "Уникальный идентификатор оператора", required = true, example="9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
            @RequestHeader("operUid") UUID operUid,
            @Parameter(description = "Данные транзакции", required = true, content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TransactionDto.class)
            ))
            @RequestBody TransactionDto dto,
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(hidden = true) HttpServletResponse response
    ) throws JsonProcessingException {
        auditService.audit(auditService.prepareRequestAudit(
                operUid,
                request,
                RequestType.SAVE_TRANSACTION,
                dto,
                null
        ));

        TransactionDto savedDto = transactionService.save(dto);

        auditService.audit(auditService.prepareResponseAudit(
                operUid,
                response,
                RequestType.SAVE_TRANSACTION,
                request.getRequestURI(),
                savedDto
        ));

        return savedDto;
    }

    @Operation(summary = "Получение списка транзакций", description = "Возвращает все транзакции пользователя")
    @ApiResponse(responseCode = "200", description = "Список транзакций успешно получен", content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = TransactionDto.class))
    ))
    @ApiResponse(responseCode = "500", description = "Internal server error", content =
            { @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorDto.class)) })
    @GetMapping()
    public List<TransactionDto> getTransactions(
            @Parameter(description = "Уникальный идентификатор оператора", required = true, example="9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
            @RequestHeader("operUid") UUID operUid,
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(hidden = true) HttpServletResponse response
    ) throws JsonProcessingException {
        auditService.audit(auditService.prepareRequestAudit(
                operUid,
                request,
                RequestType.GET_TRANSACTIONS,
                null,
                null
        ));

        List<TransactionDto> transactions = transactionService.getTransactions();

        auditService.audit(auditService.prepareResponseAudit(
                operUid,
                response,
                RequestType.GET_TRANSACTIONS,
                request.getRequestURI(),
                transactions
        ));

        return transactions;
    }

    @Operation(
            summary = "Скачать CSV-файл с транзакциями",
            description = "Позволяет скачать список транзакций в виде CSV-файла.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Файл успешно сгенерирован и отправлен",
                            content = @Content(
                                    mediaType = "text/csv",
                                    schema = @Schema(type = "string", format = "binary")
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Ошибка авторизации"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content =
                            { @Content(mediaType = "application/json", schema =
                            @Schema(implementation = ErrorDto.class)) })
            }
    )
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> download(
            @Parameter(description = "Уникальный идентификатор оператора", required = true, example="9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
            @RequestHeader("operUid") UUID operUid,
            @Parameter(hidden = true) HttpServletRequest request
    ) throws JsonProcessingException {
        auditService.audit(auditService.prepareRequestAudit(
                operUid,
                request,
                RequestType.DOWNLOAD_TRANSACTIONS,
                null,
                null
        ));


        byte[] csvFile = transactionService.download();
        InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(csvFile));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=transactions.csv");

        // response
        auditService.audit(
                Audit.builder()
                        .operUid(operUid)
                        .messageType(MessageType.RESPONSE)
                        .requestType(RequestType.DOWNLOAD_TRANSACTIONS)
                        .requestPath(request.getRequestURI())
                        .requestHeaders(headers.toString())
                        .payload(null)
                        .build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(new MediaType("text", "csv"))
                .body(inputStreamResource);
    }

    @Operation(summary = "Получить данные для дашборда", description = "Возвращает агрегированные данные по фильтру")
    @ApiResponse(responseCode = "200", description = "Данные дашборда успешно получены")
    @ApiResponse(responseCode = "422", description = "Ошибка парсинга тела json", content =
            { @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorDto.class)) })
    @ApiResponse(responseCode = "500", description = "Internal server error", content =
            { @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorDto.class)) })
    @PostMapping("/dashboard")
    public DashboardDto getDashboard(
            @Parameter(description = "Уникальный идентификатор оператора", required = true, example="9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
            @RequestHeader("operUid") UUID operUid,
            @Parameter(description = "Фильтр транзакций", required = true, example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            @RequestBody TransactionFilterDto dto,
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(hidden = true) HttpServletResponse response
    ) throws JsonProcessingException {
        auditService.audit(auditService.prepareRequestAudit(
                operUid,
                request,
                RequestType.GET_DASHBOARD,
                dto,
                null
        ));

        DashboardDto dashboardDto = transactionService.getDashboard(dto);

        auditService.audit(auditService.prepareResponseAudit(
                operUid,
                response,
                RequestType.GET_DASHBOARD,
                request.getRequestURI(),
                dashboardDto
        ));

        return dashboardDto;
    }

    @Operation(summary = "Обновить транзакцию", description = "Обновляет существующую транзакцию")
    @ApiResponse(responseCode = "200", description = "Транзакция успешно обновлена", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = TransactionDto.class)
    ))
    @ApiResponse(responseCode = "422", description = "Ошибка парсинга тела json", content =
            { @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorDto.class)) })
    @ApiResponse(responseCode = "500", description = "Internal server error", content =
            { @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorDto.class)) })
    @PatchMapping()
    public TransactionDto update(
            @Parameter(description = "Уникальный идентификатор оператора", required = true, example="9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
            @RequestHeader("operUid") UUID operUid,
            @Parameter(description = "Обновленные данные транзакции", required = true, example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            @RequestBody TransactionDto dto,
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(hidden = true) HttpServletResponse response
    ) throws JsonProcessingException {
        auditService.audit(auditService.prepareRequestAudit(
                operUid,
                request,
                RequestType.UPDATE_TRANSACTION,
                dto,
                null
        ));

        TransactionDto updatedDto = transactionService.update(dto);

        auditService.audit(auditService.prepareResponseAudit(
                operUid,
                response,
                RequestType.UPDATE_TRANSACTION,
                request.getRequestURI(),
                updatedDto
        ));

        return updatedDto;
    }

    @Operation(summary = "Удалить транзакцию", description = "Удаляет транзакцию по её идентификатору")
    @ApiResponse(responseCode = "200", description = "Транзакция успешно удалена", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = TransactionDto.class)
    ))
    @ApiResponse(responseCode = "422", description = "Ошибка парсинга тела json", content =
            { @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorDto.class)) })
    @ApiResponse(responseCode = "500", description = "Internal server error", content =
            { @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorDto.class)) })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @Parameter(description = "Уникальный идентификатор оператора", required = true, example="9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
            @RequestHeader("operUid") UUID operUid,
            @Parameter(description = "Идентификатор транзакции", required = true, example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            @PathVariable UUID id,
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(hidden = true) HttpServletResponse response
    ) throws JsonProcessingException {
        auditService.audit(auditService.prepareRequestAudit(
                operUid,
                request,
                RequestType.DELETE_TRANSACTION,
                null,
                "{\"id\": %s}".formatted(id)
        ));

        transactionService.delete(id);

        String message = "Transaction '%s' deleted successfully".formatted(id);

        auditService.audit(auditService.prepareResponseAudit(
                operUid,
                response,
                RequestType.DELETE_TRANSACTION,
                request.getRequestURI(),
                message
        ));

        return ResponseEntity.ok().body(message);
    }

    @Operation(summary = "Получить статусы транзакций", description = "Возвращает возможные статусы транзакций")
    @ApiResponse(responseCode = "200", description = "Статусы успешно получены")
    @ApiResponse(responseCode = "500", description = "Internal server error", content =
            { @Content(mediaType = "application/json", schema =
            @Schema(implementation = ErrorDto.class)) })
    @GetMapping("/statuses")
    public List<TransactionStatusDto> getStatuses(
            @Parameter(description = "Уникальный идентификатор оператора", required = true, example="9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
            @RequestHeader("operUid") UUID operUid,
            @Parameter(hidden = true) HttpServletRequest request,
            @Parameter(hidden = true) HttpServletResponse response
    ) throws JsonProcessingException {
        auditService.audit(auditService.prepareRequestAudit(
                operUid,
                request,
                RequestType.GET_STATUSES,
                null,
                null
        ));
        List<TransactionStatusDto> statuses = transactionService.getStatuses();

        auditService.audit(auditService.prepareResponseAudit(
                operUid,
                response,
                RequestType.GET_STATUSES,
                request.getRequestURI(),
                statuses
        ));

        return statuses;
    }
}
