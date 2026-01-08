import { useState } from 'react'
import Editor from './components/Editor'
import Console from './components/Console'
import Toolbar from './components/Toolbar'
import VariablesPanel from './components/VariablesPanel'
import KSynthesisPanel from './components/KSynthesisPanel'
import FeatureModelTree from './components/FeatureModelTree'
import ConfiguratorPanel from './components/ConfiguratorPanel'
import Split from 'react-split'
import './App.css'

function App() {
  const [code, setCode] = useState<string>('// Enter your FAMILIAR code here\n')
  const [displayedFMId, setDisplayedFMId] = useState<string | null>(null)
  const [synthesisVariable, setSynthesisVariable] = useState<string | null>(null)
  const [configurationVariable, setConfigurationVariable] = useState<string | null>(null)

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

  return (
    <div className="app">
      <Toolbar code={code} />
      <div className="main-content">
        <Split
          className="split-container"
          direction="vertical"
          sizes={[60, 40]}
          minSize={100}
          gutterSize={8}
        >
          <div className="editor-container">
            <Editor value={code} onChange={setCode} />
          </div>
          <div className="console-container">
            <Console />
          </div>
        </Split>
        <VariablesPanel onDisplayFM={handleDisplayFM} onSynthesize={handleSynthesize} onConfigure={handleConfigure} />
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
    </div>
  )
}

export default App
