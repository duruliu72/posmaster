package com.osudpotro.posmaster.dispatch;


import org.springframework.stereotype.Component;

@Component
public class DispatchMapper {
    //Mapping Here
    //Entity → DTO
    public DispatchDto toDto(Dispatch dispatch) {
        DispatchDto dispatchDto=new DispatchDto();
        dispatchDto.setId(dispatch.getId());
        dispatchDto.setDispatchRef(dispatch.getDispatchRef());
        return dispatchDto;
    }
}
