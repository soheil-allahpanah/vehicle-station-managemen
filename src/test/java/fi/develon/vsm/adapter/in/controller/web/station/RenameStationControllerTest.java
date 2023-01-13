package fi.develon.vsm.adapter.in.controller.web.station;

import com.fasterxml.jackson.core.type.TypeReference;
import fi.develon.vsm.TestApplication;
import fi.develon.vsm.adapter.in.controller.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


public class RenameStationControllerTest extends TestApplication {


    @Test
    void renameStation() throws Exception {

        RegisterCompanyReqDto rootRequest = RegisterCompanyReqDto.builder()
                .name("c0")
                .identificationNumber("1000")
                .build();

        mvc.perform(post("/api/v1/companies/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(rootRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());




        AddStationToCompanyReqDto addStationToCompanyReqDto = new AddStationToCompanyReqDto("st01"
                , BigDecimal.valueOf(27.2137)
                , BigDecimal.valueOf(2.4671)
        );
        var res = mvc.perform(post("/api/v1/companies/1000/addStation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(addStationToCompanyReqDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        AddStationToCompanyResDto addStation = json.readValue(res.andReturn()
                .getResponse().getContentAsString(), new TypeReference<>() {
        });


        RenameStationReqDto renameRequest = RenameStationReqDto.builder()
                .newName("newSt01")
                .build();
        var renameRes = mvc.perform(put("/api/v1/stations/"+addStation.getStationId()+"/rename")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(renameRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        RenameStationResDto renameStationResDto = json.readValue(renameRes.andReturn()
                .getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(renameStationResDto.getNewName(), equalTo(renameRequest.getNewName()));
    }


}
