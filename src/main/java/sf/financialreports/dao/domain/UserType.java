package sf.financialreports.dao.domain;

import lombok.Getter;

@Getter
public enum UserType {
    PHYSICAL("Физическое лицо"),
    LEGAL("Юридическое лицо"),
    ;

    private final String name;

    UserType(String name) {
        this.name = name;
    }
}
