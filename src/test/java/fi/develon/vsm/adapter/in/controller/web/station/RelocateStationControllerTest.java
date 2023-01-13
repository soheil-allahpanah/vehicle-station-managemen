package fi.develon.vsm.adapter.in.controller.web.station;

import com.fasterxml.jackson.core.type.TypeReference;
import fi.develon.vsm.TestApplication;
import fi.develon.vsm.adapter.in.controller.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


public class RelocateStationControllerTest extends TestApplication {


    @Test
    void addStationToCompany() throws Exception {

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



        RelocateStationReqDto relocateRequest = RelocateStationReqDto.builder()
                .latitude(BigDecimal.valueOf(26.2137))
                .longitude(BigDecimal.valueOf(1.4671)).build();
        var relocateRes = mvc.perform(put("/api/v1/stations/"+addStation.getStationId()+"/relocate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(relocateRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        RelocateStationResDto changeParentResponse = json.readValue(relocateRes.andReturn()
                .getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(changeParentResponse.getLatitude(), equalTo(relocateRequest.getLatitude()));
        assertThat(changeParentResponse.getLongitude(), equalTo(relocateRequest.getLongitude()));
    }


}
