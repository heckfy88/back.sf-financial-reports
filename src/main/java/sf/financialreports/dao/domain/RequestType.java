package sf.financialreports.dao.domain;

public enum RequestType {
    LOGIN,

    SAVE_TRANSACTION,
    UPDATE_TRANSACTION,
    DELETE_TRANSACTION,
    GET_TRANSACTIONS,
    DOWNLOAD_TRANSACTIONS,

    GET_DASHBOARD,


    GET_CATEGORIES,
    GET_STATUSES,

    ERROR,
}