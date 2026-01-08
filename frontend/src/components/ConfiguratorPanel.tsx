import { useState, useEffect } from 'react'
import { configurationApi, ConfigurationState, ConfigFeatureNode, ConfigFeatureGroup } from '@/api/client'
import './ConfiguratorPanel.css'

interface ConfiguratorPanelProps {
  variableId: string
  onClose: () => void
  onSaved: () => void
}

const ConfiguratorPanel: React.FC<ConfiguratorPanelProps> = ({ variableId, onClose, onSaved }) => {
  const [state, setState] = useState<ConfigurationState | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [saving, setSaving] = useState(false)
  const [newVarName, setNewVarName] = useState('')

  useEffect(() => {
    const startConfiguration = async () => {
      try {
        setLoading(true)
        setError(null)
        const data = await configurationApi.start(variableId)
        setState(data)
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to start configuration')
      } finally {
        setLoading(false)
      }
    }
    startConfiguration()
  }, [variableId])

  const handleSelect = async (feature: string) => {
    if (!state) return
    try {
      const currentState = getFeatureState(feature)
      let newState: ConfigurationState

      // Cycle: unselected -> selected -> deselected -> unselected
      if (currentState === 'unselected') {
        newState = await configurationApi.select(feature)
      } else if (currentState === 'selected') {
        newState = await configurationApi.deselect(feature)
      } else {
        newState = await configurationApi.unselect(feature)
      }
      setState(newState)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Selection failed')
    }
  }

  const getFeatureState = (feature: string): 'selected' | 'deselected' | 'unselected' => {
    if (!state) return 'unselected'
    if (state.selected.includes(feature)) return 'selected'
    if (state.deselected.includes(feature)) return 'deselected'
    return 'unselected'
  }

  const handleAutoComplete = async (mode: 'MAX' | 'MIN' | 'RANDOM') => {
    try {
      setLoading(true)
      const newState = await configurationApi.autoComplete(mode)
      setState(newState)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Auto-complete failed')
    } finally {
      setLoading(false)
    }
  }

  const handleSave = async () => {
    try {
      setSaving(true)
      await configurationApi.save(newVarName || undefined)
      onSaved()
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Save failed')
    } finally {
      setSaving(false)
    }
  }

  if (loading && !state) {
    return (
      <div className="config-panel">
        <div className="config-header">
          <h3>Configuration: {variableId}</h3>
          <button onClick={onClose} className="config-close-btn">&times;</button>
        </div>
        <div className="config-loading">Loading configuration...</div>
      </div>
    )
  }

  if (error && !state) {
    return (
      <div className="config-panel">
        <div className="config-header">
          <h3>Configuration: {variableId}</h3>
          <button onClick={onClose} className="config-close-btn">&times;</button>
        </div>
        <div className="config-error">{error}</div>
      </div>
    )
  }

  if (!state || !state.tree) {
    return (
      <div className="config-panel">
        <div className="config-header">
          <h3>Configuration: {variableId}</h3>
          <button onClick={onClose} className="config-close-btn">&times;</button>
        </div>
        <div className="config-error">No configuration available</div>
      </div>
    )
  }

  return (
    <div className="config-panel">
      <div className="config-header">
        <h3>Configuration: {variableId}</h3>
        <div className="config-status">
          <span className={`status-indicator ${state.valid ? 'valid' : 'invalid'}`}>
            {state.valid ? 'Valid' : 'Invalid'}
          </span>
          <span className={`status-indicator ${state.complete ? 'complete' : 'incomplete'}`}>
            {state.complete ? 'Complete' : 'Incomplete'}
          </span>
        </div>
        <button onClick={onClose} className="config-close-btn">&times;</button>
      </div>

      {error && <div className="config-warning">{error}</div>}

      <div className="config-toolbar">
        <div className="auto-complete-section">
          <span>Auto-complete:</span>
          <button onClick={() => handleAutoComplete('MAX')} disabled={loading}>Max</button>
          <button onClick={() => handleAutoComplete('MIN')} disabled={loading}>Min</button>
          <button onClick={() => handleAutoComplete('RANDOM')} disabled={loading}>Random</button>
        </div>
        <div className="save-section">
          <input
            type="text"
            placeholder="New variable name"
            value={newVarName}
            onChange={(e) => setNewVarName(e.target.value)}
          />
          <button onClick={handleSave} disabled={saving || !state.valid}>
            {saving ? 'Saving...' : 'Save'}
          </button>
        </div>
      </div>

      <div className="config-legend">
        <span className="legend-item">
          <span className="checkbox-sample selected"></span> Selected
        </span>
        <span className="legend-item">
          <span className="checkbox-sample deselected"></span> Deselected
        </span>
        <span className="legend-item">
          <span className="checkbox-sample unselected"></span> Unselected
        </span>
      </div>

      <div className="config-tree-content">
        <ConfigFeatureNodeComponent
          node={state.tree}
          isRoot={true}
          onSelect={handleSelect}
          getFeatureState={getFeatureState}
        />

        {state.constraints && state.constraints.length > 0 && (
          <div className="config-constraints-box">
            <div className="config-constraints-header">Constraints</div>
            <ul className="config-constraints-list">
              {state.constraints.map((constraint, idx) => (
                <li key={idx} className="config-constraint-item">{constraint}</li>
              ))}
            </ul>
          </div>
        )}
      </div>
    </div>
  )
}

interface ConfigFeatureNodeComponentProps {
  node: ConfigFeatureNode
  isRoot?: boolean
  isMandatory?: boolean
  isGroupMember?: boolean
  groupType?: 'or' | 'xor' | 'mutex'
  onSelect: (feature: string) => void
  getFeatureState: (feature: string) => 'selected' | 'deselected' | 'unselected'
}

const ConfigFeatureNodeComponent: React.FC<ConfigFeatureNodeComponentProps> = ({
  node,
  isRoot = false,
  isMandatory = false,
  isGroupMember = false,
  groupType,
  onSelect,
  getFeatureState
}) => {
  const featureState = getFeatureState(node.name)
  const hasChildren = (
    node.mandatory?.length > 0 ||
    node.optional?.length > 0 ||
    node.orGroups?.length > 0 ||
    node.xorGroups?.length > 0 ||
    node.mutexGroups?.length > 0
  )

  const handleClick = () => {
    onSelect(node.name)
  }

  return (
    <div className="config-node-wrapper">
      <div className={`config-node ${isRoot ? 'config-root' : ''}`}>
        <button
          className={`config-checkbox ${featureState}`}
          onClick={handleClick}
          title={`Click to change state (current: ${featureState})`}
        >
          {featureState === 'selected' && <span className="check-icon">&#10003;</span>}
          {featureState === 'deselected' && <span className="cross-icon">&#10007;</span>}
        </button>

        {!isRoot && !isGroupMember && (
          <span className={`config-indicator ${isMandatory ? 'mandatory' : 'optional'}`}>
            {isMandatory ? '(M)' : '(O)'}
          </span>
        )}
        {isGroupMember && groupType && (
          <span className={`config-indicator group-member ${groupType}`}>
            ({groupType.toUpperCase()})
          </span>
        )}

        <span className={`config-feature-name ${featureState}`}>{node.name}</span>
      </div>

      {hasChildren && (
        <div className="config-children">
          {node.mandatory?.map((child, idx) => (
            <div key={`mand-${idx}`} className="config-child mandatory-child">
              <div className="config-connector">
                <div className="config-line-v"></div>
                <div className="config-line-h"></div>
              </div>
              <ConfigFeatureNodeComponent
                node={child}
                isMandatory={true}
                onSelect={onSelect}
                getFeatureState={getFeatureState}
              />
            </div>
          ))}

          {node.optional?.map((child, idx) => (
            <div key={`opt-${idx}`} className="config-child optional-child">
              <div className="config-connector">
                <div className="config-line-v"></div>
                <div className="config-line-h"></div>
              </div>
              <ConfigFeatureNodeComponent
                node={child}
                isMandatory={false}
                onSelect={onSelect}
                getFeatureState={getFeatureState}
              />
            </div>
          ))}

          {node.orGroups?.map((group, gidx) => (
            <ConfigFeatureGroupComponent
              key={`or-${gidx}`}
              group={group}
              onSelect={onSelect}
              getFeatureState={getFeatureState}
            />
          ))}

          {node.xorGroups?.map((group, gidx) => (
            <ConfigFeatureGroupComponent
              key={`xor-${gidx}`}
              group={group}
              onSelect={onSelect}
              getFeatureState={getFeatureState}
            />
          ))}

          {node.mutexGroups?.map((group, gidx) => (
            <ConfigFeatureGroupComponent
              key={`mutex-${gidx}`}
              group={group}
              onSelect={onSelect}
              getFeatureState={getFeatureState}
            />
          ))}
        </div>
      )}
    </div>
  )
}

interface ConfigFeatureGroupComponentProps {
  group: ConfigFeatureGroup
  onSelect: (feature: string) => void
  getFeatureState: (feature: string) => 'selected' | 'deselected' | 'unselected'
}

const ConfigFeatureGroupComponent: React.FC<ConfigFeatureGroupComponentProps> = ({
  group,
  onSelect,
  getFeatureState
}) => {
  const getCardinalityLabel = () => {
    switch (group.type) {
      case 'or': return '1..n'
      case 'xor': return '1..1'
      case 'mutex': return '0..1'
    }
  }

  return (
    <div className={`config-group config-group-${group.type}`}>
      <div className="config-group-connector">
        <div className="config-line-v"></div>
        <div className={`config-arc config-arc-${group.type}`}>
          <span className="arc-cardinality">{getCardinalityLabel()}</span>
        </div>
      </div>
      <div className="config-group-members">
        {group.members.map((member, idx) => (
          <div key={idx} className="config-group-member">
            <div className="config-connector">
              <div className="config-line-h"></div>
            </div>
            <ConfigFeatureNodeComponent
              node={member}
              isGroupMember={true}
              groupType={group.type}
              onSelect={onSelect}
              getFeatureState={getFeatureState}
            />
          </div>
        ))}
      </div>
    </div>
  )
}

export default ConfiguratorPanel
