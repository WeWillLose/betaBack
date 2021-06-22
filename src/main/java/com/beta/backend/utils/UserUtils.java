package com.beta.backend.utils;

import com.beta.backend.domain.model.User;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

import static org.springframework.util.StringUtils.capitalize;

@Component
public class UserUtils {

    public static String getFioFromUser(@Nullable User user) {
        if (user == null) return "";
        StringBuilder stringBuilder = new StringBuilder();
        if (user.getLastName() != null && !user.getLastName().isBlank()) {
            stringBuilder.append(capitalize(user.getLastName()));
        }
        if (user.getFirstName() != null && !user.getFirstName().isBlank()) {
            stringBuilder.append(" ").append(capitalize(user.getFirstName()));
        }
        if (user.getMiddleName() != null && !user.getMiddleName().isBlank()) {
            stringBuilder.append(" ").append(capitalize(user.getMiddleName()));
        }
        return stringBuilder.toString();

    }


    public static String getShortFioFromUser(@Nullable User user) {

        if (user == null) return "";

        StringBuilder stringBuilder = new StringBuilder();
        if (user.getLastName() != null && !user.getLastName().isBlank()) {
            stringBuilder.append(capitalize(user.getLastName()));
        }
        if (user.getFirstName() != null && !user.getFirstName().isBlank()) {
            stringBuilder.append(" ").append(user.getFirstName().substring(0, 1).toUpperCase()).append(".");
        }
        if (user.getMiddleName() != null && !user.getMiddleName().isBlank()) {
            stringBuilder.append(" ").append(capitalize(user.getMiddleName().substring(0, 1).toUpperCase())).append(".");
        }
        return stringBuilder.toString();
    }
}
