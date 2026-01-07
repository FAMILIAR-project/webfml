import { Play, RotateCcw, Save, FolderOpen } from 'lucide-react'
import { familiarApi } from '@/api/client'
import { useState } from 'react'
import './Toolbar.css'

interface ToolbarProps {
  code: string
}

const Toolbar: React.FC<ToolbarProps> = ({ code }) => {
  const [isRunning, setIsRunning] = useState(false)

  const handleRun = async () => {
    if (!code.trim()) return

    setIsRunning(true)
    try {
      const response = await familiarApi.interpret(code)
      console.log('Execution result:', response)
    } catch (error) {
      console.error('Execution error:', error)
    } finally {
      setIsRunning(false)
    }
  }

  const handleReset = async () => {
    try {
      await familiarApi.reset()
      console.log('Environment reset')
    } catch (error) {
      console.error('Reset error:', error)
    }
  }

  const handleSave = () => {
    // TODO: Implement save functionality
    console.log('Save file')
  }

  const handleOpen = () => {
    // TODO: Implement open functionality
    console.log('Open file')
  }

  return (
    <div className="toolbar">
      <div className="toolbar-title">
        <h1>WebFML</h1>
        <span className="toolbar-subtitle">FAMILIAR Web IDE</span>
      </div>

      <div className="toolbar-actions">
        <button
          onClick={handleRun}
          disabled={isRunning}
          className="toolbar-btn toolbar-btn-primary"
          title="Run (Ctrl+Enter)"
        >
          <Play size={18} />
          <span>Run</span>
        </button>

        <button
          onClick={handleReset}
          className="toolbar-btn"
          title="Reset environment"
        >
          <RotateCcw size={18} />
          <span>Reset</span>
        </button>

        <div className="toolbar-divider" />

        <button onClick={handleOpen} className="toolbar-btn" title="Open file">
          <FolderOpen size={18} />
          <span>Open</span>
        </button>

        <button onClick={handleSave} className="toolbar-btn" title="Save file">
          <Save size={18} />
          <span>Save</span>
        </button>
      </div>
    </div>
  )
}

export default Toolbar
