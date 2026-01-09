package fr.inria.familiar.webfml.controller;

import fr.inria.familiar.webfml.dto.DerivationResult;
import fr.inria.familiar.webfml.dto.DerivedFile;
import fr.inria.familiar.webfml.service.DerivationService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for project derivation operations
 */
@RestController
@RequestMapping("/derivation")
public class DerivationController {

    private static final Logger log = LoggerFactory.getLogger(DerivationController.class);

    private final DerivationService derivationService;

    public DerivationController(DerivationService derivationService) {
        this.derivationService = derivationService;
    }

    /**
     * Derive a variant from a project using current configuration or a specific config variable
     */
    @PostMapping("/derive")
    public ResponseEntity<?> derive(
            @RequestParam("projectId") String projectId,
            @RequestParam(value = "configId", required = false) String configId,
            HttpSession session) {
        try {
            String sessionId = session.getId();
            log.info("Deriving variant for project {} in session {} with configId {}", projectId, sessionId, configId);

            DerivationResult result = derivationService.derive(sessionId, projectId, configId);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error deriving variant", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Preview a single file derivation
     */
    @PostMapping("/preview")
    public ResponseEntity<?> previewFile(
            @RequestParam("projectId") String projectId,
            @RequestParam("filePath") String filePath,
            HttpSession session) {
        try {
            String sessionId = session.getId();
            DerivedFile derivedFile = derivationService.previewFile(sessionId, projectId, filePath);
            return ResponseEntity.ok(derivedFile);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error previewing file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Download derived variant as ZIP
     */
    @GetMapping("/download")
    public ResponseEntity<?> downloadZip(
            @RequestParam("projectId") String projectId,
            HttpSession session) {
        try {
            String sessionId = session.getId();
            byte[] zipBytes = derivationService.downloadZip(sessionId, projectId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "derived-variant.zip");

            return ResponseEntity.ok()
                .headers(headers)
                .body(zipBytes);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error downloading derived variant", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }
}
