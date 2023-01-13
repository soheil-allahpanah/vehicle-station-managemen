package fi.develon.vsm.adapter.in.controller.web.company;

import com.fasterxml.jackson.core.type.TypeReference;
import fi.develon.vsm.TestApplication;
import fi.develon.vsm.adapter.in.controller.dto.RegisterCompanyReqDto;
import fi.develon.vsm.adapter.in.controller.dto.UnregisterCompanyReqDto;
import fi.develon.vsm.adapter.in.controller.dto.UnregisterCompanyResDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


public class UnregisterCompanyControllerTest extends TestApplication {



    @Test
    void registerSeveralCompaniesAndUnregisterTheRootOne() throws Exception {

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
                .identificationNumber("10021")
                .parentIdentificationNumber("1002")
                .name("c21")
                .build();

        mvc.perform(post("/api/v1/companies/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(childThree)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        UnregisterCompanyReqDto unregisterCompanyReqDto = new UnregisterCompanyReqDto(false);
        var unregisteredResponse = mvc.perform(delete("/api/v1/companies/1000/unregister")
                        .content(json.writeValueAsString(unregisterCompanyReqDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        UnregisterCompanyResDto res = json.readValue(unregisteredResponse.andReturn()
                .getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertThat(res.getIdentificationNumber(), equalTo("1000"));
        assertThat(res.getUnregisteredSubsidiaries(), is(nullValue()));
    }

    @Test
    void registerSeveralCompaniesAndUnregisterTheRootOneWithUnregisteringItsSubsidiaries() throws Exception {

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
                .identificationNumber("10021")
                .parentIdentificationNumber("1002")
                .name("c21")
                .build();

        mvc.perform(post("/api/v1/companies/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(childThree)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        UnregisterCompanyReqDto unregisterCompanyReqDto = new UnregisterCompanyReqDto(true);
        var unregisteredResponse = mvc.perform(delete("/api/v1/companies/1000/unregister")
                        .content(json.writeValueAsString(unregisterCompanyReqDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        UnregisterCompanyResDto res = json.readValue(unregisteredResponse.andReturn()
                .getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertThat(res.getIdentificationNumber(), equalTo("1000"));
        assertThat(res.getUnregisteredSubsidiaries().size(), equalTo(3));
    }




}
