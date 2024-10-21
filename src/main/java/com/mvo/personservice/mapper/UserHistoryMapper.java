package com.mvo.personservice.mapper;

import com.mvo.personservice.entity.UserHistory;
import dto.UserHistoryDTO;
import io.r2dbc.postgresql.codec.Json;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {Json.class})
public interface UserHistoryMapper {
    @Mapping(target = "changedValues", expression = "java(userHistory.getChangedValues().asString())")
    UserHistoryDTO map(UserHistory userHistory);

    @InheritInverseConfiguration
    @Mapping(target = "changedValues", expression = "java(Json.of(userHistoryDTO.changedValues()))")
    UserHistory map(UserHistoryDTO userHistoryDTO);
}
