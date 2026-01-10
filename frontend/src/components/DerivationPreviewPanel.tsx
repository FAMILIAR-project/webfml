import { useState, useEffect } from 'react'
import { X, Download, FileCode, Folder, ChevronRight, ChevronDown, AlertCircle, Check, Eye } from 'lucide-react'
import { derivationApi, DerivationResult, DerivedFile } from '@/api/client'
import './DerivationPreviewPanel.css'

// Check if file can be previewed visually
const isPreviewable = (path: string): boolean => {
  const ext = path.split('.').pop()?.toLowerCase()
  return ext === 'svg' || ext === 'html' || ext === 'htm'
}

interface DerivationPreviewPanelProps {
  projectId: string
  projectName: string
  configId?: string
  onClose: () => void
}

const DerivationPreviewPanel: React.FC<DerivationPreviewPanelProps> = ({
  projectId,
  projectName,
  configId,
  onClose,
}) => {
  const [result, setResult] = useState<DerivationResult | null>(null)
  const [selectedFile, setSelectedFile] = useState<DerivedFile | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [viewMode, setViewMode] = useState<'derived' | 'original' | 'diff' | 'preview'>('derived')

  useEffect(() => {
    deriveVariant()
  }, [projectId, configId])

  // Auto-switch to preview mode for SVG/HTML files
  useEffect(() => {
    if (selectedFile && isPreviewable(selectedFile.path)) {
      setViewMode('preview')
    } else if (viewMode === 'preview') {
      setViewMode('derived')
    }
  }, [selectedFile?.path])

  const deriveVariant = async () => {
    setLoading(true)
    setError(null)
    try {
      const derivationResult = await derivationApi.derive(projectId, configId)
      setResult(derivationResult)
      // Auto-select first file with conditionals
      const firstWithConditionals = derivationResult.files.find(f => f.hasConditionals)
      setSelectedFile(firstWithConditionals || derivationResult.files[0] || null)
    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to derive variant')
    } finally {
      setLoading(false)
    }
  }

  const handleDownload = async () => {
    try {
      const blob = await derivationApi.downloadZip(projectId)
      const url = window.URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = `${projectName}-derived.zip`
      document.body.appendChild(a)
      a.click()
      window.URL.revokeObjectURL(url)
      document.body.removeChild(a)
    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to download')
    }
  }

  const buildFileTree = (files: DerivedFile[]): TreeNode[] => {
    const root: TreeNode[] = []
    const pathMap: Record<string, TreeNode> = {}

    for (const file of files) {
      const parts = file.path.split('/')
      let currentPath = ''

      for (let i = 0; i < parts.length; i++) {
        const part = parts[i]
        const parentPath = currentPath
        currentPath = currentPath ? `${currentPath}/${part}` : part
        const isFile = i === parts.length - 1

        if (!pathMap[currentPath]) {
          const node: TreeNode = {
            name: part,
            path: currentPath,
            isFile,
            file: isFile ? file : undefined,
            children: [],
          }
          pathMap[currentPath] = node

          if (parentPath && pathMap[parentPath]) {
            pathMap[parentPath].children.push(node)
          } else if (!parentPath) {
            root.push(node)
          }
        }
      }
    }

    return root
  }

  if (loading) {
    return (
      <div className="derivation-panel">
        <div className="derivation-panel-header">
          <h2>Deriving {projectName}...</h2>
          <button onClick={onClose} className="close-btn"><X size={18} /></button>
        </div>
        <div className="loading-container">Loading...</div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="derivation-panel">
        <div className="derivation-panel-header">
          <h2>Derivation Error</h2>
          <button onClick={onClose} className="close-btn"><X size={18} /></button>
        </div>
        <div className="error-container">
          <AlertCircle size={24} />
          <p>{error}</p>
        </div>
      </div>
    )
  }

  if (!result) return null

  const fileTree = buildFileTree(result.files)

  return (
    <div className="derivation-panel">
      <div className="derivation-panel-header">
        <div className="header-left">
          <h2>Derived: {projectName}</h2>
          <div className="feature-summary">
            <span className="selected-count">
              <Check size={12} /> {result.selectedFeatures.length} selected
            </span>
            <span className="deselected-count">
              <X size={12} /> {result.deselectedFeatures.length} deselected
            </span>
          </div>
        </div>
        <div className="header-actions">
          <button onClick={handleDownload} className="download-btn" title="Download ZIP">
            <Download size={16} />
            <span>Download ZIP</span>
          </button>
          <button onClick={onClose} className="close-btn" title="Close">
            <X size={18} />
          </button>
        </div>
      </div>

      {result.hasErrors && (
        <div className="errors-section">
          <div className="errors-banner">
            <AlertCircle size={16} />
            <span>{result.errors.length} template error(s) found</span>
          </div>
          <div className="errors-list">
            {result.errors.map((err, i) => (
              <div key={i} className="error-item">
                <span className="error-file">{err.filePath}</span>
                {err.lineNumber > 0 && <span className="error-line">line {err.lineNumber}</span>}
                <span className="error-message">{err.message}</span>
              </div>
            ))}
          </div>
        </div>
      )}

      <div className="derivation-panel-content">
        <div className="file-sidebar">
          <div className="sidebar-header">Files ({result.files.length})</div>
          <div className="file-tree-container">
            <FileTreeView
              nodes={fileTree}
              selectedPath={selectedFile?.path}
              onSelectFile={(file) => setSelectedFile(file)}
            />
          </div>
        </div>

        <div className="file-viewer">
          {selectedFile ? (
            <>
              <div className="viewer-header">
                <span className="file-path">{selectedFile.path}</span>
                {selectedFile.hasConditionals && (
                  <span className="has-conditionals-badge">Has Conditionals</span>
                )}
                <div className="view-mode-toggle">
                  {isPreviewable(selectedFile.path) && (
                    <button
                      className={viewMode === 'preview' ? 'active' : ''}
                      onClick={() => setViewMode('preview')}
                      title="Visual Preview"
                    >
                      <Eye size={14} />
                    </button>
                  )}
                  <button
                    className={viewMode === 'derived' ? 'active' : ''}
                    onClick={() => setViewMode('derived')}
                  >
                    Derived
                  </button>
                  <button
                    className={viewMode === 'original' ? 'active' : ''}
                    onClick={() => setViewMode('original')}
                  >
                    Original
                  </button>
                  <button
                    className={viewMode === 'diff' ? 'active' : ''}
                    onClick={() => setViewMode('diff')}
                  >
                    Diff
                  </button>
                </div>
              </div>
              <div className="file-content">
                {viewMode === 'preview' && isPreviewable(selectedFile.path) ? (
                  <PreviewRenderer content={selectedFile.derivedContent} fileName={selectedFile.path} />
                ) : viewMode === 'diff' ? (
                  <DiffView original={selectedFile.originalContent} derived={selectedFile.derivedContent} />
                ) : (
                  <pre className="code-block">
                    {viewMode === 'derived' ? selectedFile.derivedContent : selectedFile.originalContent}
                  </pre>
                )}
              </div>
              {selectedFile.usedFeatures.length > 0 && (
                <div className="used-features">
                  <span>Features used:</span>
                  {selectedFile.usedFeatures.map(f => (
                    <span
                      key={f}
                      className={`feature-tag ${result.selectedFeatures.includes(f) ? 'selected' : 'deselected'}`}
                    >
                      {f}
                    </span>
                  ))}
                </div>
              )}
            </>
          ) : (
            <div className="no-file-selected">Select a file to preview</div>
          )}
        </div>
      </div>
    </div>
  )
}

// Tree node type
interface TreeNode {
  name: string
  path: string
  isFile: boolean
  file?: DerivedFile
  children: TreeNode[]
}

// File tree view component
interface FileTreeViewProps {
  nodes: TreeNode[]
  selectedPath?: string
  onSelectFile: (file: DerivedFile) => void
  level?: number
}

const FileTreeView: React.FC<FileTreeViewProps> = ({
  nodes,
  selectedPath,
  onSelectFile,
  level = 0,
}) => {
  const [expanded, setExpanded] = useState<Record<string, boolean>>({})

  // Auto-expand all by default
  useEffect(() => {
    const expandAll: Record<string, boolean> = {}
    const traverse = (nodes: TreeNode[]) => {
      for (const node of nodes) {
        if (!node.isFile) {
          expandAll[node.path] = true
          traverse(node.children)
        }
      }
    }
    traverse(nodes)
    setExpanded(expandAll)
  }, [nodes])

  const toggleExpand = (path: string) => {
    setExpanded(prev => ({ ...prev, [path]: !prev[path] }))
  }

  return (
    <div className="tree-level" style={{ paddingLeft: level * 12 }}>
      {nodes.map(node => (
        <div key={node.path}>
          <div
            className={`tree-node ${node.isFile ? 'file' : 'folder'} ${selectedPath === node.path ? 'selected' : ''} ${node.file?.hasConditionals ? 'has-conditionals' : ''}`}
            onClick={() => {
              if (node.isFile && node.file) {
                onSelectFile(node.file)
              } else {
                toggleExpand(node.path)
              }
            }}
          >
            {!node.isFile && (
              <span className="expand-icon">
                {expanded[node.path] ? <ChevronDown size={12} /> : <ChevronRight size={12} />}
              </span>
            )}
            {node.isFile ? <FileCode size={14} /> : <Folder size={14} />}
            <span className="node-name">{node.name}</span>
          </div>
          {!node.isFile && expanded[node.path] && (
            <FileTreeView
              nodes={node.children}
              selectedPath={selectedPath}
              onSelectFile={onSelectFile}
              level={level + 1}
            />
          )}
        </div>
      ))}
    </div>
  )
}

// Preview renderer for SVG and HTML files
interface PreviewRendererProps {
  content: string
  fileName: string
}

const PreviewRenderer: React.FC<PreviewRendererProps> = ({ content, fileName }) => {
  const ext = fileName.split('.').pop()?.toLowerCase()

  if (ext === 'svg') {
    return (
      <div className="svg-preview">
        <div
          className="svg-container"
          dangerouslySetInnerHTML={{ __html: content }}
        />
      </div>
    )
  }

  if (ext === 'html' || ext === 'htm') {
    return (
      <div className="html-preview">
        <iframe
          srcDoc={content}
          title="HTML Preview"
          sandbox="allow-scripts"
          className="html-iframe"
        />
      </div>
    )
  }

  return <pre className="code-block">{content}</pre>
}

// Simple diff view component
interface DiffViewProps {
  original: string
  derived: string
}

const DiffView: React.FC<DiffViewProps> = ({ original, derived }) => {
  const originalLines = original.split('\n')
  const derivedLines = derived.split('\n')

  // Simple line-by-line diff
  const diffLines: { type: 'same' | 'removed' | 'added'; content: string }[] = []

  let oi = 0
  let di = 0

  while (oi < originalLines.length || di < derivedLines.length) {
    const origLine = originalLines[oi]
    const derivLine = derivedLines[di]

    if (origLine === derivLine) {
      diffLines.push({ type: 'same', content: origLine || '' })
      oi++
      di++
    } else if (origLine !== undefined && !derivedLines.includes(origLine)) {
      diffLines.push({ type: 'removed', content: origLine })
      oi++
    } else if (derivLine !== undefined && !originalLines.includes(derivLine)) {
      diffLines.push({ type: 'added', content: derivLine })
      di++
    } else {
      // Both exist but don't match - show as removed then added
      if (origLine !== undefined) {
        diffLines.push({ type: 'removed', content: origLine })
        oi++
      }
      if (derivLine !== undefined) {
        diffLines.push({ type: 'added', content: derivLine })
        di++
      }
    }
  }

  return (
    <pre className="diff-view">
      {diffLines.map((line, i) => (
        <div key={i} className={`diff-line ${line.type}`}>
          <span className="diff-marker">
            {line.type === 'removed' ? '-' : line.type === 'added' ? '+' : ' '}
          </span>
          <span className="diff-content">{line.content}</span>
        </div>
      ))}
    </pre>
  )
}

export default DerivationPreviewPanel
