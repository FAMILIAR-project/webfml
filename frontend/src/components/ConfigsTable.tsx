import { useState, useEffect } from 'react'
import { X, Download, Check } from 'lucide-react'
import { familiarApi, ConfigurationsData } from '@/api/client'
import './ConfigsTable.css'

interface ConfigsTableProps {
  variableId: string
  onClose: () => void
}

const ConfigsTable: React.FC<ConfigsTableProps> = ({ variableId, onClose }) => {
  const [data, setData] = useState<ConfigurationsData | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [limit, setLimit] = useState(100)

  const loadConfigurations = async (configLimit: number) => {
    try {
      setLoading(true)
      setError(null)
      const result = await familiarApi.getConfigurations(variableId, configLimit)
      setData(result)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load configurations')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadConfigurations(limit)
  }, [variableId])

  const handleLimitChange = (newLimit: number) => {
    setLimit(newLimit)
    loadConfigurations(newLimit)
  }

  const exportCSV = () => {
    if (!data) return

    const headers = ['#', ...data.features]
    const rows = data.configurations.map((config, idx) => {
      return [idx + 1, ...data.features.map(f => config[f] ? 'true' : 'false')]
    })

    const csvContent = [
      headers.join(','),
      ...rows.map(row => row.join(','))
    ].join('\n')

    const blob = new Blob([csvContent], { type: 'text/csv' })
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `${variableId}_configurations.csv`
    a.click()
    URL.revokeObjectURL(url)
  }

  if (loading) {
    return (
      <div className="configs-table-container">
        <div className="configs-header">
          <h3>Configurations: {variableId}</h3>
          <button onClick={onClose} className="configs-close-btn"><X size={18} /></button>
        </div>
        <div className="configs-loading">Loading configurations...</div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="configs-table-container">
        <div className="configs-header">
          <h3>Configurations: {variableId}</h3>
          <button onClick={onClose} className="configs-close-btn"><X size={18} /></button>
        </div>
        <div className="configs-error">{error}</div>
      </div>
    )
  }

  if (!data || data.configurations.length === 0) {
    return (
      <div className="configs-table-container">
        <div className="configs-header">
          <h3>Configurations: {variableId}</h3>
          <button onClick={onClose} className="configs-close-btn"><X size={18} /></button>
        </div>
        <div className="configs-empty">No configurations found</div>
      </div>
    )
  }

  return (
    <div className="configs-table-container">
      <div className="configs-header">
        <div className="configs-header-left">
          <h3>Configurations: {variableId}</h3>
          <span className="configs-count">{data.totalCount} configuration{data.totalCount !== 1 ? 's' : ''}</span>
        </div>
        <div className="configs-header-actions">
          <div className="configs-limit">
            <label>Limit:</label>
            <select value={limit} onChange={(e) => handleLimitChange(Number(e.target.value))}>
              <option value={10}>10</option>
              <option value={50}>50</option>
              <option value={100}>100</option>
              <option value={500}>500</option>
              <option value={1000}>1000</option>
              <option value={0}>All</option>
            </select>
          </div>
          <button onClick={exportCSV} className="configs-export-btn" title="Export as CSV">
            <Download size={14} />
            Export CSV
          </button>
          <button onClick={onClose} className="configs-close-btn"><X size={18} /></button>
        </div>
      </div>

      <div className="configs-table-wrapper">
        <table className="configs-table">
          <thead>
            <tr>
              <th className="config-index">#</th>
              {data.features.map((feature) => (
                <th key={feature} className="feature-header">{feature}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {data.configurations.map((config, idx) => (
              <tr key={idx}>
                <td className="config-index">{idx + 1}</td>
                {data.features.map((feature) => (
                  <td key={feature} className={`config-cell ${config[feature] ? 'selected' : 'not-selected'}`}>
                    {config[feature] ? <Check size={14} /> : ''}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}

export default ConfigsTable
