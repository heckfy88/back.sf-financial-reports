package sf.financialreports.dao.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class IntegrationLog {
    long id;
    UUID operUid;
    RequestType requestType;
    LocalDateTime createdAt;
    String payload; // если будет проблема с сохранением - заменить в БД json на text
    String requestParams; // формат: {"param1": "value1"; "param2": "value2"}
}