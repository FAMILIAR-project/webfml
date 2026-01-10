import { useState, useEffect } from 'react'
import { FolderOpen, FileCode, Folder, ChevronRight, ChevronDown, Play, Link, Unlink, Settings, Plus, Trash2, AlertCircle, CheckCircle } from 'lucide-react'
import { projectApi, ProjectMetadata, FileTreeNode } from '@/api/client'
import './WorkspacePanel.css'

interface WorkspacePanelProps {
  featureModels: string[]
  configurations: string[]
  onDerive: (projectId: string, projectName: string, configId?: string) => void
  onLiveConfig?: (projectId: string, projectName: string, fmVariableId: string, configId?: string) => void
  onConfigureFM: (fmVariableId: string) => void
  onOpenAddProject: () => void
  onOpenFile?: (filePath: string, fileName: string, content: string) => void
  refreshTrigger?: number
}

const WorkspacePanel: React.FC<WorkspacePanelProps> = ({
  featureModels,
  configurations,
  onDerive,
  onLiveConfig,
  onConfigureFM,
  onOpenAddProject,
  onOpenFile,
  refreshTrigger,
}) => {
  const [projects, setProjects] = useState<ProjectMetadata[]>([])
  const [selectedProject, setSelectedProject] = useState<ProjectMetadata | null>(null)
  const [projectFiles, setProjectFiles] = useState<FileTreeNode[]>([])
  const [error, setError] = useState<string | null>(null)

  // File viewer
  const [viewingFile, setViewingFile] = useState<{ path: string; content: string } | null>(null)

  // Associate dialog
  const [showAssociateDialog, setShowAssociateDialog] = useState(false)
  const [selectedFM, setSelectedFM] = useState('')

  // Config selection
  const [selectedConfig, setSelectedConfig] = useState<string>('')

  useEffect(() => {
    loadProjects()
  }, [])

  // Refresh projects when refreshTrigger changes
  useEffect(() => {
    if (refreshTrigger !== undefined && refreshTrigger > 0) {
      loadProjects()
    }
  }, [refreshTrigger])

  // Reset selected config if it's no longer available
  useEffect(() => {
    if (selectedConfig && !configurations.includes(selectedConfig)) {
      setSelectedConfig('')
    }
  }, [configurations, selectedConfig])

  const loadProjects = async () => {
    setError(null)
    try {
      const list = await projectApi.list()
      setProjects(list)
      if (list.length > 0 && !selectedProject) {
        handleSelectProject(list[0])
      }
    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to load projects')
    }
  }

  const loadProjectFiles = async (projectId: string) => {
    try {
      const files = await projectApi.getFiles(projectId)
      setProjectFiles(files)
    } catch (err: any) {
      console.error('Failed to load project files:', err)
    }
  }

  const handleSelectProject = async (project: ProjectMetadata) => {
    setSelectedProject(project)
    setViewingFile(null)
    await loadProjectFiles(project.id)
  }

  const handleViewFile = async (filePath: string) => {
    if (!selectedProject) return
    try {
      const result = await projectApi.getFileContent(selectedProject.id, filePath)
      // Extract file name from path
      const fileName = filePath.split('/').pop() || filePath
      // Open in editor tab if callback provided, otherwise show in preview
      if (onOpenFile) {
        onOpenFile(filePath, fileName, result.content)
      } else {
        setViewingFile({ path: filePath, content: result.content })
      }
    } catch (err: any) {
      setError('Failed to load file')
    }
  }

  const handleAssociate = async () => {
    if (!selectedProject || !selectedFM) return
    try {
      await projectApi.associate(selectedProject.id, selectedFM)
      await loadProjects()
      setSelectedProject({ ...selectedProject, associatedFM: selectedFM })
      setShowAssociateDialog(false)
    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to associate')
    }
  }

  const handleDissociate = async () => {
    if (!selectedProject) return
    if (!confirm('Unlink feature model from this project?')) return
    try {
      await projectApi.dissociate(selectedProject.id)
      await loadProjects()
      setSelectedProject({ ...selectedProject, associatedFM: undefined })
    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to unlink')
    }
  }

  const handleDelete = async (projectId: string) => {
    if (!confirm('Delete this project?')) return
    try {
      await projectApi.delete(projectId)
      if (selectedProject?.id === projectId) {
        setSelectedProject(null)
        setProjectFiles([])
        setViewingFile(null)
      }
      await loadProjects()
    } catch (err: any) {
      setError('Failed to delete project')
    }
  }

  const handleDerive = () => {
    if (selectedProject) {
      onDerive(selectedProject.id, selectedProject.name, selectedConfig || undefined)
    }
  }

  const canDerive = selectedProject?.associatedFM && featureModels.includes(selectedProject.associatedFM)

  return (
    <div className="workspace-panel">
      <div className="workspace-header">
        <span>Workspace</span>
        <button className="add-btn" onClick={onOpenAddProject} title="Add project">
          <Plus size={14} />
        </button>
      </div>

      {error && <div className="workspace-error">{error}</div>}

      <div className="workspace-content">
        {/* Project selector */}
        <div className="project-selector">
          <select
            value={selectedProject?.id || ''}
            onChange={(e) => {
              const project = projects.find(p => p.id === e.target.value)
              if (project) handleSelectProject(project)
            }}
            className="project-select"
          >
            {projects.length === 0 && <option value="">No projects</option>}
            {projects.map(p => (
              <option key={p.id} value={p.id}>{p.name}</option>
            ))}
          </select>
          {selectedProject && (
            <button
              className="delete-project-btn"
              onClick={() => handleDelete(selectedProject.id)}
              title="Delete project"
            >
              <Trash2 size={14} />
            </button>
          )}
        </div>

        {selectedProject ? (
          <>
            {/* Status indicators */}
            <div className="project-status">
              <div className={`status-item ${selectedProject.associatedFM ? 'ok' : 'warning'}`}>
                {selectedProject.associatedFM ? (
                  <>
                    <CheckCircle size={12} />
                    <span>FM: {selectedProject.associatedFM}</span>
                    <button
                      className="unlink-btn"
                      onClick={handleDissociate}
                      title="Unlink feature model"
                    >
                      <Unlink size={10} />
                    </button>
                  </>
                ) : (
                  <>
                    <AlertCircle size={12} />
                    <span>No FM linked</span>
                  </>
                )}
              </div>
            </div>

            {/* Actions */}
            <div className="project-quick-actions">
              <button
                className="quick-action"
                onClick={() => {
                  setSelectedFM(featureModels[0] || '')
                  setShowAssociateDialog(true)
                }}
                disabled={featureModels.length === 0}
                title={featureModels.length === 0 ? 'Create a FM first' : 'Link feature model'}
              >
                <Link size={12} />
                <span>{selectedProject.associatedFM ? 'Change FM' : 'Link FM'}</span>
              </button>

              {canDerive && onLiveConfig && (
                <button
                  className="quick-action primary"
                  onClick={() => onLiveConfig(selectedProject.id, selectedProject.name, selectedProject.associatedFM!, selectedConfig || undefined)}
                  title="Live configuration with preview"
                >
                  <Play size={12} />
                  <span>Live</span>
                </button>
              )}

              {selectedProject.associatedFM && (
                <button
                  className="quick-action"
                  onClick={() => onConfigureFM(selectedProject.associatedFM!)}
                  title="Configure features only"
                >
                  <Settings size={12} />
                  <span>Configure</span>
                </button>
              )}

              {canDerive && (
                <button
                  className="quick-action"
                  onClick={handleDerive}
                  title="Derive with current config"
                >
                  <Play size={12} />
                  <span>Derive</span>
                </button>
              )}
            </div>

            {/* Config selector (if multiple configs) */}
            {configurations.length > 0 && selectedProject.associatedFM && (
              <div className="config-selector">
                <label>Config:</label>
                <select
                  value={selectedConfig}
                  onChange={(e) => setSelectedConfig(e.target.value)}
                  className="config-select"
                >
                  <option value="">(current session)</option>
                  {configurations.map(c => (
                    <option key={c} value={c}>{c}</option>
                  ))}
                </select>
                {selectedConfig && (
                  <button
                    className="unlink-btn"
                    onClick={() => setSelectedConfig('')}
                    title="Clear config selection"
                  >
                    <Unlink size={10} />
                  </button>
                )}
              </div>
            )}

            {/* File browser */}
            <div className="file-browser-compact">
              <div className="browser-label">Files</div>
              <div className="file-tree-compact">
                <FileTreeCompact
                  nodes={projectFiles}
                  onFileClick={handleViewFile}
                  selectedPath={viewingFile?.path}
                />
              </div>
            </div>

            {/* File preview */}
            {viewingFile && (
              <div className="file-preview">
                <div className="preview-header">
                  <FileCode size={12} />
                  <span>{viewingFile.path}</span>
                </div>
                <pre className="preview-content">{viewingFile.content}</pre>
              </div>
            )}
          </>
        ) : projects.length === 0 ? (
          <div className="no-projects">
            <FolderOpen size={32} />
            <p>No projects yet</p>
            <button className="add-project-btn" onClick={onOpenAddProject}>
              Add Project
            </button>
          </div>
        ) : (
          <div className="no-projects">
            <p>Select a project</p>
          </div>
        )}
      </div>

      {/* Associate dialog */}
      {showAssociateDialog && (
        <div className="mini-dialog-overlay" onClick={() => setShowAssociateDialog(false)}>
          <div className="mini-dialog" onClick={e => e.stopPropagation()}>
            <h4>Link Feature Model</h4>
            {featureModels.length === 0 ? (
              <p>Create a feature model first</p>
            ) : (
              <>
                <select
                  value={selectedFM}
                  onChange={(e) => setSelectedFM(e.target.value)}
                  className="dialog-select"
                >
                  {featureModels.map(fm => (
                    <option key={fm} value={fm}>{fm}</option>
                  ))}
                </select>
                <div className="dialog-buttons">
                  <button onClick={() => setShowAssociateDialog(false)}>Cancel</button>
                  <button className="primary" onClick={handleAssociate}>Link</button>
                </div>
              </>
            )}
          </div>
        </div>
      )}
    </div>
  )
}

// Compact file tree component
interface FileTreeCompactProps {
  nodes: FileTreeNode[]
  level?: number
  onFileClick: (path: string) => void
  selectedPath?: string
  parentPath?: string
}

const FileTreeCompact: React.FC<FileTreeCompactProps> = ({
  nodes,
  level = 0,
  onFileClick,
  selectedPath,
  parentPath = ''
}) => {
  const [expanded, setExpanded] = useState<Record<string, boolean>>(() => {
    const initial: Record<string, boolean> = {}
    nodes.forEach(n => { if (n.type === 'folder') initial[n.label] = level < 2 })
    return initial
  })

  const getFullPath = (label: string) => parentPath ? `${parentPath}/${label}` : label

  return (
    <div className="tree-level" style={{ paddingLeft: level * 12 }}>
      {nodes.map(node => {
        const fullPath = getFullPath(node.label)
        return (
          <div key={node.label}>
            <div
              className={`tree-item ${node.type} ${selectedPath === fullPath ? 'selected' : ''}`}
              onClick={() => {
                if (node.type === 'folder') {
                  setExpanded(prev => ({ ...prev, [node.label]: !prev[node.label] }))
                } else {
                  onFileClick(fullPath)
                }
              }}
            >
              {node.type === 'folder' && (
                <span className="expand-icon">
                  {expanded[node.label] ? <ChevronDown size={10} /> : <ChevronRight size={10} />}
                </span>
              )}
              {node.type === 'folder' ? <Folder size={12} /> : <FileCode size={12} />}
              <span className="item-name">{node.label}</span>
            </div>
            {node.type === 'folder' && expanded[node.label] && node.children && (
              <FileTreeCompact
                nodes={node.children}
                level={level + 1}
                onFileClick={onFileClick}
                selectedPath={selectedPath}
                parentPath={fullPath}
              />
            )}
          </div>
        )
      })}
    </div>
  )
}

export default WorkspacePanel
