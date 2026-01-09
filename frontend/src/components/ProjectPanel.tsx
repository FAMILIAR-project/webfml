import { useState, useEffect, useRef } from 'react'
import { X, Upload, FolderOpen, Trash2, Link, FileCode, Folder, ChevronRight, ChevronDown, Play, Eye, AlertCircle, CheckCircle, Settings } from 'lucide-react'
import { projectApi, ProjectMetadata, FileTreeNode } from '@/api/client'
import './ProjectPanel.css'

interface ProjectPanelProps {
  onClose: () => void
  featureModels: string[]
  onAssociate?: (projectId: string, fmVariableId: string) => void
  onDerive?: (projectId: string, projectName: string) => void
  onConfigureFM?: (fmVariableId: string) => void
  onProjectAdded?: () => void
}

const ProjectPanel: React.FC<ProjectPanelProps> = ({ onClose, featureModels, onAssociate, onDerive, onConfigureFM, onProjectAdded }) => {
  const [projects, setProjects] = useState<ProjectMetadata[]>([])
  const [selectedProject, setSelectedProject] = useState<ProjectMetadata | null>(null)
  const [projectFiles, setProjectFiles] = useState<FileTreeNode[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [activeTab, setActiveTab] = useState<'list' | 'upload'>('list')

  // File viewer state
  const [viewingFile, setViewingFile] = useState<{ path: string; content: string } | null>(null)
  const [loadingFile, setLoadingFile] = useState(false)

  // Upload form state
  const [uploadName, setUploadName] = useState('')
  const [uploadFile, setUploadFile] = useState<File | null>(null)
  const [registerPath, setRegisterPath] = useState('')
  const [registerName, setRegisterName] = useState('')
  const fileInputRef = useRef<HTMLInputElement>(null)

  // Associate dialog
  const [showAssociateDialog, setShowAssociateDialog] = useState(false)
  const [associateProjectId, setAssociateProjectId] = useState<string | null>(null)
  const [selectedFM, setSelectedFM] = useState<string>('')

  useEffect(() => {
    loadProjects()
  }, [])

  const loadProjects = async () => {
    setLoading(true)
    setError(null)
    try {
      const list = await projectApi.list()
      setProjects(list)
      // Auto-select first project if none selected
      if (list.length > 0 && !selectedProject) {
        handleSelectProject(list[0])
      }
    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to load projects')
    } finally {
      setLoading(false)
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
    setLoadingFile(true)
    try {
      const result = await projectApi.getFileContent(selectedProject.id, filePath)
      setViewingFile({ path: filePath, content: result.content })
    } catch (err: any) {
      setError('Failed to load file: ' + (err.response?.data?.error || err.message))
    } finally {
      setLoadingFile(false)
    }
  }

  const handleUploadZip = async () => {
    if (!uploadFile || !uploadName.trim()) {
      setError('Please provide a name and select a file')
      return
    }

    setLoading(true)
    setError(null)
    try {
      const newProject = await projectApi.upload(uploadFile, uploadName.trim())
      setUploadFile(null)
      setUploadName('')
      if (fileInputRef.current) fileInputRef.current.value = ''
      await loadProjects()
      setActiveTab('list')
      handleSelectProject(newProject)
      onProjectAdded?.()
    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to upload project')
    } finally {
      setLoading(false)
    }
  }

  const handleRegisterPath = async () => {
    if (!registerPath.trim() || !registerName.trim()) {
      setError('Please provide a name and path')
      return
    }

    setLoading(true)
    setError(null)
    try {
      const newProject = await projectApi.register(registerPath.trim(), registerName.trim())
      setRegisterPath('')
      setRegisterName('')
      await loadProjects()
      setActiveTab('list')
      handleSelectProject(newProject)
      onProjectAdded?.()
    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to register project')
    } finally {
      setLoading(false)
    }
  }

  const handleDeleteProject = async (projectId: string) => {
    if (!confirm('Delete this project?')) return

    setLoading(true)
    try {
      await projectApi.delete(projectId)
      if (selectedProject?.id === projectId) {
        setSelectedProject(null)
        setProjectFiles([])
        setViewingFile(null)
      }
      await loadProjects()
    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to delete project')
    } finally {
      setLoading(false)
    }
  }

  const handleOpenAssociateDialog = (projectId: string) => {
    setAssociateProjectId(projectId)
    setSelectedFM(featureModels[0] || '')
    setShowAssociateDialog(true)
  }

  const handleAssociate = async () => {
    if (!associateProjectId || !selectedFM) return

    setLoading(true)
    try {
      await projectApi.associate(associateProjectId, selectedFM)
      await loadProjects()
      // Update selected project
      const updated = projects.find(p => p.id === associateProjectId)
      if (updated) {
        setSelectedProject({ ...updated, associatedFM: selectedFM })
      }
      setShowAssociateDialog(false)
      if (onAssociate) onAssociate(associateProjectId, selectedFM)
    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to associate project')
    } finally {
      setLoading(false)
    }
  }

  const handleFileDrop = (e: React.DragEvent) => {
    e.preventDefault()
    const file = e.dataTransfer.files[0]
    if (file && file.name.endsWith('.zip')) {
      setUploadFile(file)
      if (!uploadName) setUploadName(file.name.replace('.zip', ''))
    }
  }

  const handleDerive = () => {
    if (selectedProject && onDerive) {
      onDerive(selectedProject.id, selectedProject.name)
    }
  }

  const handleConfigureFM = () => {
    if (selectedProject?.associatedFM && onConfigureFM) {
      onConfigureFM(selectedProject.associatedFM)
    }
  }

  return (
    <div className="project-panel">
      <div className="project-panel-header">
        <h2>Projects</h2>
        <button onClick={onClose} className="close-btn" title="Close">
          <X size={18} />
        </button>
      </div>

      <div className="project-panel-tabs">
        <button
          className={`tab-btn ${activeTab === 'list' ? 'active' : ''}`}
          onClick={() => setActiveTab('list')}
        >
          Projects
        </button>
        <button
          className={`tab-btn ${activeTab === 'upload' ? 'active' : ''}`}
          onClick={() => setActiveTab('upload')}
        >
          Add Project
        </button>
      </div>

      {error && <div className="error-message">{error}</div>}

      <div className="project-panel-content">
        {activeTab === 'list' && (
          <div className="project-workspace">
            {/* Left sidebar: project list */}
            <div className="project-sidebar">
              <div className="sidebar-section-title">Your Projects</div>
              {loading && projects.length === 0 ? (
                <div className="loading">Loading...</div>
              ) : projects.length === 0 ? (
                <div className="empty-message">
                  No projects yet.<br/>
                  <button className="link-btn" onClick={() => setActiveTab('upload')}>
                    Add your first project
                  </button>
                </div>
              ) : (
                <div className="project-list-compact">
                  {projects.map(project => (
                    <div
                      key={project.id}
                      className={`project-item-compact ${selectedProject?.id === project.id ? 'selected' : ''}`}
                      onClick={() => handleSelectProject(project)}
                    >
                      <FolderOpen size={14} />
                      <span className="project-name">{project.name}</span>
                      {project.associatedFM && <span className="fm-badge">FM</span>}
                    </div>
                  ))}
                </div>
              )}
            </div>

            {/* Main area: project details and file browser */}
            <div className="project-main">
              {selectedProject ? (
                <>
                  {/* Project header with status */}
                  <div className="project-detail-header">
                    <div className="project-title">
                      <h3>{selectedProject.name}</h3>
                      <span className="project-source-type">{selectedProject.sourceType}</span>
                    </div>
                    <div className="project-header-actions">
                      <button
                        onClick={e => { e.stopPropagation(); handleDeleteProject(selectedProject.id) }}
                        className="icon-btn delete"
                        title="Delete project"
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                  </div>

                  {/* Workflow status */}
                  <div className="workflow-status">
                    <div className={`workflow-step ${selectedProject ? 'done' : ''}`}>
                      <CheckCircle size={14} />
                      <span>Project loaded</span>
                    </div>
                    <div className={`workflow-step ${selectedProject.associatedFM ? 'done' : 'pending'}`}>
                      {selectedProject.associatedFM ? <CheckCircle size={14} /> : <AlertCircle size={14} />}
                      <span>
                        {selectedProject.associatedFM
                          ? `FM: ${selectedProject.associatedFM}`
                          : 'No feature model'}
                      </span>
                      {!selectedProject.associatedFM && featureModels.length > 0 && (
                        <button
                          className="inline-action"
                          onClick={() => handleOpenAssociateDialog(selectedProject.id)}
                        >
                          Associate
                        </button>
                      )}
                      {!selectedProject.associatedFM && featureModels.length === 0 && (
                        <span className="hint">Create a FM first</span>
                      )}
                    </div>
                    {selectedProject.associatedFM && (
                      <div className="workflow-step pending">
                        <Settings size={14} />
                        <span>Configure features</span>
                        <button
                          className="inline-action"
                          onClick={handleConfigureFM}
                        >
                          Configure
                        </button>
                      </div>
                    )}
                  </div>

                  {/* Action buttons */}
                  <div className="project-actions-bar">
                    <button
                      className="action-btn-large"
                      onClick={() => handleOpenAssociateDialog(selectedProject.id)}
                      disabled={featureModels.length === 0}
                    >
                      <Link size={16} />
                      {selectedProject.associatedFM ? 'Change FM' : 'Associate FM'}
                    </button>
                    {selectedProject.associatedFM && (
                      <button
                        className="action-btn-large primary"
                        onClick={handleDerive}
                      >
                        <Play size={16} />
                        Derive Variant
                      </button>
                    )}
                  </div>

                  {/* File browser and viewer */}
                  <div className="file-browser">
                    <div className="file-tree-panel">
                      <div className="panel-title">Files</div>
                      <div className="file-tree-scroll">
                        <FileTree
                          nodes={projectFiles}
                          onFileClick={handleViewFile}
                          selectedPath={viewingFile?.path}
                        />
                      </div>
                    </div>
                    <div className="file-viewer-panel">
                      {loadingFile ? (
                        <div className="file-placeholder">Loading file...</div>
                      ) : viewingFile ? (
                        <>
                          <div className="file-viewer-header">
                            <FileCode size={14} />
                            <span>{viewingFile.path}</span>
                          </div>
                          <pre className="file-content">{viewingFile.content}</pre>
                        </>
                      ) : (
                        <div className="file-placeholder">
                          <Eye size={24} />
                          <span>Select a file to preview</span>
                        </div>
                      )}
                    </div>
                  </div>
                </>
              ) : (
                <div className="no-project-selected">
                  <FolderOpen size={48} />
                  <h3>Select a project</h3>
                  <p>Choose a project from the list or add a new one</p>
                </div>
              )}
            </div>
          </div>
        )}

        {activeTab === 'upload' && (
          <div className="upload-view">
            <div className="upload-section">
              <h3><Upload size={16} /> Upload ZIP</h3>
              <div
                className={`drop-zone ${uploadFile ? 'has-file' : ''}`}
                onDragOver={e => e.preventDefault()}
                onDrop={handleFileDrop}
                onClick={() => fileInputRef.current?.click()}
              >
                {uploadFile ? (
                  <span>{uploadFile.name}</span>
                ) : (
                  <span>Drop ZIP file here or click to browse</span>
                )}
                <input
                  ref={fileInputRef}
                  type="file"
                  accept=".zip"
                  onChange={e => {
                    const file = e.target.files?.[0]
                    if (file) {
                      setUploadFile(file)
                      if (!uploadName) setUploadName(file.name.replace('.zip', ''))
                    }
                  }}
                  style={{ display: 'none' }}
                />
              </div>
              <input
                type="text"
                placeholder="Project name"
                value={uploadName}
                onChange={e => setUploadName(e.target.value)}
                className="input-field"
              />
              <button
                onClick={handleUploadZip}
                disabled={!uploadFile || !uploadName.trim() || loading}
                className="submit-btn"
              >
                Upload
              </button>
            </div>

            <div className="upload-section">
              <h3><FolderOpen size={16} /> Register Local Path</h3>
              <p className="section-hint">Point to an existing folder on your filesystem</p>
              <input
                type="text"
                placeholder="Absolute path (e.g., /path/to/project)"
                value={registerPath}
                onChange={e => setRegisterPath(e.target.value)}
                className="input-field"
              />
              <input
                type="text"
                placeholder="Project name"
                value={registerName}
                onChange={e => setRegisterName(e.target.value)}
                className="input-field"
              />
              <button
                onClick={handleRegisterPath}
                disabled={!registerPath.trim() || !registerName.trim() || loading}
                className="submit-btn"
              >
                Register
              </button>
            </div>
          </div>
        )}
      </div>

      {showAssociateDialog && (
        <div className="dialog-overlay" onClick={() => setShowAssociateDialog(false)}>
          <div className="dialog" onClick={e => e.stopPropagation()}>
            <h3>Associate with Feature Model</h3>
            {featureModels.length === 0 ? (
              <>
                <p className="dialog-message">No feature models available.</p>
                <p className="dialog-hint">Create a feature model first by running a FAMILIAR command like:</p>
                <pre className="dialog-code">fm = FM(Root: A B [C];)</pre>
                <div className="dialog-actions">
                  <button onClick={() => setShowAssociateDialog(false)} className="cancel-btn">
                    Close
                  </button>
                </div>
              </>
            ) : (
              <>
                <p className="dialog-message">Select a feature model to associate with this project:</p>
                <select
                  value={selectedFM}
                  onChange={e => setSelectedFM(e.target.value)}
                  className="select-field"
                >
                  {featureModels.map(fm => (
                    <option key={fm} value={fm}>{fm}</option>
                  ))}
                </select>
                <div className="dialog-actions">
                  <button onClick={() => setShowAssociateDialog(false)} className="cancel-btn">
                    Cancel
                  </button>
                  <button onClick={handleAssociate} className="submit-btn" disabled={!selectedFM}>
                    Associate
                  </button>
                </div>
              </>
            )}
          </div>
        </div>
      )}
    </div>
  )
}

// File tree component with click handler
interface FileTreeProps {
  nodes: FileTreeNode[]
  level?: number
  onFileClick?: (path: string) => void
  selectedPath?: string
  parentPath?: string
}

const FileTree: React.FC<FileTreeProps> = ({ nodes, level = 0, onFileClick, selectedPath, parentPath = '' }) => {
  const [expanded, setExpanded] = useState<Record<string, boolean>>(() => {
    // Auto-expand first level
    const initial: Record<string, boolean> = {}
    nodes.forEach(n => { if (n.type === 'folder') initial[n.label] = level < 1 })
    return initial
  })

  const toggleExpand = (label: string) => {
    setExpanded(prev => ({ ...prev, [label]: !prev[label] }))
  }

  const getFullPath = (label: string) => parentPath ? `${parentPath}/${label}` : label

  return (
    <div className="file-tree" style={{ paddingLeft: level * 16 }}>
      {nodes.map(node => {
        const fullPath = getFullPath(node.label)
        return (
          <div key={node.label} className="file-node">
            <div
              className={`file-node-header ${node.type} ${selectedPath === fullPath ? 'selected' : ''}`}
              onClick={() => {
                if (node.type === 'folder') {
                  toggleExpand(node.label)
                } else if (onFileClick) {
                  onFileClick(fullPath)
                }
              }}
            >
              {node.type === 'folder' && (
                <span className="expand-icon">
                  {expanded[node.label] ? <ChevronDown size={14} /> : <ChevronRight size={14} />}
                </span>
              )}
              {node.type === 'folder' ? <Folder size={14} /> : <FileCode size={14} />}
              <span className="file-name">{node.label}</span>
            </div>
            {node.type === 'folder' && expanded[node.label] && node.children && (
              <FileTree
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

export default ProjectPanel
