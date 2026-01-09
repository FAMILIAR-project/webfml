package fr.inria.familiar.webfml.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for FamiliarController REST endpoints.
 * Tests the configs formatting workaround and API responses.
 */
@SpringBootTest
@AutoConfigureMockMvc
class FamiliarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        // Each test gets a fresh session with a unique ID
        session = new MockHttpSession();
    }

    // ========== Configs Formatting Tests ==========

    @Test
    void evalPrompt_ConfigsFormattedWithCommas() throws Exception {
        // First create a feature model
        mockMvc.perform(post("/familiar/eval-prompt")
                        .param("command", "fm1 = FM(A: [B] [C];)")
                        .session(session))
                .andExpect(status().isOk());

        // Get configs - should be formatted with commas instead of semicolons
        mockMvc.perform(post("/familiar/eval-prompt")
                        .param("command", "cf = configs fm1")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastVar", containsString("cf =")))
                .andExpect(jsonPath("$.lastVar", containsString(",")))
                .andExpect(jsonPath("$.lastVar", not(containsString(";"))));
    }

    @Test
    void evalPrompt_StringNotFormattedAsConfigs() throws Exception {
        // Create a string that looks like configs but should NOT be formatted
        mockMvc.perform(post("/familiar/eval-prompt")
                        .param("command", "str1 = \"{{A;B};{C;D}}\"")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastVar", containsString("str1 =")))
                // String value should retain semicolons
                .andExpect(jsonPath("$.lastVar", containsString(";")));
    }

    @Test
    void evalPrompt_FeatureModelNotAffected() throws Exception {
        // Feature model value uses commas naturally, should not be affected
        mockMvc.perform(post("/familiar/eval-prompt")
                        .param("command", "fm1 = FM(A: B [C];)")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastVar", containsString("fm1 =")))
                // FM uses semicolons in its syntax, those should remain
                .andExpect(jsonPath("$.lastVar", containsString(";")));
    }

    @Test
    void getVariableInfo_ConfigsTypeIsSet() throws Exception {
        // Create FM and get configs
        mockMvc.perform(post("/familiar/eval-prompt")
                        .param("command", "fm1 = FM(A: [B];)")
                        .session(session))
                .andExpect(status().isOk());

        mockMvc.perform(post("/familiar/eval-prompt")
                        .param("command", "cf = configs fm1")
                        .session(session))
                .andExpect(status().isOk());

        // Get variable info - type should be "Set" (not "Configs")
        mockMvc.perform(get("/familiar/variable/cf/info")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("Set")))
                .andExpect(jsonPath("$.value", containsString(",")))
                .andExpect(jsonPath("$.value", not(containsString(";"))));
    }

    @Test
    void getVariableInfo_FeatureModelType() throws Exception {
        mockMvc.perform(post("/familiar/eval-prompt")
                        .param("command", "fm1 = FM(A: B [C];)")
                        .session(session))
                .andExpect(status().isOk());

        mockMvc.perform(get("/familiar/variable/fm1/info")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("FeatureModel")));
    }

    // ========== Configurations Endpoint Tests ==========

    @Test
    void getConfigurations_ReturnsTableData() throws Exception {
        mockMvc.perform(post("/familiar/eval-prompt")
                        .param("command", "fm1 = FM(A: [B] [C];)")
                        .session(session))
                .andExpect(status().isOk());

        mockMvc.perform(get("/familiar/fm/fm1/configs")
                        .param("limit", "100")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.variableId", is("fm1")))
                .andExpect(jsonPath("$.features", hasItems("A", "B", "C")))
                .andExpect(jsonPath("$.configurations", hasSize(4)))
                .andExpect(jsonPath("$.totalCount", is(4)));
    }

    @Test
    void getConfigurations_RespectsLimit() throws Exception {
        mockMvc.perform(post("/familiar/eval-prompt")
                        .param("command", "fm1 = FM(A: [B] [C] [D] [E];)")
                        .session(session))
                .andExpect(status().isOk());

        // Without limit: 16 configs (2^4), with limit: 3
        mockMvc.perform(get("/familiar/fm/fm1/configs")
                        .param("limit", "3")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.configurations", hasSize(lessThanOrEqualTo(3))));
    }

    @Test
    void getConfigurations_NotAFeatureModel() throws Exception {
        mockMvc.perform(post("/familiar/eval-prompt")
                        .param("command", "str1 = \"not a fm\"")
                        .session(session))
                .andExpect(status().isOk());

        mockMvc.perform(get("/familiar/fm/str1/configs")
                        .session(session))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msgError", containsString("not a feature model")));
    }

    // ========== Analysis Endpoint Tests ==========

    @Test
    void analyzeFeatureModel_ValidFM() throws Exception {
        mockMvc.perform(post("/familiar/eval-prompt")
                        .param("command", "fm1 = FM(A: B [C];)")
                        .session(session))
                .andExpect(status().isOk());

        mockMvc.perform(get("/familiar/fm/fm1/analyze")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.variableId", is("fm1")))
                .andExpect(jsonPath("$.isValid", is(true)))
                .andExpect(jsonPath("$.deadFeatures", hasSize(0)))
                .andExpect(jsonPath("$.falseOptionals", hasSize(0)));
    }

    @Test
    void analyzeFeatureModel_WithDeadFeature() throws Exception {
        // B is dead: requires C, but C excludes B
        mockMvc.perform(post("/familiar/eval-prompt")
                        .param("command", "fm1 = FM(A: [B] [C]; B -> C; C -> !B;)")
                        .session(session))
                .andExpect(status().isOk());

        mockMvc.perform(get("/familiar/fm/fm1/analyze")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isValid", is(true)))
                .andExpect(jsonPath("$.deadFeatures", hasItem("B")));
    }

    @Test
    void analyzeFeatureModel_WithFalseOptional() throws Exception {
        // C is optional but B requires it
        mockMvc.perform(post("/familiar/eval-prompt")
                        .param("command", "fm1 = FM(A: B [C]; B -> C;)")
                        .session(session))
                .andExpect(status().isOk());

        mockMvc.perform(get("/familiar/fm/fm1/analyze")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isValid", is(true)))
                .andExpect(jsonPath("$.falseOptionals", hasItem("C")));
    }
}
