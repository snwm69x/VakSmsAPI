package com.snwm.enums;

public enum ApiError {
    API_KEY_NOT_FOUND("apiKeyNotFound", "Неверный API ключ"),
    NO_SERVICE("noService", "Данный сервис не поддерживается, свяжитесь с администрацией сайта"),
    NO_NUMBER("noNumber", "Нет номеров, попробуйте позже"),
    NO_MONEY("noMoney", "Недостаточно средств, пополните баланс"),
    NO_COUNTRY("noCountry", "Запрашиваемая страна отсутствует"),
    NO_OPERATOR("noOperator", "Оператор не найден для запрашиваемой страны"),
    BAD_STATUS("badStatus", "Не верный статус"),
    ID_NUM_NOT_FOUND("idNumNotFound", "Не верный ID операции"),
    BAD_SERVICE("badService", "Не верный код сайта, сервиса, соц. сети"),
    FAILED_REQUEST("failedToSendRequest", "Ошибка отправки запроса"),
    BAD_DATA("badData", "Отправлены неверные данные");

    private final String error;
    private final String description;

    ApiError(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }

    public static ApiError fromError(String err) {
        for (ApiError error : ApiError.values()) {
            if (error.getError().equals(err)) {
                return error;
            }
        }
        throw new IllegalArgumentException("No enum constant with description " + err);
    }
}
