import { useState, useEffect } from 'react'
import {
  X,
  Undo2,
  Redo2,
  Save,
  CheckCircle,
  ChevronDown,
  ChevronRight,
  Crown,
  XCircle,
} from 'lucide-react'
import { ksynthesisApi, KSynthesisState } from '@/api/client'
import './KSynthesisPanel.css'

interface KSynthesisPanelProps {
  variableId: string
  onClose: () => void
  onSaved: () => void
}

const KSynthesisPanel: React.FC<KSynthesisPanelProps> = ({ variableId, onClose, onSaved }) => {
  const [state, setState] = useState<KSynthesisState | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [expandedSections, setExpandedSections] = useState({
    rankingLists: true,
    clusters: true,
    cliques: false,
  })
  const [heuristics, setHeuristics] = useState<string[]>([])
  const [selectedHeuristic, setSelectedHeuristic] = useState('SmithWaterman')

  useEffect(() => {
    startSynthesis()
    loadHeuristics()
  }, [variableId])

  const startSynthesis = async () => {
    setLoading(true)
    setError(null)
    try {
      const result = await ksynthesisApi.start(variableId)
      setState(result)
    } catch (err: any) {
      setError(err.response?.data?.error || err.message)
    } finally {
      setLoading(false)
    }
  }

  const loadHeuristics = async () => {
    try {
      const result = await ksynthesisApi.getHeuristics()
      setHeuristics(result.heuristics)
      setSelectedHeuristic(result.defaultRankingHeuristic)
    } catch (err) {
      console.error('Failed to load heuristics:', err)
    }
  }

  const handleSelectParent = async (feature: string, parent: string) => {
    try {
      const result = await ksynthesisApi.selectParent([feature], parent)
      setState(result)
    } catch (err: any) {
      setError(err.response?.data?.error || err.message)
    }
  }

  const handleIgnoreParent = async (feature: string, parent: string) => {
    try {
      const result = await ksynthesisApi.ignoreParent(feature, parent)
      setState(result)
    } catch (err: any) {
      setError(err.response?.data?.error || err.message)
    }
  }

  const handleSetRoot = async (feature: string) => {
    try {
      const result = await ksynthesisApi.setRoot(feature)
      setState(result)
    } catch (err: any) {
      setError(err.response?.data?.error || err.message)
    }
  }

  const handleComplete = async () => {
    try {
      const result = await ksynthesisApi.complete()
      setState(result)
    } catch (err: any) {
      setError(err.response?.data?.error || err.message)
    }
  }

  const handleUndo = async () => {
    try {
      const result = await ksynthesisApi.undo()
      setState(result)
    } catch (err: any) {
      setError(err.response?.data?.error || err.message)
    }
  }

  const handleRedo = async () => {
    try {
      const result = await ksynthesisApi.redo()
      setState(result)
    } catch (err: any) {
      setError(err.response?.data?.error || err.message)
    }
  }

  const handleSave = async () => {
    try {
      await ksynthesisApi.save()
      onSaved()
      onClose()
    } catch (err: any) {
      setError(err.response?.data?.error || err.message)
    }
  }

  const handleHeuristicChange = async (heuristic: string) => {
    setSelectedHeuristic(heuristic)
    try {
      const result = await ksynthesisApi.setRankingHeuristic(heuristic)
      setState(result)
    } catch (err: any) {
      setError(err.response?.data?.error || err.message)
    }
  }

  const toggleSection = (section: keyof typeof expandedSections) => {
    setExpandedSections(prev => ({ ...prev, [section]: !prev[section] }))
  }

  if (loading) {
    return (
      <div className="ksynthesis-panel">
        <div className="ksynthesis-header">
          <h2>Interactive KSynthesis: {variableId}</h2>
          <button onClick={onClose} className="close-btn">
            <X size={20} />
          </button>
        </div>
        <div className="ksynthesis-loading">Loading synthesis data...</div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="ksynthesis-panel">
        <div className="ksynthesis-header">
          <h2>Interactive KSynthesis: {variableId}</h2>
          <button onClick={onClose} className="close-btn">
            <X size={20} />
          </button>
        </div>
        <div className="ksynthesis-error">Error: {error}</div>
      </div>
    )
  }

  return (
    <div className="ksynthesis-panel">
      <div className="ksynthesis-header">
        <h2>Interactive KSynthesis: {variableId}</h2>
        <div className="ksynthesis-actions">
          <button onClick={handleUndo} title="Undo">
            <Undo2 size={16} />
          </button>
          <button onClick={handleRedo} title="Redo">
            <Redo2 size={16} />
          </button>
          <button onClick={handleComplete} className="complete-btn" title="Complete FM automatically">
            <CheckCircle size={16} />
            <span>Complete</span>
          </button>
          <button onClick={handleSave} className="save-btn" title="Save to variable">
            <Save size={16} />
            <span>Save</span>
          </button>
          <button onClick={onClose} className="close-btn">
            <X size={20} />
          </button>
        </div>
      </div>

      <div className="ksynthesis-content">
        {/* Heuristic selector */}
        <div className="heuristic-selector">
          <label>Heuristic:</label>
          <select value={selectedHeuristic} onChange={e => handleHeuristicChange(e.target.value)}>
            {heuristics.map(h => (
              <option key={h} value={h}>
                {h}
              </option>
            ))}
          </select>
        </div>

        {/* FM Preview */}
        <div className="fm-preview">
          <h3>Feature Model Preview</h3>
          <div className="fm-stats">
            <span>{state?.fm.nodes.length || 0} features</span>
            <span>{state?.fm.edges.length || 0} edges</span>
          </div>
          <div className="fm-tree">
            {state?.fm.nodes.map(node => {
              const parentEdge = state.fm.edges.find(e => e.target === node)
              return (
                <div key={node} className="fm-node">
                  <span className="node-name">{node}</span>
                  {parentEdge && <span className="node-parent">← {parentEdge.source}</span>}
                </div>
              )
            })}
          </div>
        </div>

        {/* Ranking Lists */}
        <div className="section">
          <div className="section-header" onClick={() => toggleSection('rankingLists')}>
            {expandedSections.rankingLists ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
            <h3>Ranking Lists ({state?.rankingLists.length || 0})</h3>
          </div>
          {expandedSections.rankingLists && (
            <div className="ranking-lists">
              {state?.rankingLists.map(item => (
                <div key={item.feature} className="ranking-item">
                  <div className="ranking-feature">
                    <span className="feature-name">{item.feature}</span>
                    {item.isPossibleRoot && (
                      <button
                        onClick={() => handleSetRoot(item.feature)}
                        className="root-btn"
                        title="Set as root"
                      >
                        <Crown size={14} />
                      </button>
                    )}
                    {item.parentInFM && (
                      <span className="current-parent">current: {item.parentInFM}</span>
                    )}
                  </div>
                  <div className="parent-candidates">
                    {item.parents.slice(0, 5).map(parent => (
                      <div key={parent} className="parent-candidate">
                        <button
                          onClick={() => handleSelectParent(item.feature, parent)}
                          className="select-parent-btn"
                          title={`Select ${parent} as parent`}
                        >
                          <CheckCircle size={12} />
                        </button>
                        <span className="parent-name">{parent}</span>
                        <button
                          onClick={() => handleIgnoreParent(item.feature, parent)}
                          className="ignore-parent-btn"
                          title={`Ignore ${parent}`}
                        >
                          <XCircle size={12} />
                        </button>
                      </div>
                    ))}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Clusters */}
        <div className="section">
          <div className="section-header" onClick={() => toggleSection('clusters')}>
            {expandedSections.clusters ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
            <h3>Clusters ({state?.clusters.length || 0})</h3>
          </div>
          {expandedSections.clusters && (
            <div className="clusters">
              {state?.clusters.map((cluster, idx) => (
                <div key={idx} className="cluster">
                  <div className="cluster-features">
                    {cluster.map(f => (
                      <span key={f.name} className="cluster-feature">
                        {f.name}
                        {f.parentInFM && <span className="mini-parent">→{f.parentInFM}</span>}
                      </span>
                    ))}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Cliques */}
        <div className="section">
          <div className="section-header" onClick={() => toggleSection('cliques')}>
            {expandedSections.cliques ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
            <h3>Cliques ({state?.cliques.length || 0})</h3>
          </div>
          {expandedSections.cliques && (
            <div className="cliques">
              {state?.cliques.map((clique, idx) => (
                <div key={idx} className="clique">
                  {clique.map(f => (
                    <span key={f.name} className="clique-feature">
                      {f.name}
                    </span>
                  ))}
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default KSynthesisPanel
