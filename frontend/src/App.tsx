import { useState } from 'react'
import Editor from './components/Editor'
import Console from './components/Console'
import Toolbar from './components/Toolbar'
import Split from 'react-split'
import './App.css'

function App() {
  const [code, setCode] = useState<string>('// Enter your FAMILIAR code here\n')

  return (
    <div className="app">
      <Toolbar code={code} />
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
    </div>
  )
}

export default App
