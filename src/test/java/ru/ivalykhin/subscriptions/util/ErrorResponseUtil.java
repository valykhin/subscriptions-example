package ru.ivalykhin.subscriptions.util;

import ru.ivalykhin.subscriptions.exception.SubscriptionsBusinessException;

public class ErrorResponseUtil {
    public static String getErrorMessage(SubscriptionsBusinessException exception) {
        return """
                {
                "error_code": "%s",
                "message": "%s"
                }
                """.formatted(exception.getErrorCode(),
                ((RuntimeException) exception).getMessage());
    }
}
