package com.mvo.personservice.mapper;

import com.mvo.personservice.entity.UserHistory;
import dto.UserHistoryDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserHistoryMapper {
    UserHistoryDTO map(UserHistory userHistory);

    @InheritInverseConfiguration
    UserHistory map(UserHistoryDTO userHistoryDTO);
}
