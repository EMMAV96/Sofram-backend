package com.sofram.shared.web;

import java.util.List;

public record ApiError(
        String timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldErrorDetail> fieldErrors
) {
}
