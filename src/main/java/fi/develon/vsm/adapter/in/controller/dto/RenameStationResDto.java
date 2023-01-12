package fi.develon.vsm.adapter.in.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RenameStationResDto {

    private String ownerId;
    private String ownerName;
    private Long id;
    private String newName;

}
