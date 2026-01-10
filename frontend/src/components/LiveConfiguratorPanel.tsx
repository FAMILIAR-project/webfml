import { useState, useEffect, useRef } from 'react'
import { X, Check, Minus, ChevronRight, ChevronDown, AlertCircle, Download, Eye, FileCode, Save } from 'lucide-react'
import { configurationApi, derivationApi, DerivationResult, DerivedFile, ConfigFeatureNode } from '@/api/client'
import './LiveConfiguratorPanel.css'

// Check if file can be previewed visually
const isPreviewable = (path: string): boolean => {
  const ext = path.split('.').pop()?.toLowerCase()
  return ext === 'svg' || ext === 'html' || ext === 'htm'
}

interface LiveConfiguratorPanelProps {
  projectId: string
  projectName: string
  fmVariableId: string
  configId?: string  // If provided, use this existing config instead of starting fresh
  onClose: () => void
  onSaved?: () => void
}

const LiveConfiguratorPanel: React.FC<LiveConfiguratorPanelProps> = ({
  projectId,
  projectName,
  fmVariableId,
  configId,
  onClose,
  onSaved,
}) => {
  // Configuration state
  const [featureTree, setFeatureTree] = useState<ConfigFeatureNode | null>(null)
  const [selectedFeatures, setSelectedFeatures] = useState<Set<string>>(new Set())
  const [deselectedFeatures, setDeselectedFeatures] = useState<Set<string>>(new Set())
  const [configLoading, setConfigLoading] = useState(true)
  const [configError, setConfigError] = useState<string | null>(null)

  // Derivation state
  const [derivationResult, setDerivationResult] = useState<DerivationResult | null>(null)
  const [selectedFile, setSelectedFile] = useState<DerivedFile | null>(null)
  const [derivationLoading, setDerivationLoading] = useState(false)
  const [viewMode, setViewMode] = useState<'preview' | 'derived' | 'original' | 'diff'>('preview')

  // Debounce timer for derivation
  const derivationTimer = useRef<ReturnType<typeof setTimeout> | null>(null)

  // Load configuration
  useEffect(() => {
    loadConfiguration()
  }, [fmVariableId, configId])

  // Trigger derivation when selection changes (debounced)
  useEffect(() => {
    if (derivationTimer.current) {
      clearTimeout(derivationTimer.current)
    }
    derivationTimer.current = setTimeout(() => {
      deriveVariant()
    }, 300) // 300ms debounce

    return () => {
      if (derivationTimer.current) {
        clearTimeout(derivationTimer.current)
      }
    }
  }, [selectedFeatures, deselectedFeatures])

  const loadConfiguration = async () => {
    setConfigLoading(true)
    setConfigError(null)
    try {
      let result
      if (configId) {
        // Start with the specified configuration variable
        result = await configurationApi.start(configId)
      } else {
        // Try to get current session state first, otherwise start fresh with FM
        try {
          result = await configurationApi.getState()
          // Check if the current session matches the FM we want
          if (result.fmVariableId !== fmVariableId) {
            result = await configurationApi.start(fmVariableId)
          }
        } catch {
          // No active session, start fresh
          result = await configurationApi.start(fmVariableId)
        }
      }
      setFeatureTree(result.tree)
      setSelectedFeatures(new Set(result.selected))
      setDeselectedFeatures(new Set(result.deselected))
    } catch (err: any) {
      setConfigError(err.response?.data?.error || 'Failed to load configuration')
    } finally {
      setConfigLoading(false)
    }
  }

  const deriveVariant = async () => {
    if (!featureTree) return

    setDerivationLoading(true)
    try {
      // Derive with current configuration state
      const result = await derivationApi.derive(projectId)
      setDerivationResult(result)

      // Auto-select first previewable file, or first file with conditionals
      const previewableFile = result.files.find(f => isPreviewable(f.path))
      const conditionalFile = result.files.find(f => f.hasConditionals)
      setSelectedFile(previewableFile || conditionalFile || result.files[0] || null)

      if (previewableFile) {
        setViewMode('preview')
      }
    } catch (err: any) {
      console.error('Derivation error:', err)
    } finally {
      setDerivationLoading(false)
    }
  }

  const handleSelect = async (featureName: string) => {
    try {
      const result = await configurationApi.select(featureName)
      setSelectedFeatures(new Set(result.selected))
      setDeselectedFeatures(new Set(result.deselected))
    } catch (err: any) {
      setConfigError(err.response?.data?.error || 'Failed to select feature')
    }
  }

  const handleDeselect = async (featureName: string) => {
    try {
      const result = await configurationApi.deselect(featureName)
      setSelectedFeatures(new Set(result.selected))
      setDeselectedFeatures(new Set(result.deselected))
    } catch (err: any) {
      setConfigError(err.response?.data?.error || 'Failed to deselect feature')
    }
  }

  const handleUnselect = async (featureName: string) => {
    try {
      const result = await configurationApi.unselect(featureName)
      setSelectedFeatures(new Set(result.selected))
      setDeselectedFeatures(new Set(result.deselected))
    } catch (err: any) {
      setConfigError(err.response?.data?.error || 'Failed to unselect feature')
    }
  }

  const handleSave = async () => {
    try {
      await configurationApi.save()
      onSaved?.()
    } catch (err: any) {
      setConfigError(err.response?.data?.error || 'Failed to save configuration')
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
      setConfigError(err.response?.data?.error || 'Failed to download')
    }
  }

  if (configLoading) {
    return (
      <div className="live-configurator">
        <div className="live-header">
          <h2>Loading...</h2>
          <button onClick={onClose} className="close-btn"><X size={18} /></button>
        </div>
        <div className="loading-container">Initializing configuration...</div>
      </div>
    )
  }

  if (configError && !featureTree) {
    return (
      <div className="live-configurator">
        <div className="live-header">
          <h2>Error</h2>
          <button onClick={onClose} className="close-btn"><X size={18} /></button>
        </div>
        <div className="error-container">
          <AlertCircle size={24} />
          <p>{configError}</p>
        </div>
      </div>
    )
  }

  return (
    <div className="live-configurator">
      <div className="live-header">
        <div className="header-info">
          <h2>Live Configurator: {projectName}</h2>
          <span className="fm-badge">FM: {fmVariableId}</span>
        </div>
        <div className="header-actions">
          <button onClick={handleSave} className="save-btn" title="Save configuration">
            <Save size={16} />
            <span>Save Config</span>
          </button>
          <button onClick={handleDownload} className="download-btn" title="Download derived ZIP">
            <Download size={16} />
            <span>Download</span>
          </button>
          <button onClick={onClose} className="close-btn" title="Close">
            <X size={18} />
          </button>
        </div>
      </div>

      {configError && (
        <div className="error-banner">
          <AlertCircle size={14} />
          <span>{configError}</span>
          <button onClick={() => setConfigError(null)}>Dismiss</button>
        </div>
      )}

      <div className="live-content">
        {/* Left: Configuration Panel */}
        <div className="config-panel">
          <div className="panel-header">
            <span>Features</span>
            <span className="selection-count">
              <span className="selected">{selectedFeatures.size} selected</span>
              <span className="deselected">{deselectedFeatures.size} deselected</span>
            </span>
          </div>
          <div className="feature-tree-scroll">
            {featureTree && (
              <FeatureTreeView
                node={featureTree}
                selected={selectedFeatures}
                deselected={deselectedFeatures}
                onSelect={handleSelect}
                onDeselect={handleDeselect}
                onUnselect={handleUnselect}
              />
            )}
          </div>
        </div>

        {/* Right: Live Preview */}
        <div className="preview-panel">
          <div className="panel-header">
            <span>Live Preview</span>
            {derivationLoading && <span className="deriving-indicator">Deriving...</span>}
          </div>

          <div className="preview-content">
            {/* File list */}
            <div className="file-list">
              {derivationResult?.files.map(file => (
                <div
                  key={file.path}
                  className={`file-item ${selectedFile?.path === file.path ? 'selected' : ''} ${file.hasConditionals ? 'has-conditionals' : ''} ${isPreviewable(file.path) ? 'previewable' : ''}`}
                  onClick={() => {
                    setSelectedFile(file)
                    if (isPreviewable(file.path)) {
                      setViewMode('preview')
                    } else {
                      setViewMode('derived')
                    }
                  }}
                >
                  {isPreviewable(file.path) ? <Eye size={14} /> : <FileCode size={14} />}
                  <span className="file-name">{file.path.split('/').pop()}</span>
                  {file.hasConditionals && <span className="conditional-dot" />}
                </div>
              ))}
            </div>

            {/* File viewer */}
            <div className="file-viewer">
              {selectedFile ? (
                <>
                  <div className="viewer-header">
                    <span className="file-path">{selectedFile.path}</span>
                    <div className="view-toggle">
                      {isPreviewable(selectedFile.path) && (
                        <button
                          className={viewMode === 'preview' ? 'active' : ''}
                          onClick={() => setViewMode('preview')}
                        >
                          <Eye size={14} />
                        </button>
                      )}
                      <button
                        className={viewMode === 'derived' ? 'active' : ''}
                        onClick={() => setViewMode('derived')}
                      >
                        Code
                      </button>
                      <button
                        className={viewMode === 'diff' ? 'active' : ''}
                        onClick={() => setViewMode('diff')}
                      >
                        Diff
                      </button>
                    </div>
                  </div>
                  <div className="viewer-content">
                    {viewMode === 'preview' && isPreviewable(selectedFile.path) ? (
                      <PreviewRenderer content={selectedFile.derivedContent} fileName={selectedFile.path} />
                    ) : viewMode === 'diff' ? (
                      <SimpleDiff original={selectedFile.originalContent} derived={selectedFile.derivedContent} />
                    ) : (
                      <pre className="code-view">{selectedFile.derivedContent}</pre>
                    )}
                  </div>
                  {selectedFile.usedFeatures.length > 0 && (
                    <div className="used-features">
                      {selectedFile.usedFeatures.map(f => (
                        <span
                          key={f}
                          className={`feature-tag ${selectedFeatures.has(f) ? 'selected' : 'deselected'}`}
                        >
                          {f}
                        </span>
                      ))}
                    </div>
                  )}
                </>
              ) : (
                <div className="no-file">Select a file to preview</div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

// Feature tree component - renders ConfigFeatureNode structure
interface FeatureTreeViewProps {
  node: ConfigFeatureNode
  selected: Set<string>
  deselected: Set<string>
  onSelect: (name: string) => void
  onDeselect: (name: string) => void
  onUnselect: (name: string) => void
  level?: number
}

const FeatureTreeView: React.FC<FeatureTreeViewProps> = ({
  node,
  selected,
  deselected,
  onSelect,
  onDeselect,
  onUnselect,
  level = 0,
}) => {
  const [expanded, setExpanded] = useState(level < 3)

  // Collect all children from mandatory, optional, and groups
  const allChildren: ConfigFeatureNode[] = [
    ...node.mandatory,
    ...node.optional,
    ...node.orGroups.flatMap(g => g.members),
    ...node.xorGroups.flatMap(g => g.members),
    ...node.mutexGroups.flatMap(g => g.members),
  ]
  const hasChildren = allChildren.length > 0

  const isSelected = selected.has(node.name)
  const isDeselected = deselected.has(node.name)
  const isUndecided = !isSelected && !isDeselected

  const handleClick = () => {
    if (isUndecided || isDeselected) {
      onSelect(node.name)
    } else {
      onDeselect(node.name)
    }
  }

  const handleRightClick = (e: React.MouseEvent) => {
    e.preventDefault()
    onUnselect(node.name)
  }

  return (
    <div className="feature-node" style={{ marginLeft: level * 16 }}>
      <div className="feature-row">
        {hasChildren ? (
          <button className="expand-btn" onClick={() => setExpanded(!expanded)}>
            {expanded ? <ChevronDown size={14} /> : <ChevronRight size={14} />}
          </button>
        ) : (
          <span className="expand-placeholder" />
        )}
        <button
          className={`feature-checkbox ${isSelected ? 'selected' : ''} ${isDeselected ? 'deselected' : ''}`}
          onClick={handleClick}
          onContextMenu={handleRightClick}
          title={`Click to ${isSelected ? 'deselect' : 'select'}, right-click to reset`}
        >
          {isSelected && <Check size={12} />}
          {isDeselected && <X size={12} />}
          {isUndecided && <Minus size={12} />}
        </button>
        <span className={`feature-name ${isSelected ? 'selected' : ''} ${isDeselected ? 'deselected' : ''}`}>
          {node.name}
        </span>
        {node.xorGroups.length > 0 && <span className="group-badge">xor</span>}
        {node.orGroups.length > 0 && <span className="group-badge">or</span>}
      </div>
      {hasChildren && expanded && (
        <div className="feature-children">
          {allChildren.map(child => (
            <FeatureTreeView
              key={child.name}
              node={child}
              selected={selected}
              deselected={deselected}
              onSelect={onSelect}
              onDeselect={onDeselect}
              onUnselect={onUnselect}
              level={level + 1}
            />
          ))}
        </div>
      )}
    </div>
  )
}

// Preview renderer
interface PreviewRendererProps {
  content: string
  fileName: string
}

const PreviewRenderer: React.FC<PreviewRendererProps> = ({ content, fileName }) => {
  const ext = fileName.split('.').pop()?.toLowerCase()

  if (ext === 'svg') {
    return (
      <div className="svg-preview">
        <div className="svg-container" dangerouslySetInnerHTML={{ __html: content }} />
      </div>
    )
  }

  if (ext === 'html' || ext === 'htm') {
    return (
      <div className="html-preview">
        <iframe srcDoc={content} title="HTML Preview" sandbox="allow-scripts" />
      </div>
    )
  }

  return <pre className="code-view">{content}</pre>
}

// Simple diff component
interface SimpleDiffProps {
  original: string
  derived: string
}

const SimpleDiff: React.FC<SimpleDiffProps> = ({ original, derived }) => {
  const origLines = original.split('\n')
  const derivedLines = derived.split('\n')

  const diff: { type: 'same' | 'removed' | 'added'; content: string }[] = []

  let oi = 0, di = 0
  while (oi < origLines.length || di < derivedLines.length) {
    if (origLines[oi] === derivedLines[di]) {
      diff.push({ type: 'same', content: origLines[oi] || '' })
      oi++; di++
    } else if (origLines[oi] !== undefined && !derivedLines.includes(origLines[oi])) {
      diff.push({ type: 'removed', content: origLines[oi] })
      oi++
    } else if (derivedLines[di] !== undefined && !origLines.includes(derivedLines[di])) {
      diff.push({ type: 'added', content: derivedLines[di] })
      di++
    } else {
      if (origLines[oi] !== undefined) { diff.push({ type: 'removed', content: origLines[oi] }); oi++ }
      if (derivedLines[di] !== undefined) { diff.push({ type: 'added', content: derivedLines[di] }); di++ }
    }
  }

  return (
    <pre className="diff-view">
      {diff.map((line, i) => (
        <div key={i} className={`diff-line ${line.type}`}>
          <span className="diff-marker">{line.type === 'removed' ? '-' : line.type === 'added' ? '+' : ' '}</span>
          <span>{line.content}</span>
        </div>
      ))}
    </pre>
  )
}

export default LiveConfiguratorPanel
