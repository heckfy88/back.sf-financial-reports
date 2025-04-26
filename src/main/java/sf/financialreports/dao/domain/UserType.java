package sf.financialreports.dao.domain;

import lombok.Getter;

@Getter
public enum UserType {
    PHYSICAL_PERSON("Физическое лицо"),
    LEGAL_ENTITY("Юридическое лицо"),
    ;

    private final String name;

    UserType(String name) {
        this.name = name;
    }
}
