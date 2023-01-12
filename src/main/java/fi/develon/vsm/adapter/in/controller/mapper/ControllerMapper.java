package fi.develon.vsm.adapter.in.controller.mapper;

public interface ControllerMapper<RequestDto, RequestObj, ResponseDto, ResponseObj>{

    RequestObj toObj(RequestDto dto);

    ResponseDto toDto(ResponseObj obj);

}
