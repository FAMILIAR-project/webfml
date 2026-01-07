package fr.inria.familiar.webfml.dto;

import lombok.Data;

import java.util.List;

/**
 * DTO representing a file tree node
 */
@Data
public class FileTreeNode {
    private String label;
    private String type; // "file" or "folder"
    private boolean leaf;
    private boolean expanded;
    private List<FileTreeNode> children;
}
