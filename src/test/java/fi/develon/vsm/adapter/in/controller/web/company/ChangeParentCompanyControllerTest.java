package fi.develon.vsm.adapter.in.controller.web.company;

import com.fasterxml.jackson.core.type.TypeReference;
import fi.develon.vsm.TestApplication;
import fi.develon.vsm.adapter.in.controller.dto.ChangeParentOfCompanyReqDto;
import fi.develon.vsm.adapter.in.controller.dto.ChangeParentOfCompanyResDto;
import fi.develon.vsm.adapter.in.controller.dto.RegisterCompanyReqDto;
import fi.develon.vsm.adapter.in.controller.dto.RegisterCompanyResDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


public class ChangeParentCompanyControllerTest extends TestApplication {


    @Test
    void saveTwoParentCompanyAndOneChildAndChangeParent() throws Exception {

        RegisterCompanyReqDto rootRequest = RegisterCompanyReqDto.builder()
                .name("c0")
                .identificationNumber("1000")
                .build();

        mvc.perform(post("/api/v1/companies/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(rootRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        RegisterCompanyReqDto root1Request = RegisterCompanyReqDto.builder()
                .name("c00")
                .identificationNumber("10000")
                .build();

        mvc.perform(post("/api/v1/companies/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(root1Request)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        RegisterCompanyReqDto child1 = RegisterCompanyReqDto.builder()
                .name("c01")
                .parentIdentificationNumber("1000")
                .identificationNumber("10001")
                .build();

        mvc.perform(post("/api/v1/companies/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(child1)))
                .andExpect(MockMvcResultMatchers.status().isOk());


        ChangeParentOfCompanyReqDto changeParentOfCompanyReqDto = new ChangeParentOfCompanyReqDto(
                "10000"
        );
        var changeParentResponseActions = mvc.perform(put("/api/v1/companies/1000/changeParent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(changeParentOfCompanyReqDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        ChangeParentOfCompanyResDto changeParentResponse = json.readValue(changeParentResponseActions.andReturn()
                .getResponse().getContentAsString(), new TypeReference<>() {
        });


        assertThat(changeParentResponse.getParentIdentificationNumber(), equalTo(changeParentOfCompanyReqDto.getNewParentIdentificationNumber()));
        assertThat(changeParentResponse.getParentName(), equalTo(root1Request.getName()));
    }


}
