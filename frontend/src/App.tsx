import { useState } from 'react'
import Editor from './components/Editor'
import Console from './components/Console'
import Toolbar from './components/Toolbar'
import VariablesPanel from './components/VariablesPanel'
import KSynthesisPanel from './components/KSynthesisPanel'
import Split from 'react-split'
import './App.css'

function App() {
  const [code, setCode] = useState<string>('// Enter your FAMILIAR code here\n')
  const [displayedFM, setDisplayedFM] = useState<{ id: string; value: string } | null>(null)
  const [synthesisVariable, setSynthesisVariable] = useState<string | null>(null)

  const handleDisplayFM = (id: string, value: string) => {
    setDisplayedFM({ id, value })
  }

  const closeDisplayModal = () => {
    setDisplayedFM(null)
  }

  const handleSynthesize = (variableId: string) => {
    setSynthesisVariable(variableId)
  }

  const closeSynthesisPanel = () => {
    setSynthesisVariable(null)
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
        <VariablesPanel onDisplayFM={handleDisplayFM} onSynthesize={handleSynthesize} />
      </div>

      {/* Feature Model Display Modal */}
      {displayedFM && (
        <div className="modal-overlay" onClick={closeDisplayModal}>
          <div className="modal-content" onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h3>Feature Model: {displayedFM.id}</h3>
              <button onClick={closeDisplayModal} className="modal-close">&times;</button>
            </div>
            <div className="modal-body">
              <pre className="fm-display">{displayedFM.value}</pre>
            </div>
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
    </div>
  )
}

export default App
