package fi.develon.vsm.adapter.in.controller.web.company;

import com.fasterxml.jackson.core.type.TypeReference;
import fi.develon.vsm.TestApplication;
import fi.develon.vsm.adapter.in.controller.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


public class RenameCompanyControllerTest extends TestApplication {



    @Test
    void registerCompanyAndChangeItsName() throws Exception {

        RegisterCompanyReqDto rootRequest = RegisterCompanyReqDto.builder()
                .identificationNumber("1000")
                .name("c1")
                .build();

        mvc.perform(post("/api/v1/companies/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(rootRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());


        RenameCompanyReqDto renameRequest = new RenameCompanyReqDto(
                "c2"
        );
        var renameResponseActions = mvc.perform(put("/api/v1/companies/1000/rename")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(renameRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        RenameCompanyResDto res = json.readValue(renameResponseActions.andReturn()
                .getResponse().getContentAsString(), new TypeReference<>() {
        });


        assertThat(res.getName(), equalTo(renameRequest.getNewName()));
    }





}
