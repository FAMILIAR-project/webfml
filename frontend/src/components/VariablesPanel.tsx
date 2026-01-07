import { useState, useEffect } from 'react'
import { Variable, Eye, Sparkles, RefreshCw } from 'lucide-react'
import { familiarApi } from '@/api/client'
import './VariablesPanel.css'

interface VariableInfo {
  id: string
  value: string
  isFeatureModel: boolean
}

interface VariablesPanelProps {
  onDisplayFM: (variableId: string, fmValue: string) => void
  onSynthesize: (variableId: string) => void
}

const VariablesPanel: React.FC<VariablesPanelProps> = ({ onDisplayFM, onSynthesize }) => {
  const [variables, setVariables] = useState<VariableInfo[]>([])
  const [loading, setLoading] = useState(false)
  const [expanded, setExpanded] = useState<Record<string, boolean>>({})

  const refreshVariables = async () => {
    setLoading(true)
    try {
      const varIds = await familiarApi.getAllVariables()
      const varInfos: VariableInfo[] = []

      for (const id of varIds) {
        try {
          const value = await familiarApi.getVariable(id)
          // Check if it's a feature model (contains FM syntax patterns)
          const isFeatureModel = value.includes(':') && (value.includes(';') || value.includes('['))
          varInfos.push({ id, value, isFeatureModel })
        } catch {
          varInfos.push({ id, value: '(error loading)', isFeatureModel: false })
        }
      }

      setVariables(varInfos)
    } catch (error) {
      console.error('Failed to load variables:', error)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    refreshVariables()
    // Refresh every 2 seconds to keep in sync
    const interval = setInterval(refreshVariables, 2000)
    return () => clearInterval(interval)
  }, [])

  const handleDisplay = (variable: VariableInfo) => {
    onDisplayFM(variable.id, variable.value)
  }

  const handleSynthesize = (variableId: string) => {
    onSynthesize(variableId)
  }

  const toggleExpanded = (id: string) => {
    setExpanded(prev => ({ ...prev, [id]: !prev[id] }))
  }

  return (
    <div className="variables-panel">
      <div className="variables-header">
        <div className="flex items-center gap-2">
          <Variable size={16} />
          <span>Variables</span>
        </div>
        <button
          onClick={refreshVariables}
          className="refresh-btn"
          title="Refresh variables"
          disabled={loading}
        >
          <RefreshCw size={14} className={loading ? 'animate-spin' : ''} />
        </button>
      </div>

      <div className="variables-list">
        {variables.length === 0 ? (
          <div className="no-variables">No variables defined</div>
        ) : (
          variables.map(variable => (
            <div key={variable.id} className="variable-item">
              <div
                className="variable-header"
                onClick={() => toggleExpanded(variable.id)}
              >
                <span className={`variable-name ${variable.isFeatureModel ? 'is-fm' : ''}`}>
                  {variable.id}
                </span>
                {variable.isFeatureModel && (
                  <span className="variable-type">FM</span>
                )}
              </div>

              {expanded[variable.id] && (
                <div className="variable-details">
                  <pre className="variable-value">{variable.value}</pre>

                  {variable.isFeatureModel && (
                    <div className="variable-actions">
                      <button
                        onClick={() => handleDisplay(variable)}
                        className="action-btn display-btn"
                        title="Display feature model"
                      >
                        <Eye size={12} />
                        <span>Display</span>
                      </button>
                      <button
                        onClick={() => handleSynthesize(variable.id)}
                        className="action-btn synth-btn"
                        title="Run ksynthesis"
                      >
                        <Sparkles size={12} />
                        <span>Synthesize</span>
                      </button>
                    </div>
                  )}
                </div>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  )
}

export default VariablesPanel
