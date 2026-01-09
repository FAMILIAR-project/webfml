import { useState, useEffect } from 'react'
import { Search } from 'lucide-react'
import { familiarApi, FeatureNode, FeatureGroup, FeatureModelStructure } from '@/api/client'
import './FeatureModelTree.css'

interface FeatureModelTreeProps {
  variableId: string
  onClose: () => void
}

interface AnalysisState {
  isValid: boolean
  deadFeatures: Set<string>
  falseOptionals: Set<string>
}

const FeatureModelTree: React.FC<FeatureModelTreeProps> = ({ variableId, onClose }) => {
  const [structure, setStructure] = useState<FeatureModelStructure | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [analyzing, setAnalyzing] = useState(false)
  const [analysis, setAnalysis] = useState<AnalysisState | null>(null)

  useEffect(() => {
    const loadStructure = async () => {
      try {
        setLoading(true)
        setError(null)
        const data = await familiarApi.getFeatureModelStructure(variableId)
        setStructure(data)
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to load feature model')
      } finally {
        setLoading(false)
      }
    }
    loadStructure()
  }, [variableId])

  const handleAnalyze = async () => {
    try {
      setAnalyzing(true)
      const result = await familiarApi.analyzeFeatureModel(variableId)
      setAnalysis({
        isValid: result.isValid,
        deadFeatures: new Set(result.deadFeatures),
        falseOptionals: new Set(result.falseOptionals),
      })
    } catch (err) {
      console.error('Analysis failed:', err)
    } finally {
      setAnalyzing(false)
    }
  }

  const clearAnalysis = () => {
    setAnalysis(null)
  }

  if (loading) {
    return (
      <div className="fm-tree-container">
        <div className="fm-tree-header">
          <h3>Feature Model: {variableId}</h3>
          <button onClick={onClose} className="fm-close-btn">&times;</button>
        </div>
        <div className="fm-tree-loading">Loading feature model...</div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="fm-tree-container">
        <div className="fm-tree-header">
          <h3>Feature Model: {variableId}</h3>
          <button onClick={onClose} className="fm-close-btn">&times;</button>
        </div>
        <div className="fm-tree-error">{error}</div>
      </div>
    )
  }

  if (!structure || !structure.tree) {
    return (
      <div className="fm-tree-container">
        <div className="fm-tree-header">
          <h3>Feature Model: {variableId}</h3>
          <button onClick={onClose} className="fm-close-btn">&times;</button>
        </div>
        <div className="fm-tree-error">No feature model structure available</div>
      </div>
    )
  }

  return (
    <div className="fm-tree-container">
      <div className="fm-tree-header">
        <div className="fm-header-left">
          <h3>Feature Model: {variableId}</h3>
          {analysis && (
            <span className={`validity-indicator ${analysis.isValid ? 'valid' : 'invalid'}`}>
              {analysis.isValid ? 'Valid' : 'Invalid'}
            </span>
          )}
        </div>
        <div className="fm-header-actions">
          {!analysis ? (
            <button
              onClick={handleAnalyze}
              className="fm-analyze-btn"
              disabled={analyzing}
            >
              <Search size={14} />
              {analyzing ? 'Analyzing...' : 'Analyze'}
            </button>
          ) : (
            <button onClick={clearAnalysis} className="fm-clear-analysis-btn">
              Clear Analysis
            </button>
          )}
          <button onClick={onClose} className="fm-close-btn">&times;</button>
        </div>
      </div>

      <div className="fm-legend-bar">
        <div className="fm-legend">
          <span className="legend-item"><span className="mandatory-indicator">●</span> Mandatory</span>
          <span className="legend-item"><span className="optional-indicator">○</span> Optional</span>
          <span className="legend-item"><span className="or-arc">◗</span> OR (1..n)</span>
          <span className="legend-item"><span className="xor-arc">◖</span> XOR (1..1)</span>
          <span className="legend-item"><span className="mutex-arc">◖</span> MUTEX (0..1)</span>
        </div>
        {analysis && (
          <div className="fm-analysis-legend">
            <span className="legend-item"><span className="dead-indicator"></span> Dead Feature</span>
            <span className="legend-item"><span className="false-optional-indicator"></span> False Optional</span>
          </div>
        )}
      </div>

      <div className="fm-tree-content">
        <FeatureNodeComponent node={structure.tree} isRoot={true} analysis={analysis} />

        {/* Constraints box */}
        {structure.constraints && structure.constraints.length > 0 && (
          <div className="fm-constraints-box">
            <div className="fm-constraints-header">Constraints</div>
            <ul className="fm-constraints-list">
              {structure.constraints.map((constraint, idx) => (
                <li key={idx} className="fm-constraint-item">{constraint}</li>
              ))}
            </ul>
          </div>
        )}
      </div>
    </div>
  )
}

interface FeatureNodeComponentProps {
  node: FeatureNode
  isRoot?: boolean
  isMandatory?: boolean
  isGroupMember?: boolean
  groupType?: 'or' | 'xor' | 'mutex'
  analysis?: AnalysisState | null
}

const FeatureNodeComponent: React.FC<FeatureNodeComponentProps> = ({
  node,
  isRoot = false,
  isMandatory = false,
  isGroupMember = false,
  groupType,
  analysis
}) => {
  const hasChildren = (
    node.mandatory?.length > 0 ||
    node.optional?.length > 0 ||
    node.orGroups?.length > 0 ||
    node.xorGroups?.length > 0 ||
    node.mutexGroups?.length > 0
  )

  const isDead = analysis?.deadFeatures.has(node.name) ?? false
  const isFalseOptional = analysis?.falseOptionals.has(node.name) ?? false

  const getFeatureClass = () => {
    if (isDead) return 'dead-feature'
    if (isFalseOptional) return 'false-optional-feature'
    return ''
  }

  return (
    <div className="fm-node-wrapper">
      <div className={`fm-node ${isRoot ? 'fm-root' : ''} ${getFeatureClass()}`}>
        {!isRoot && !isGroupMember && (
          <span className={`fm-indicator ${isMandatory ? 'mandatory' : 'optional'}`}>
            {isMandatory ? '●' : '○'}
          </span>
        )}
        {isGroupMember && groupType && (
          <span className={`fm-indicator group-member ${groupType}`}>
            {groupType === 'or' ? '◗' : '◖'}
          </span>
        )}
        {isGroupMember && groupType === 'mutex' && (
          <span className="fm-cardinality">[0..1]</span>
        )}
        <span className={`fm-feature-name ${getFeatureClass()}`}>{node.name}</span>
      </div>

      {hasChildren && (
        <div className="fm-children">
          {/* Mandatory children */}
          {node.mandatory?.map((child, idx) => (
            <div key={`mand-${idx}`} className="fm-child mandatory-child">
              <div className="fm-connector">
                <div className="fm-line-v"></div>
                <div className="fm-line-h"></div>
              </div>
              <FeatureNodeComponent node={child} isMandatory={true} analysis={analysis} />
            </div>
          ))}

          {/* Optional children */}
          {node.optional?.map((child, idx) => (
            <div key={`opt-${idx}`} className="fm-child optional-child">
              <div className="fm-connector">
                <div className="fm-line-v"></div>
                <div className="fm-line-h"></div>
              </div>
              <FeatureNodeComponent node={child} isMandatory={false} analysis={analysis} />
            </div>
          ))}

          {/* OR groups */}
          {node.orGroups?.map((group, gidx) => (
            <FeatureGroupComponent key={`or-${gidx}`} group={group} analysis={analysis} />
          ))}

          {/* XOR groups */}
          {node.xorGroups?.map((group, gidx) => (
            <FeatureGroupComponent key={`xor-${gidx}`} group={group} analysis={analysis} />
          ))}

          {/* MUTEX groups */}
          {node.mutexGroups?.map((group, gidx) => (
            <FeatureGroupComponent key={`mutex-${gidx}`} group={group} analysis={analysis} />
          ))}
        </div>
      )}
    </div>
  )
}

interface FeatureGroupComponentProps {
  group: FeatureGroup
  analysis?: AnalysisState | null
}

const FeatureGroupComponent: React.FC<FeatureGroupComponentProps> = ({ group, analysis }) => {
  const getArcSymbol = () => {
    switch (group.type) {
      case 'or': return '◗'
      case 'xor': return '◖'
      case 'mutex': return '◗' // Filled arc like OR, but with 0..1 cardinality shown
    }
  }

  const getCardinalityLabel = () => {
    switch (group.type) {
      case 'or': return '1..n'
      case 'xor': return '1..1'
      case 'mutex': return '0..1'
    }
  }

  return (
    <div className={`fm-group fm-group-${group.type}`}>
      <div className="fm-group-connector">
        <div className="fm-line-v"></div>
        <div className={`fm-arc fm-arc-${group.type}`}>
          <span className="arc-symbol">{getArcSymbol()}</span>
          <span className="arc-cardinality">{getCardinalityLabel()}</span>
        </div>
      </div>
      <div className="fm-group-members">
        {group.members.map((member, idx) => (
          <div key={idx} className="fm-group-member">
            <div className="fm-connector">
              <div className="fm-line-h"></div>
            </div>
            <FeatureNodeComponent
              node={member}
              isGroupMember={true}
              groupType={group.type}
              analysis={analysis}
            />
          </div>
        ))}
      </div>
    </div>
  )
}

export default FeatureModelTree
