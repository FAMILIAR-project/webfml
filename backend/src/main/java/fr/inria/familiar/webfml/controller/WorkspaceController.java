package fr.inria.familiar.webfml.controller;

import fr.inria.familiar.webfml.dto.FileTreeNode;
import fr.inria.familiar.webfml.service.WorkspaceService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for workspace file operations
 */
@Slf4j
@RestController
@RequestMapping("/workspace")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    /**
     * List all files in the workspace
     */
    @GetMapping("/files")
    public ResponseEntity<List<FileTreeNode>> listFiles() {
        List<FileTreeNode> files = workspaceService.listFiles();
        return ResponseEntity.ok(files);
    }

    /**
     * Load a file content
     */
    @GetMapping("/file")
    public ResponseEntity<String> loadFile(@RequestParam String filename) {
        try {
            String content = workspaceService.loadFile(filename);
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            log.error("Error loading file: {}", filename, e);
            return ResponseEntity.badRequest().body("Error loading file: " + e.getMessage());
        }
    }

    /**
     * Save file content
     */
    @PostMapping("/file")
    public ResponseEntity<?> saveFile(
            @RequestParam String filename,
            @RequestBody String content) {
        try {
            workspaceService.saveFile(filename, content);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            log.error("Error saving file: {}", filename, e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error saving file: " + e.getMessage()));
        }
    }

    /**
     * Create a new file
     */
    @PostMapping("/file/create")
    public ResponseEntity<?> createFile(@RequestParam String name) {
        try {
            workspaceService.createFile(name);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            log.error("Error creating file: {}", name, e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error creating file: " + e.getMessage()));
        }
    }

    /**
     * Delete a file
     */
    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestParam String name) {
        try {
            workspaceService.deleteFile(name);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            log.error("Error deleting file: {}", name, e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error deleting file: " + e.getMessage()));
        }
    }

    /**
     * Create a new folder
     */
    @PostMapping("/folder")
    public ResponseEntity<?> createFolder(@RequestParam String name) {
        try {
            workspaceService.createFolder(name);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            log.error("Error creating folder: {}", name, e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error creating folder: " + e.getMessage()));
        }
    }

    /**
     * Delete a folder
     */
    @DeleteMapping("/folder")
    public ResponseEntity<?> deleteFolder(@RequestParam String name) {
        try {
            workspaceService.deleteFolder(name);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            log.error("Error deleting folder: {}", name, e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error deleting folder: " + e.getMessage()));
        }
    }
}
