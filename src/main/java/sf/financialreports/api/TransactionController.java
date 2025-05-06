package sf.financialreports.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sf.financialreports.api.dto.TransactionDto;
import sf.financialreports.api.dto.TransactionStatusDto;
import sf.financialreports.api.dto.dashboard.DashboardDto;
import sf.financialreports.api.dto.dashboard.TransactionFilterDto;
import sf.financialreports.service.TransactionService;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Транзакции", description = "Операции с финансовыми транзакциями")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Создание транзакции", description = "Создает новую транзакцию")
    @ApiResponse(responseCode = "200", description = "Транзакция успешно создана", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = TransactionDto.class)
    ))
    @PostMapping()
    public TransactionDto save(
            @Parameter(description = "Уникальный идентификатор оператора", required = true, example="9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
            @RequestHeader("operUid") UUID operUid,
            @Parameter(description = "Данные транзакции", required = true, content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TransactionDto.class)
            ))
            @RequestBody TransactionDto dto
    ) {
        return transactionService.save(dto);
    }

    @Operation(summary = "Получение списка транзакций", description = "Возвращает все транзакции пользователя")
    @ApiResponse(responseCode = "200", description = "Список транзакций успешно получен", content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = TransactionDto.class))
    ))
    @GetMapping()
    public List<TransactionDto> getTransactions(
            @Parameter(description = "Уникальный идентификатор оператора", required = true, example="9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
            @RequestHeader("operUid") UUID operUid
    ) {
        return transactionService.getTransactions();
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
                    @ApiResponse(responseCode = "401", description = "Неавторизован"),
                    @ApiResponse(responseCode = "500", description = "Ошибка сервера")
            }
    )
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> download(
            @Parameter(description = "Уникальный идентификатор оператора", required = true, example="9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
            @RequestHeader("operUid") UUID operUid
    ) {
        byte[] csvFile = transactionService.download();
        InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(csvFile));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=transactions.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(new MediaType("text", "csv"))
                .body(inputStreamResource);
    }

    @Operation(summary = "Получить данные для дашборда", description = "Возвращает агрегированные данные по фильтру")
    @ApiResponse(responseCode = "200", description = "Данные дашборда успешно получены")
    @PostMapping("/dashboard")
    public DashboardDto getDashboard(
            @Parameter(description = "Уникальный идентификатор оператора", required = true, example="9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
            @RequestHeader("operUid") UUID operUid,
            @Parameter(description = "Фильтр транзакций", required = true, example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            @RequestBody TransactionFilterDto dto
    ) {
        return transactionService.getDashboard(dto);
    }

    @Operation(summary = "Обновить транзакцию", description = "Обновляет существующую транзакцию")
    @ApiResponse(responseCode = "200", description = "Транзакция успешно обновлена")
    @PatchMapping("")
    public TransactionDto update(
            @Parameter(description = "Уникальный идентификатор оператора", required = true, example="9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
            @RequestHeader("operUid") UUID operUid,
            @Parameter(description = "Обновленные данные транзакции", required = true, example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            @RequestBody TransactionDto dto
    ) {
        return transactionService.update(dto);
    }

    @Operation(summary = "Удалить транзакцию", description = "Удаляет транзакцию по её идентификатору")
    @ApiResponse(responseCode = "200", description = "Транзакция успешно удалена")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @Parameter(description = "Уникальный идентификатор оператора", required = true, example="9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
            @RequestHeader("operUid") UUID operUid,
            @Parameter(description = "Идентификатор транзакции", required = true, example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            @PathVariable UUID id
    ) {
        transactionService.delete(id);
        return ResponseEntity.ok().body("Transaction '%s' deleted successfully".formatted(id));
    }

    @Operation(summary = "Получить статусы транзакций", description = "Возвращает возможные статусы транзакций")
    @ApiResponse(responseCode = "200", description = "Статусы успешно получены")
    @GetMapping("/statuses")
    public List<TransactionStatusDto> getStatuses(
            @Parameter(description = "Уникальный идентификатор оператора", required = true, example="9f8c1d45-b4e1-4f4b-9ad8-12b3d98f726e")
            @RequestHeader("operUid") UUID operUid
    ) {
        return transactionService.getStatuses();
    }
}
