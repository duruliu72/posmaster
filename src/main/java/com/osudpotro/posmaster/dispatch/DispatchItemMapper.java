package com.osudpotro.posmaster.dispatch;

import org.springframework.stereotype.Component;

@Component
public class DispatchItemMapper {
    //Mapping Here
    //Entity → DTO
    public DispatchItemDto toDto(DispatchItem dispatchItem) {
        DispatchItemDto dto=new DispatchItemDto();
        return dto;
    }
}
