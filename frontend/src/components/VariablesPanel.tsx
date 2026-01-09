import { useState, useEffect } from 'react'
import { Variable, Eye, Sparkles, RefreshCw, Settings, Table2, FolderOpen } from 'lucide-react'
import { familiarApi } from '@/api/client'
import './VariablesPanel.css'

interface VariableInfo {
  id: string
  value: string
  type: 'FeatureModel' | 'Configuration' | 'Set' | 'unknown'
}

interface VariablesPanelProps {
  onDisplayFM: (variableId: string, fmValue: string) => void
  onSynthesize: (variableId: string) => void
  onConfigure: (variableId: string) => void
  onShowConfigs: (variableId: string) => void
  onOpenProjects?: () => void
  onFeatureModelsChange?: (fmIds: string[]) => void
  onConfigurationsChange?: (configIds: string[]) => void
}

const VariablesPanel: React.FC<VariablesPanelProps> = ({ onDisplayFM, onSynthesize, onConfigure, onShowConfigs, onOpenProjects, onFeatureModelsChange, onConfigurationsChange }) => {
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
          const info = await familiarApi.getVariableInfo(id)
          varInfos.push({
            id: info.id,
            value: info.value,
            type: info.type as 'FeatureModel' | 'Configuration' | 'Set' | 'unknown'
          })
        } catch {
          varInfos.push({ id, value: '(error loading)', type: 'unknown' })
        }
      }

      setVariables(varInfos)

      // Report feature model IDs to parent
      const fmIds = varInfos.filter(v => v.type === 'FeatureModel').map(v => v.id)
      if (onFeatureModelsChange) {
        onFeatureModelsChange(fmIds)
      }

      // Report configuration IDs to parent
      const configIds = varInfos.filter(v => v.type === 'Configuration').map(v => v.id)
      if (onConfigurationsChange) {
        onConfigurationsChange(configIds)
      }
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

  const handleDisplayFM = (variable: VariableInfo) => {
    onDisplayFM(variable.id, variable.value)
  }

  const handleDisplayConfig = (variableId: string) => {
    // For configurations, open the configurator view
    onConfigure(variableId)
  }

  const handleSynthesize = (variableId: string) => {
    onSynthesize(variableId)
  }

  const handleConfigure = (variableId: string) => {
    onConfigure(variableId)
  }

  const toggleExpanded = (id: string) => {
    setExpanded(prev => ({ ...prev, [id]: !prev[id] }))
  }

  const getTypeLabel = (type: string) => {
    switch (type) {
      case 'FeatureModel': return 'FM'
      case 'Configuration': return 'Config'
      default: return null
    }
  }

  const getTypeClass = (type: string) => {
    switch (type) {
      case 'FeatureModel': return 'is-fm'
      case 'Configuration': return 'is-config'
      default: return ''
    }
  }

  return (
    <div className="variables-panel">
      <div className="variables-header">
        <div className="flex items-center gap-2">
          <Variable size={16} />
          <span>Variables</span>
        </div>
        <div className="header-actions">
          {onOpenProjects && (
            <button
              onClick={onOpenProjects}
              className="projects-btn"
              title="Manage projects"
            >
              <FolderOpen size={14} />
            </button>
          )}
          <button
            onClick={refreshVariables}
            className="refresh-btn"
            title="Refresh variables"
            disabled={loading}
          >
            <RefreshCw size={14} className={loading ? 'animate-spin' : ''} />
          </button>
        </div>
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
                <span className={`variable-name ${getTypeClass(variable.type)}`}>
                  {variable.id}
                </span>
                {getTypeLabel(variable.type) && (
                  <span className={`variable-type ${variable.type === 'Configuration' ? 'config-type' : ''}`}>
                    {getTypeLabel(variable.type)}
                  </span>
                )}
              </div>

              {expanded[variable.id] && (
                <div className="variable-details">
                  <pre className="variable-value">{variable.value}</pre>

                  {variable.type === 'FeatureModel' && (
                    <div className="variable-actions">
                      <button
                        onClick={() => handleDisplayFM(variable)}
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
                      <button
                        onClick={() => handleConfigure(variable.id)}
                        className="action-btn config-btn"
                        title="Configure feature model"
                      >
                        <Settings size={12} />
                        <span>Configure</span>
                      </button>
                      <button
                        onClick={() => onShowConfigs(variable.id)}
                        className="action-btn configs-btn"
                        title="Show all configurations"
                      >
                        <Table2 size={12} />
                        <span>Configs</span>
                      </button>
                    </div>
                  )}

                  {variable.type === 'Configuration' && (
                    <div className="variable-actions">
                      <button
                        onClick={() => handleDisplayConfig(variable.id)}
                        className="action-btn config-btn"
                        title="View/edit configuration"
                      >
                        <Settings size={12} />
                        <span>View Config</span>
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
