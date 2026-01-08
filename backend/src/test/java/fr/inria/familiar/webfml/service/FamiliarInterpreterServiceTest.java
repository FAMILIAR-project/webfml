package fr.inria.familiar.webfml.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for FamiliarInterpreterService, specifically the feature model structure extraction.
 */
class FamiliarInterpreterServiceTest {

    private FamiliarInterpreterService service;
    private static final String TEST_SESSION = "test-session";

    @BeforeEach
    void setUp() {
        service = new FamiliarInterpreterService();
    }

    @Test
    void testFeatureModelStructure_ComplexFM() throws Exception {
        // FM with mandatory, optional, XOR groups, OR groups, and MUTEX groups
        // A: C B [D]        -> C and B mandatory, D optional
        // D: E [F] (JJ|IN)  -> E mandatory, F optional, JJ|IN is XOR group
        // F: (H|G) (T|U)+ (W|Y)? (K|O)?  -> H|G is XOR, T|U is OR, W|Y and K|O are MUTEX
        String command = "fm2 = FM(A: C B [D]; D: E [F] (JJ|IN); F: (H|G) (T|U)+ (W|Y)? (K|O)?;)";

        service.evalPrompt(TEST_SESSION, command);
        Map<String, Object> structure = service.getFeatureModelStructure(TEST_SESSION, "fm2");

        assertNotNull(structure);
        assertEquals("fm2", structure.get("variableId"));
        assertEquals("A", structure.get("root"));

        @SuppressWarnings("unchecked")
        Map<String, Object> tree = (Map<String, Object>) structure.get("tree");
        assertNotNull(tree);
        assertEquals("A", tree.get("name"));

        // Check root A has mandatory children B and C, and optional D
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> mandatoryA = (List<Map<String, Object>>) tree.get("mandatory");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> optionalA = (List<Map<String, Object>>) tree.get("optional");

        assertEquals(2, mandatoryA.size(), "A should have 2 mandatory children (B and C)");
        assertEquals(1, optionalA.size(), "A should have 1 optional child (D)");

        // Find node D in optional children
        Map<String, Object> nodeD = optionalA.get(0);
        assertEquals("D", nodeD.get("name"));

        // D should have mandatory E, optional F, and XOR group (JJ|IN)
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> mandatoryD = (List<Map<String, Object>>) nodeD.get("mandatory");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> optionalD = (List<Map<String, Object>>) nodeD.get("optional");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> xorGroupsD = (List<Map<String, Object>>) nodeD.get("xorGroups");

        assertEquals(1, mandatoryD.size(), "D should have 1 mandatory child (E)");
        assertEquals(1, optionalD.size(), "D should have 1 optional child (F)");
        assertEquals(1, xorGroupsD.size(), "D should have 1 XOR group (JJ|IN)");

        // Verify XOR group (JJ|IN) under D
        Map<String, Object> xorGroupD = xorGroupsD.get(0);
        assertEquals("xor", xorGroupD.get("type"));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> xorMembersD = (List<Map<String, Object>>) xorGroupD.get("members");
        assertEquals(2, xorMembersD.size(), "XOR group should have 2 members (JJ and IN)");

        // Find node F
        Map<String, Object> nodeF = optionalD.get(0);
        assertEquals("F", nodeF.get("name"));

        // F should have:
        // - 1 XOR group (H|G)
        // - 1 OR group (T|U)+
        // - 2 MUTEX groups (W|Y)? and (K|O)?
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> xorGroupsF = (List<Map<String, Object>>) nodeF.get("xorGroups");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> orGroupsF = (List<Map<String, Object>>) nodeF.get("orGroups");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> mutexGroupsF = (List<Map<String, Object>>) nodeF.get("mutexGroups");

        assertEquals(1, xorGroupsF.size(), "F should have 1 XOR group (H|G)");
        assertEquals(1, orGroupsF.size(), "F should have 1 OR group (T|U)+");
        assertEquals(2, mutexGroupsF.size(), "F should have 2 MUTEX groups (W|Y)? and (K|O)?");

        // Verify XOR group (H|G) under F
        Map<String, Object> xorGroupF = xorGroupsF.get(0);
        assertEquals("xor", xorGroupF.get("type"));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> xorMembersF = (List<Map<String, Object>>) xorGroupF.get("members");
        assertEquals(2, xorMembersF.size(), "XOR group (H|G) should have 2 members");

        // Verify OR group (T|U)+ under F
        Map<String, Object> orGroupF = orGroupsF.get(0);
        assertEquals("or", orGroupF.get("type"));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> orMembersF = (List<Map<String, Object>>) orGroupF.get("members");
        assertEquals(2, orMembersF.size(), "OR group (T|U)+ should have 2 members");

        // Verify MUTEX groups under F
        for (Map<String, Object> mutexGroup : mutexGroupsF) {
            assertEquals("mutex", mutexGroup.get("type"));
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> mutexMembers = (List<Map<String, Object>>) mutexGroup.get("members");
            assertEquals(2, mutexMembers.size(), "Each MUTEX group should have 2 members");
        }
    }

    @Test
    void testFeatureModelStructure_MultipleXorGroupsStayDistinct() throws Exception {
        // Two separate XOR groups under Root should stay distinct
        String command = "fm3 = FM(Root: (A|B) (C|D|E);)";

        service.evalPrompt(TEST_SESSION, command);
        Map<String, Object> structure = service.getFeatureModelStructure(TEST_SESSION, "fm3");

        @SuppressWarnings("unchecked")
        Map<String, Object> tree = (Map<String, Object>) structure.get("tree");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> xorGroups = (List<Map<String, Object>>) tree.get("xorGroups");

        assertEquals(2, xorGroups.size(), "Root should have 2 distinct XOR groups");

        // Verify each group has the correct number of members
        boolean foundGroup2 = false;
        boolean foundGroup3 = false;
        for (Map<String, Object> group : xorGroups) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> members = (List<Map<String, Object>>) group.get("members");
            if (members.size() == 2) {
                foundGroup2 = true;
            } else if (members.size() == 3) {
                foundGroup3 = true;
            }
        }
        assertTrue(foundGroup2, "Should have a group with 2 members (A|B)");
        assertTrue(foundGroup3, "Should have a group with 3 members (C|D|E)");
    }

    @Test
    void testFeatureModelStructure_OrAndXorGroupsSeparate() throws Exception {
        // OR group and XOR group should be in different arrays
        String command = "fm4 = FM(Root: (A|B)+ (C|D);)";

        service.evalPrompt(TEST_SESSION, command);
        Map<String, Object> structure = service.getFeatureModelStructure(TEST_SESSION, "fm4");

        @SuppressWarnings("unchecked")
        Map<String, Object> tree = (Map<String, Object>) structure.get("tree");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> xorGroups = (List<Map<String, Object>>) tree.get("xorGroups");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> orGroups = (List<Map<String, Object>>) tree.get("orGroups");

        assertEquals(1, xorGroups.size(), "Root should have 1 XOR group (C|D)");
        assertEquals(1, orGroups.size(), "Root should have 1 OR group (A|B)+");

        // Verify types
        assertEquals("xor", xorGroups.get(0).get("type"));
        assertEquals("or", orGroups.get(0).get("type"));
    }

    @Test
    void testFeatureModelStructure_SimpleFM() throws Exception {
        // Simple FM with mandatory and optional children
        String command = "fm1 = FM(A: B [C];)";

        service.evalPrompt(TEST_SESSION, command);
        Map<String, Object> structure = service.getFeatureModelStructure(TEST_SESSION, "fm1");

        assertNotNull(structure);
        assertEquals("A", structure.get("root"));

        @SuppressWarnings("unchecked")
        Map<String, Object> tree = (Map<String, Object>) structure.get("tree");
        assertEquals("A", tree.get("name"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> mandatory = (List<Map<String, Object>>) tree.get("mandatory");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> optional = (List<Map<String, Object>>) tree.get("optional");

        assertEquals(1, mandatory.size(), "A should have 1 mandatory child (B)");
        assertEquals(1, optional.size(), "A should have 1 optional child (C)");
        assertEquals("B", mandatory.get(0).get("name"));
        assertEquals("C", optional.get(0).get("name"));
    }

    @Test
    void testFeatureModelStructure_WithConstraints() throws Exception {
        // FM with cross-tree constraints
        String command = "fmC = FM(A: B [C] [D] [E]; B -> C; D -> E;)";

        service.evalPrompt(TEST_SESSION, command);
        Map<String, Object> structure = service.getFeatureModelStructure(TEST_SESSION, "fmC");

        assertNotNull(structure);

        @SuppressWarnings("unchecked")
        List<String> constraints = (List<String>) structure.get("constraints");

        assertNotNull(constraints, "Constraints should not be null");
        assertEquals(2, constraints.size(), "Should have 2 constraints");

        // Constraints are sorted alphabetically
        assertTrue(constraints.contains("(B -> C)"), "Should contain B implies C constraint");
        assertTrue(constraints.contains("(D -> E)"), "Should contain D implies E constraint");
    }

    @Test
    void testFeatureModelStructure_NoConstraints() throws Exception {
        // FM without constraints
        String command = "fmNoC = FM(A: B [C];)";

        service.evalPrompt(TEST_SESSION, command);
        Map<String, Object> structure = service.getFeatureModelStructure(TEST_SESSION, "fmNoC");

        @SuppressWarnings("unchecked")
        List<String> constraints = (List<String>) structure.get("constraints");

        assertNotNull(constraints, "Constraints list should not be null");
        assertTrue(constraints.isEmpty(), "Constraints list should be empty for FM without constraints");
    }
}
