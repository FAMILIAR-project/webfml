import { Editor as MonacoEditor } from '@monaco-editor/react'
import { useEffect, useRef } from 'react'
import { editor } from 'monaco-editor'
import { X, FileCode, Sparkles } from 'lucide-react'
import { familiarApi } from '@/api/client'
import './TabbedEditor.css'

export interface EditorTab {
  id: string
  title: string
  content: string
  language: string
  isModified: boolean
  isFamiliar: boolean
  filePath?: string // For project files
}

interface TabbedEditorProps {
  tabs: EditorTab[]
  activeTabId: string
  onTabChange: (tabId: string) => void
  onTabClose: (tabId: string) => void
  onContentChange: (tabId: string, content: string) => void
}

const TabbedEditor: React.FC<TabbedEditorProps> = ({
  tabs,
  activeTabId,
  onTabChange,
  onTabClose,
  onContentChange,
}) => {
  const editorRef = useRef<editor.IStandaloneCodeEditor | null>(null)

  const activeTab = tabs.find(t => t.id === activeTabId)

  useEffect(() => {
    familiarApi.getKeywords().then(keywords => {
      console.log('Loaded FAMILIAR keywords:', keywords.length)
    })
  }, [])

  const handleEditorDidMount = (editor: editor.IStandaloneCodeEditor) => {
    editorRef.current = editor
    editor.getModel()?.updateOptions({ tabSize: 2 })

    editor.addAction({
      id: 'run-familiar',
      label: 'Run FAMILIAR Code',
      keybindings: [2048 | 3],
      run: () => {
        console.log('Run command triggered')
      },
    })
  }

  const getLanguageForFile = (filename: string): string => {
    const ext = filename.split('.').pop()?.toLowerCase()
    const languageMap: Record<string, string> = {
      'java': 'java',
      'js': 'javascript',
      'ts': 'typescript',
      'tsx': 'typescript',
      'jsx': 'javascript',
      'py': 'python',
      'md': 'markdown',
      'json': 'json',
      'xml': 'xml',
      'html': 'html',
      'css': 'css',
      'scss': 'scss',
      'yaml': 'yaml',
      'yml': 'yaml',
      'sh': 'shell',
      'bash': 'shell',
      'properties': 'properties',
      'fml': 'javascript', // Use JS highlighting for FML
    }
    return languageMap[ext || ''] || 'plaintext'
  }

  return (
    <div className="tabbed-editor">
      {/* Tab Bar */}
      <div className="tab-bar">
        {tabs.map(tab => (
          <div
            key={tab.id}
            className={`tab ${tab.id === activeTabId ? 'active' : ''} ${tab.isFamiliar ? 'familiar-tab' : ''}`}
            onClick={() => onTabChange(tab.id)}
          >
            <span className="tab-icon">
              {tab.isFamiliar ? <Sparkles size={12} /> : <FileCode size={12} />}
            </span>
            <span className="tab-title">
              {tab.title}
              {tab.isModified && <span className="modified-dot">*</span>}
            </span>
            {!tab.isFamiliar && (
              <button
                className="tab-close"
                onClick={(e) => {
                  e.stopPropagation()
                  onTabClose(tab.id)
                }}
              >
                <X size={12} />
              </button>
            )}
          </div>
        ))}
      </div>

      {/* Editor */}
      <div className="editor-content">
        {activeTab ? (
          <MonacoEditor
            key={activeTab.id}
            height="100%"
            language={activeTab.isFamiliar ? 'javascript' : getLanguageForFile(activeTab.title)}
            theme="vs-dark"
            value={activeTab.content}
            onChange={value => onContentChange(activeTab.id, value || '')}
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
              readOnly: !activeTab.isFamiliar && !!activeTab.filePath, // Project files are read-only
            }}
          />
        ) : (
          <div className="no-editor">
            <p>No file open</p>
          </div>
        )}
      </div>
    </div>
  )
}

export default TabbedEditor
