package com.remote.submission.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remote.submission.model.CodeSubmission;
import com.remote.submission.service.CodeService;

@WebMvcTest(CodeController.class)
class CodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CodeService codeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSubmitCode() throws Exception {
        CodeSubmission submission = new CodeSubmission();
        submission.setCodeContent("print('Hello, World!')".getBytes());
        submission.setLanguage("python");
        submission.setUserId(1);

        when(codeService.handleCodeSubmission(any(CodeSubmission.class))).thenReturn(submission);

        mockMvc.perform(post("/submission/code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submission)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.language").value("python"))
                .andExpect(jsonPath("$.userId").value(1));
    }
}