package fi.develon.vsm.adapter.in.controller.web.station;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import fi.develon.vsm.TestApplication;
import fi.develon.vsm.adapter.in.controller.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


public class GetStationWithDistanceControllerTest extends TestApplication {


    @Test
    void createCompaniesAndTheirStationsThenQueryStation() throws Exception {

        RegisterCompanyReqDto rootRequest = RegisterCompanyReqDto.builder()
                .name("c0")
                .identificationNumber("1000")
                .build();
        RegisterCompanyReqDto childOne = RegisterCompanyReqDto.builder()
                .name("c1")
                .parentIdentificationNumber("1000")
                .identificationNumber("1001")
                .build();
        RegisterCompanyReqDto childTwo = RegisterCompanyReqDto.builder()
                .name("c2")
                .parentIdentificationNumber("1000")
                .identificationNumber("1002")
                .build();

        RegisterCompanyReqDto childTwoOne = RegisterCompanyReqDto.builder()
                .name("c21")
                .parentIdentificationNumber("1002")
                .identificationNumber("10021")
                .build();
        RegisterCompanyReqDto childTwoTwo = RegisterCompanyReqDto.builder()
                .name("c22")
                .parentIdentificationNumber("1002")
                .identificationNumber("10022")
                .build();

        createCompany(rootRequest);
        createCompany(childOne);
        createCompany(childTwo);
        createCompany(childTwoOne);
        createCompany(childTwoTwo);


        AddStationToCompanyReqDto stationOneOnRoot = new AddStationToCompanyReqDto("c0st01"
                , BigDecimal.valueOf(0.2137)
                , BigDecimal.valueOf(0.4671)
        );
        AddStationToCompanyReqDto stationTwoOnRoot = new AddStationToCompanyReqDto("c0st02"
                , BigDecimal.valueOf(1.2137)
                , BigDecimal.valueOf(1.4671)
        );
        AddStationToCompanyReqDto stationOneOnChildOne = new AddStationToCompanyReqDto("c1st01"
                , BigDecimal.valueOf(3.2137)
                , BigDecimal.valueOf(3.4671)
        );
        AddStationToCompanyReqDto stationTwoOnChildOne = new AddStationToCompanyReqDto("c1st02"
                , BigDecimal.valueOf(2.2137)
                , BigDecimal.valueOf(2.4671)
        );

        AddStationToCompanyReqDto stationOneOnChildTwoOne = new AddStationToCompanyReqDto("c21st01"
                , BigDecimal.valueOf(40.2137)
                , BigDecimal.valueOf(40.4671)
        );
        AddStationToCompanyReqDto stationOneOnChildTwoTwo = new AddStationToCompanyReqDto("c22st01"
                , BigDecimal.valueOf(15.2137)
                , BigDecimal.valueOf(15.4671)
        );

        addStation(rootRequest.getIdentificationNumber(), stationOneOnRoot);
        addStation(rootRequest.getIdentificationNumber(), stationTwoOnRoot);
        addStation(childOne.getIdentificationNumber(), stationOneOnChildOne);
        addStation(childOne.getIdentificationNumber(), stationTwoOnChildOne);
        addStation(childTwoOne.getIdentificationNumber(), stationOneOnChildTwoOne);
        addStation(childTwoTwo.getIdentificationNumber(), stationOneOnChildTwoTwo);




        var getStationRequest = mvc.perform(get("/api/v1/companies/" + rootRequest.getIdentificationNumber() + "/nearbyStation?"
                +"lat=0.2137&long=0.4671")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
        List<GetStationsWithDistanceResDto> stations = json.readValue(getStationRequest.andReturn()
                .getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(stations.size(), equalTo(6));
        assertThat(stations.get(0).getStationName(), equalTo("c0st01"));
        assertThat(stations.get(1).getStationName(), equalTo("c0st02"));
        assertThat(stations.get(2).getStationName(), equalTo("c1st02"));
        assertThat(stations.get(3).getStationName(), equalTo("c1st01"));
        assertThat(stations.get(4).getStationName(), equalTo("c22st01"));
        assertThat(stations.get(5).getStationName(), equalTo("c21st01"));
    }

    private void createCompany(RegisterCompanyReqDto dto) throws Exception {
        mvc.perform(post("/api/v1/companies/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void addStation(String companyIdentificationNumber, AddStationToCompanyReqDto dto) throws Exception {
        mvc.perform(post("/api/v1/companies/1000/addStation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

}
