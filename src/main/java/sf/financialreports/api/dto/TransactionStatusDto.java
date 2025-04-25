package sf.financialreports.api.dto;

import sf.financialreports.dao.domain.Status;


public record TransactionStatusDto(
        String name,
        String title,
        int weight
) {
    public static TransactionStatusDto from(Status status) {
        return new TransactionStatusDto(
                status.name(),
                status.getTitle(),
                status.getWeight()
        );
    }
}