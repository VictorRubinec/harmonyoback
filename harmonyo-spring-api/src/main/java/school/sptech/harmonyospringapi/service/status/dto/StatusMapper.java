package school.sptech.harmonyospringapi.service.status.dto;

import school.sptech.harmonyospringapi.domain.Status;

import java.util.ArrayList;

public class StatusMapper {

    public static Status of(StatusCriacaoDto statusCriacaoDto) {
        Status status = new Status();

        status.setDescricao(statusCriacaoDto.getDescricao());

        return status;
    }

    public static StatusExibicaoDto ofStatusExibicaoDto(Status status) {
        StatusExibicaoDto statusExibicaoDto = new StatusExibicaoDto();

        statusExibicaoDto.setId(status.getId());
        statusExibicaoDto.setDescricao(status.getDescricao());

        return statusExibicaoDto;
    }
}
