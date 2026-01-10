import { useState, useCallback } from 'react'
import Console from './components/Console'
import Toolbar from './components/Toolbar'
import VariablesPanel from './components/VariablesPanel'
import KSynthesisPanel from './components/KSynthesisPanel'
import FeatureModelTree from './components/FeatureModelTree'
import ConfiguratorPanel from './components/ConfiguratorPanel'
import ConfigsTable from './components/ConfigsTable'
import ProjectPanel from './components/ProjectPanel'
import DerivationPreviewPanel from './components/DerivationPreviewPanel'
import LiveConfiguratorPanel from './components/LiveConfiguratorPanel'
import WorkspacePanel from './components/WorkspacePanel'
import TabbedEditor, { EditorTab } from './components/TabbedEditor'
import Split from 'react-split'
import './App.css'

const FAMILIAR_TAB_ID = 'familiar-main'

function App() {
  // Editor tabs state
  const [editorTabs, setEditorTabs] = useState<EditorTab[]>([
    {
      id: FAMILIAR_TAB_ID,
      title: 'FAMILIAR',
      content: '// Enter your FAMILIAR code here\n',
      language: 'javascript',
      isModified: false,
      isFamiliar: true,
    }
  ])
  const [activeTabId, setActiveTabId] = useState<string>(FAMILIAR_TAB_ID)

  // Other state
  const [displayedFMId, setDisplayedFMId] = useState<string | null>(null)
  const [synthesisVariable, setSynthesisVariable] = useState<string | null>(null)
  const [configurationVariable, setConfigurationVariable] = useState<string | null>(null)
  const [configsVariable, setConfigsVariable] = useState<string | null>(null)
  const [projectPanelOpen, setProjectPanelOpen] = useState(false)
  const [derivationProject, setDerivationProject] = useState<{ id: string; name: string; configId?: string } | null>(null)
  const [liveConfigProject, setLiveConfigProject] = useState<{ id: string; name: string; fmVariableId: string; configId?: string } | null>(null)
  const [featureModelIds, setFeatureModelIds] = useState<string[]>([])
  const [configurationIds, setConfigurationIds] = useState<string[]>([])
  const [projectRefreshTrigger, setProjectRefreshTrigger] = useState(0)

  // Get FAMILIAR code (for toolbar)
  const familiarCode = editorTabs.find(t => t.id === FAMILIAR_TAB_ID)?.content || ''

  // Tab management
  const handleTabChange = (tabId: string) => {
    setActiveTabId(tabId)
  }

  const handleTabClose = (tabId: string) => {
    // Don't close FAMILIAR tab
    if (tabId === FAMILIAR_TAB_ID) return

    setEditorTabs(prev => prev.filter(t => t.id !== tabId))
    // If closing active tab, switch to FAMILIAR
    if (tabId === activeTabId) {
      setActiveTabId(FAMILIAR_TAB_ID)
    }
  }

  const handleContentChange = (tabId: string, content: string) => {
    setEditorTabs(prev => prev.map(tab =>
      tab.id === tabId
        ? { ...tab, content, isModified: tab.isFamiliar ? false : true }
        : tab
    ))
  }

  // Open file in editor tab
  const handleOpenFile = useCallback((filePath: string, fileName: string, content: string) => {
    const tabId = `file-${filePath}`

    // Check if tab already exists
    const existingTab = editorTabs.find(t => t.id === tabId)
    if (existingTab) {
      setActiveTabId(tabId)
      return
    }

    // Create new tab
    const newTab: EditorTab = {
      id: tabId,
      title: fileName,
      content,
      language: 'plaintext',
      isModified: false,
      isFamiliar: false,
      filePath,
    }

    setEditorTabs(prev => [...prev, newTab])
    setActiveTabId(tabId)
  }, [editorTabs])

  const handleDisplayFM = (id: string, _value: string) => {
    setDisplayedFMId(id)
  }

  const closeDisplayModal = () => {
    setDisplayedFMId(null)
  }

  const handleSynthesize = (variableId: string) => {
    setSynthesisVariable(variableId)
  }

  const closeSynthesisPanel = () => {
    setSynthesisVariable(null)
  }

  const handleConfigure = (variableId: string) => {
    setConfigurationVariable(variableId)
  }

  const closeConfiguratorPanel = () => {
    setConfigurationVariable(null)
  }

  const handleShowConfigs = (variableId: string) => {
    setConfigsVariable(variableId)
  }

  const closeConfigsTable = () => {
    setConfigsVariable(null)
  }

  const openProjectPanel = () => {
    setProjectPanelOpen(true)
  }

  const closeProjectPanel = () => {
    setProjectPanelOpen(false)
  }

  const handleDerive = (projectId: string, projectName: string, configId?: string) => {
    setDerivationProject({ id: projectId, name: projectName, configId })
  }

  const closeDerivationPanel = () => {
    setDerivationProject(null)
  }

  const handleLiveConfig = (projectId: string, projectName: string, fmVariableId: string, configId?: string) => {
    setLiveConfigProject({ id: projectId, name: projectName, fmVariableId, configId })
  }

  const closeLiveConfigPanel = () => {
    setLiveConfigProject(null)
  }

  const updateFeatureModelIds = (ids: string[]) => {
    setFeatureModelIds(ids)
  }

  const updateConfigurationIds = (ids: string[]) => {
    setConfigurationIds(ids)
  }

  return (
    <div className="app">
      <Toolbar code={familiarCode} />
      <div className="main-content">
        <WorkspacePanel
          featureModels={featureModelIds}
          configurations={configurationIds}
          onDerive={handleDerive}
          onLiveConfig={handleLiveConfig}
          onConfigureFM={handleConfigure}
          onOpenAddProject={openProjectPanel}
          onOpenFile={handleOpenFile}
          refreshTrigger={projectRefreshTrigger}
        />
        <Split
          className="split-container"
          direction="vertical"
          sizes={[60, 40]}
          minSize={100}
          gutterSize={8}
        >
          <div className="editor-container">
            <TabbedEditor
              tabs={editorTabs}
              activeTabId={activeTabId}
              onTabChange={handleTabChange}
              onTabClose={handleTabClose}
              onContentChange={handleContentChange}
            />
          </div>
          <div className="console-container">
            <Console />
          </div>
        </Split>
        <VariablesPanel
          onDisplayFM={handleDisplayFM}
          onSynthesize={handleSynthesize}
          onConfigure={handleConfigure}
          onShowConfigs={handleShowConfigs}
          onOpenProjects={openProjectPanel}
          onFeatureModelsChange={updateFeatureModelIds}
          onConfigurationsChange={updateConfigurationIds}
        />
      </div>

      {/* Feature Model Display Modal */}
      {displayedFMId && (
        <div className="modal-overlay" onClick={closeDisplayModal}>
          <div className="modal-content fm-tree-modal" onClick={e => e.stopPropagation()}>
            <FeatureModelTree variableId={displayedFMId} onClose={closeDisplayModal} />
          </div>
        </div>
      )}

      {/* Interactive KSynthesis Modal */}
      {synthesisVariable && (
        <div className="modal-overlay" onClick={closeSynthesisPanel}>
          <div className="modal-content ksynthesis-modal" onClick={e => e.stopPropagation()}>
            <KSynthesisPanel
              variableId={synthesisVariable}
              onClose={closeSynthesisPanel}
              onSaved={closeSynthesisPanel}
            />
          </div>
        </div>
      )}

      {/* Feature Model Configuration Modal */}
      {configurationVariable && (
        <div className="modal-overlay" onClick={closeConfiguratorPanel}>
          <div className="modal-content configurator-modal" onClick={e => e.stopPropagation()}>
            <ConfiguratorPanel
              variableId={configurationVariable}
              onClose={closeConfiguratorPanel}
              onSaved={closeConfiguratorPanel}
            />
          </div>
        </div>
      )}

      {/* Configurations Table Modal */}
      {configsVariable && (
        <div className="modal-overlay" onClick={closeConfigsTable}>
          <div className="modal-content configs-table-modal" onClick={e => e.stopPropagation()}>
            <ConfigsTable
              variableId={configsVariable}
              onClose={closeConfigsTable}
            />
          </div>
        </div>
      )}

      {/* Project Panel Modal */}
      {projectPanelOpen && (
        <div className="modal-overlay" onClick={closeProjectPanel}>
          <div className="modal-content project-panel-modal" onClick={e => e.stopPropagation()}>
            <ProjectPanel
              onClose={closeProjectPanel}
              featureModels={featureModelIds}
              onDerive={(projectId, projectName) => {
                closeProjectPanel()
                handleDerive(projectId, projectName)
              }}
              onConfigureFM={(fmVariableId) => {
                closeProjectPanel()
                handleConfigure(fmVariableId)
              }}
              onProjectAdded={() => {
                setProjectRefreshTrigger(prev => prev + 1)
              }}
            />
          </div>
        </div>
      )}

      {/* Derivation Preview Modal */}
      {derivationProject && (
        <div className="modal-overlay" onClick={closeDerivationPanel}>
          <div className="modal-content derivation-modal" onClick={e => e.stopPropagation()}>
            <DerivationPreviewPanel
              projectId={derivationProject.id}
              projectName={derivationProject.name}
              configId={derivationProject.configId}
              onClose={closeDerivationPanel}
            />
          </div>
        </div>
      )}

      {/* Live Configurator Modal */}
      {liveConfigProject && (
        <div className="modal-overlay" onClick={closeLiveConfigPanel}>
          <div className="modal-content live-config-modal" onClick={e => e.stopPropagation()}>
            <LiveConfiguratorPanel
              projectId={liveConfigProject.id}
              projectName={liveConfigProject.name}
              fmVariableId={liveConfigProject.fmVariableId}
              configId={liveConfigProject.configId}
              onClose={closeLiveConfigPanel}
              onSaved={closeLiveConfigPanel}
            />
          </div>
        </div>
      )}
    </div>
  )
}

export default App
