package fr.inria.familiar.webfml.controller;

import fr.inria.familiar.webfml.dto.FileTreeNode;
import fr.inria.familiar.webfml.dto.ProjectMetadata;
import fr.inria.familiar.webfml.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * REST controller for project management operations
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * Upload a project from ZIP file
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadZip(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "File is empty"));
            }

            ProjectMetadata metadata = projectService.uploadZip(file, name);
            return ResponseEntity.ok(metadata);
        } catch (Exception e) {
            log.error("Error uploading project", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Register a project from filesystem path
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerFromPath(
            @RequestParam("path") String path,
            @RequestParam("name") String name) {
        try {
            ProjectMetadata metadata = projectService.registerFromPath(path, name);
            return ResponseEntity.ok(metadata);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error registering project", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * List all projects
     */
    @GetMapping("/list")
    public ResponseEntity<?> listProjects() {
        try {
            List<ProjectMetadata> projects = projectService.listProjects();
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            log.error("Error listing projects", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get project metadata
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProject(@PathVariable String id) {
        try {
            ProjectMetadata metadata = projectService.getProject(id);
            return ResponseEntity.ok(metadata);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error getting project", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Delete a project
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable String id) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting project", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get project file tree
     */
    @GetMapping("/{id}/files")
    public ResponseEntity<?> getProjectFiles(@PathVariable String id) {
        try {
            List<FileTreeNode> files = projectService.getProjectFiles(id);
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            log.error("Error getting project files", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get file content
     */
    @GetMapping("/{id}/file")
    public ResponseEntity<?> getFileContent(
            @PathVariable String id,
            @RequestParam("path") String path) {
        try {
            String content = projectService.getFileContent(id, path);
            return ResponseEntity.ok(Map.of("content", content, "path", path));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error getting file content", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Associate project with feature model
     */
    @PostMapping("/{id}/associate")
    public ResponseEntity<?> associateWithFM(
            @PathVariable String id,
            @RequestParam("fmVariableId") String fmVariableId) {
        try {
            ProjectMetadata metadata = projectService.associateWithFM(id, fmVariableId);
            return ResponseEntity.ok(metadata);
        } catch (Exception e) {
            log.error("Error associating project with FM", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Dissociate project from feature model (clear FM binding)
     */
    @PostMapping("/{id}/dissociate")
    public ResponseEntity<?> dissociateFromFM(@PathVariable String id) {
        try {
            ProjectMetadata metadata = projectService.dissociateFromFM(id);
            return ResponseEntity.ok(metadata);
        } catch (Exception e) {
            log.error("Error dissociating project from FM", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }
}
