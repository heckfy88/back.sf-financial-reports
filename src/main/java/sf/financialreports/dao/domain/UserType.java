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

    public static sf.financialreports.dao.jooq.enums.UserType fromDomain(UserType userType) {
        return sf.financialreports.dao.jooq.enums.UserType.valueOf(userType.name());
    }

    public static UserType fromDb(sf.financialreports.dao.jooq.enums.UserType userType) {
        return UserType.valueOf(userType.name());
    }
}
