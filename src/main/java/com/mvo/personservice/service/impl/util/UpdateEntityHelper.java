package com.mvo.personservice.service.impl.util;

import com.mvo.personservice.entity.User;
import com.mvo.personservice.entity.UserHistory;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class UpdateEntityHelper {
    public <T> void checkAndUpdateFields(Supplier<T> fieldGetterFromEntityFromRequest, Supplier<T> fieldGetterFromEntityFoundedForUpdate,
                                         String fieldName, Consumer<T> fieldSetterFromEntityFoundedForUpdate, Map<String, Object> changedValues) {
        if ((fieldGetterFromEntityFromRequest.get() != null)
                && !fieldGetterFromEntityFromRequest.get().equals(fieldGetterFromEntityFoundedForUpdate.get())) {
            fieldSetterFromEntityFoundedForUpdate.accept(fieldGetterFromEntityFromRequest.get());
            changedValues.put(fieldName, fieldGetterFromEntityFromRequest.get());
        }
    }

    public void checkAndUpdateLocalDateTimeFields(Supplier<LocalDateTime> fieldGetterFromEntityFromRequest, Supplier<LocalDateTime> fieldGetterFromEntityFoundedForUpdate,
                                                  String fieldName, Consumer<LocalDateTime> fieldSetterFromEntityFoundedForUpdate, Map<String, Object> changedValues) {
        if ((fieldGetterFromEntityFromRequest.get() != null)
                && !fieldGetterFromEntityFromRequest.get().withNano(0).equals(fieldGetterFromEntityFoundedForUpdate.get().withNano(0))) {
            fieldSetterFromEntityFoundedForUpdate.accept(fieldGetterFromEntityFromRequest.get());
            changedValues.put(fieldName, fieldGetterFromEntityFromRequest.get());
        }
    }

    public String createJson(Map<String, Object> changedValues) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");

        changedValues.forEach((key, value) -> {
            jsonBuilder.append("\"")
                    .append(key)
                    .append("\": ")
                    .append("\"")
                    .append(value)
                    .append("\"")
                    .append(", ");
        });

        if (jsonBuilder.length() > 1) {
            jsonBuilder.setLength(jsonBuilder.length() - 2);
        }

        jsonBuilder.append("}");

        return jsonBuilder.toString();
    }

    public UserHistory createUserHistoryEntity(User updatedUser, String changedValues) {
        return UserHistory.builder()
                .created(LocalDateTime.now())
                .userId(updatedUser.getId())
                .reason("BY_SYSTEM")
                .userType("user")
                .changedValues(Json.of(changedValues))
                .build();
    }

}
