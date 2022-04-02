package io.github.mfvanek.spring5mvc.controllers;

import io.github.mfvanek.spring5mvc.config.WebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebConfig.class})
@WebAppConfiguration
class RequestControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void verifyTestConfiguration() {
        ServletContext servletContext = wac.getServletContext();

        assertThat(servletContext).isNotNull();
        assertThat(servletContext).isInstanceOf(MockServletContext.class);
        assertThat(wac.getBean("requestController"))
                .isNotNull()
                .isInstanceOf(RequestController.class);
    }

    @Test
    void apiDocsShouldWork() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/v2/api-docs"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("application/json", mvcResult.getResponse().getContentType());
        assertThat(mvcResult.getResponse().getContentAsString()).startsWith("{\"swagger\":\"2.0\"");
    }

    @Test
    void log() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/api/v1/demo"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("text/plain;charset=ISO-8859-1", mvcResult.getResponse().getContentType());
        assertThat(mvcResult.getResponse().getContentAsString()).startsWith("Add log entry at ");
    }

    @Test
    void logException() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/api/v1/demoException"))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andReturn();

        assertEquals("application/json", mvcResult.getResponse().getContentType());
        assertThat(mvcResult.getResponse().getContentAsString()).contains("\"message\":\"sample exception\"");
    }

    @Test
    void displayWelcomeMessage() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/api/v1/welcome"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("application/json", mvcResult.getResponse().getContentType());
        assertThat(mvcResult.getResponse().getContentAsString())
                .startsWith("{\"title\":\"Jaeger tracing demo\",\"description\":\"Welcome from Spring MVC and Embedded Jetty. ");
    }
}
