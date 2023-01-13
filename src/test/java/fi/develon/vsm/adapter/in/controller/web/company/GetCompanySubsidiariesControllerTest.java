package fi.develon.vsm.adapter.in.controller.web.company;

import com.fasterxml.jackson.core.type.TypeReference;
import fi.develon.vsm.TestApplication;
import fi.develon.vsm.adapter.in.controller.dto.GetCompanySubsidiariesResDto;
import fi.develon.vsm.adapter.in.controller.dto.RegisterCompanyReqDto;
import fi.develon.vsm.adapter.in.controller.dto.RenameCompanyReqDto;
import fi.develon.vsm.adapter.in.controller.dto.RenameStationResDto;
import fi.develon.vsm.domain.core.dto.GetStationsWithDistanceRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


public class GetCompanySubsidiariesControllerTest extends TestApplication {



    @Test
    void registerSeveralCompaniesAndGetQueryFormItsRoot() throws Exception {

        RegisterCompanyReqDto rootRequest = RegisterCompanyReqDto.builder()
                .identificationNumber("1000")
                .name("c0")
                .build();

        mvc.perform(post("/api/v1/companies/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(rootRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        RegisterCompanyReqDto childOne = RegisterCompanyReqDto.builder()
                .identificationNumber("1001")
                .parentIdentificationNumber("1000")
                .name("c1")
                .build();

        mvc.perform(post("/api/v1/companies/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(childOne)))
                .andExpect(MockMvcResultMatchers.status().isOk());


        RegisterCompanyReqDto childTwo = RegisterCompanyReqDto.builder()
                .identificationNumber("1002")
                .parentIdentificationNumber("1000")
                .name("c2")
                .build();

        mvc.perform(post("/api/v1/companies/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(childTwo)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        RegisterCompanyReqDto childThree = RegisterCompanyReqDto.builder()
                .identificationNumber("1003")
                .parentIdentificationNumber("1000")
                .name("c3")
                .build();

        mvc.perform(post("/api/v1/companies/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(childThree)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        var renameResponseActions = mvc.perform(get("/api/v1/companies/1000/subsidiaries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        GetCompanySubsidiariesResDto res = json.readValue(renameResponseActions.andReturn()
                .getResponse().getContentAsString(), new TypeReference<>() {
        });


        assertThat(res.getSubsidiaries().size(), equalTo(3));
    }





}
