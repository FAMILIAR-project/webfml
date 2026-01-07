import { Editor as MonacoEditor } from '@monaco-editor/react'
import { useEffect, useRef } from 'react'
import { editor } from 'monaco-editor'
import { familiarApi } from '@/api/client'

interface EditorProps {
  value: string
  onChange: (value: string) => void
}

const Editor: React.FC<EditorProps> = ({ value, onChange }) => {
  const editorRef = useRef<editor.IStandaloneCodeEditor | null>(null)

  useEffect(() => {
    // Load FAMILIAR keywords for syntax highlighting
    familiarApi.getKeywords().then(keywords => {
      console.log('Loaded FAMILIAR keywords:', keywords.length)
    })
  }, [])

  const handleEditorDidMount = (editor: editor.IStandaloneCodeEditor) => {
    editorRef.current = editor

    // Configure FAMILIAR language
    editor.getModel()?.updateOptions({ tabSize: 2 })

    // Add keyboard shortcuts
    // Note: monaco global is available after editor mount
    // For now, using editor API directly
    editor.addAction({
      id: 'run-familiar',
      label: 'Run FAMILIAR Code',
      keybindings: [2048 | 3], // Ctrl+Enter (monaco.KeyMod.CtrlCmd | monaco.KeyCode.Enter)
      run: () => {
        console.log('Run command triggered')
      },
    })
  }

  return (
    <div className="w-full h-full">
      <MonacoEditor
        height="100%"
        defaultLanguage="javascript"
        theme="vs-dark"
        value={value}
        onChange={value => onChange(value || '')}
        onMount={handleEditorDidMount}
        options={{
          minimap: { enabled: false },
          fontSize: 14,
          lineNumbers: 'on',
          roundedSelection: false,
          scrollBeyondLastLine: false,
          automaticLayout: true,
          wordWrap: 'on',
          tabSize: 2,
          insertSpaces: true,
        }}
      />
    </div>
  )
}

export default Editor
