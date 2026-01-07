package fr.inria.familiar.webfml.dto;

import java.util.List;

/**
 * DTO representing a file tree node
 */
public class FileTreeNode {
    private String label;
    private String type; // "file" or "folder"
    private boolean leaf;
    private boolean expanded;
    private List<FileTreeNode> children;

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isLeaf() { return leaf; }
    public void setLeaf(boolean leaf) { this.leaf = leaf; }
    public boolean isExpanded() { return expanded; }
    public void setExpanded(boolean expanded) { this.expanded = expanded; }
    public List<FileTreeNode> getChildren() { return children; }
    public void setChildren(List<FileTreeNode> children) { this.children = children; }
}
