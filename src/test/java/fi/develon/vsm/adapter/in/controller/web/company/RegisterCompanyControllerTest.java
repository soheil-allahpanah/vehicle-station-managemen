package fi.develon.vsm.adapter.in.controller.web.company;

import com.fasterxml.jackson.core.type.TypeReference;
import fi.develon.vsm.TestApplication;
import fi.develon.vsm.adapter.in.controller.dto.RegisterCompanyReqDto;
import fi.develon.vsm.adapter.in.controller.dto.RegisterCompanyResDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


public class RegisterCompanyControllerTest extends TestApplication {

    @Test
    void givenRequestWhenRequestIsInvalidShouldReturnWithBadRequest() throws Exception {

        RegisterCompanyReqDto request = RegisterCompanyReqDto.builder()
                .identificationNumber("10001")
                .build();

        mvc.perform(post("/api/v1/companies/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenRequestWhenParentIsNullShouldSave() throws Exception {

        RegisterCompanyReqDto request = RegisterCompanyReqDto.builder()
                .name("c0")
                .identificationNumber("10001")
                .build();

        var resultActions = mvc.perform(post("/api/v1/companies/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        RegisterCompanyResDto response = json.readValue(resultActions.andReturn()
                .getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(request.getIdentificationNumber(), equalTo(response.getIdentificationNumber()));
        assertThat(request.getName(), equalTo(response.getName()));
    }


    @Test
    void givenRequestWhenParentIsNotNullAndNotExistShouldReturnNotFound() throws Exception {

        RegisterCompanyReqDto request = RegisterCompanyReqDto.builder()
                .name("c0")
                .identificationNumber("10001")
                .parentIdentificationNumber("10000")
                .build();

        mvc.perform(post("/api/v1/companies/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }


}
