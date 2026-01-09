import { useState, useRef, useEffect } from 'react'
import { Terminal, Trash2 } from 'lucide-react'
import { familiarApi } from '@/api/client'
import './Console.css'

interface ConsoleMessage {
  type: 'input' | 'output' | 'error'
  content: string
  timestamp: Date
}

const Console: React.FC = () => {
  const [messages, setMessages] = useState<ConsoleMessage[]>([
    {
      type: 'output',
      content: 'FAMILIAR Console - Type commands and press Enter',
      timestamp: new Date(),
    },
  ])
  const [input, setInput] = useState('')
  const [history, setHistory] = useState<string[]>([])
  const [historyIndex, setHistoryIndex] = useState(-1)
  const consoleEndRef = useRef<HTMLDivElement>(null)
  const inputRef = useRef<HTMLInputElement>(null)

  useEffect(() => {
    consoleEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  const addMessage = (type: ConsoleMessage['type'], content: string) => {
    setMessages(prev => [...prev, { type, content, timestamp: new Date() }])
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!input.trim()) return

    // Add input to messages
    addMessage('input', input)

    // Add to history
    setHistory(prev => [...prev, input])
    setHistoryIndex(-1)

    try {
      // Execute command
      const response = await familiarApi.evalPrompt(input)

      // Display result (configs are already formatted by backend)
      if (response.lastVar) {
        addMessage('output', response.lastVar)
      }

      // Display variables
      if (response.varIds && response.varIds.length > 0) {
        addMessage('output', `Variables: ${response.varIds.join(', ')}`)
      }
    } catch (error: any) {
      const errorMsg = error.response?.data?.msgError || error.message || 'Unknown error'
      addMessage('error', errorMsg)
    }

    setInput('')
  }

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'ArrowUp') {
      e.preventDefault()
      if (history.length > 0) {
        const newIndex = historyIndex === -1 ? history.length - 1 : Math.max(0, historyIndex - 1)
        setHistoryIndex(newIndex)
        setInput(history[newIndex])
      }
    } else if (e.key === 'ArrowDown') {
      e.preventDefault()
      if (historyIndex !== -1) {
        const newIndex = Math.min(history.length - 1, historyIndex + 1)
        setHistoryIndex(newIndex)
        setInput(newIndex === history.length ? '' : history[newIndex])
      }
    }
  }

  const handleClear = () => {
    setMessages([
      {
        type: 'output',
        content: 'Console cleared',
        timestamp: new Date(),
      },
    ])
  }

  return (
    <div className="console">
      <div className="console-header">
        <div className="flex items-center gap-2">
          <Terminal size={16} />
          <span>Console</span>
        </div>
        <button
          onClick={handleClear}
          className="console-clear-btn"
          title="Clear console"
        >
          <Trash2 size={16} />
        </button>
      </div>

      <div className="console-messages">
        {messages.map((msg, index) => (
          <div key={index} className={`console-message console-message-${msg.type}`}>
            <span className="console-message-prefix">
              {msg.type === 'input' ? '> ' : msg.type === 'error' ? '✗ ' : ''}
            </span>
            <span className="console-message-content">{msg.content}</span>
          </div>
        ))}
        <div ref={consoleEndRef} />
      </div>

      <form onSubmit={handleSubmit} className="console-input-container">
        <span className="console-prompt">&gt;</span>
        <input
          ref={inputRef}
          type="text"
          value={input}
          onChange={e => setInput(e.target.value)}
          onKeyDown={handleKeyDown}
          className="console-input"
          placeholder="Enter FAMILIAR command..."
          autoFocus
        />
      </form>
    </div>
  )
}

export default Console
