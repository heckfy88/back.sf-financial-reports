package sf.financialreports.dao.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Audit {
    UUID id;
    UUID operUid;
    UUID userId;
    MessageType messageType;
    String requestPath;
    RequestType requestType;
    String requestHeaders; // exclude Authorization or mask it
    String requestParams; // формат: {"param1": "value1"; "param2": "value2"}
    String payload; // если будет проблема с сохранением - заменить в БД json на text
    LocalDateTime createdAt;
}