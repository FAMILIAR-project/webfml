import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
})

export interface InterpretRequest {
  command: string
}

export interface InterpretResponse {
  varIds: string[]
  lastVar: string
}

export interface ErrorResponse {
  msgError: string
}

// Feature Model Tree Structure
export interface FeatureNode {
  name: string
  mandatory: FeatureNode[]
  optional: FeatureNode[]
  orGroups: FeatureGroup[]
  xorGroups: FeatureGroup[]
  mutexGroups: FeatureGroup[]
}

export interface FeatureGroup {
  type: 'or' | 'xor' | 'mutex'
  members: FeatureNode[]
}

export interface FeatureModelStructure {
  variableId: string
  root: string
  tree: FeatureNode
  constraints: string[]
}

// FAMILIAR API
export const familiarApi = {
  interpret: async (command: string): Promise<InterpretResponse> => {
    const response = await api.post<InterpretResponse>('/familiar/interpret', { command })
    return response.data
  },

  evalPrompt: async (command: string): Promise<InterpretResponse> => {
    const response = await api.post<InterpretResponse>(
      '/familiar/eval-prompt',
      {},
      { params: { command } }
    )
    return response.data
  },

  getVariable: async (id: string): Promise<string> => {
    const response = await api.get<string>(`/familiar/variable/${id}`)
    return response.data
  },

  getAllVariables: async (): Promise<string[]> => {
    const response = await api.get<string[]>('/familiar/variables')
    return response.data
  },

  reset: async (): Promise<void> => {
    await api.post('/familiar/reset')
  },

  getKeywords: async (): Promise<string[]> => {
    const response = await api.get<string[]>('/familiar/keywords')
    return response.data
  },

  getFeatureModelStructure: async (variableId: string): Promise<FeatureModelStructure> => {
    const response = await api.get<FeatureModelStructure>(`/familiar/fm/${variableId}/structure`)
    return response.data
  },
}

// Workspace API
export interface FileTreeNode {
  label: string
  type: 'file' | 'folder'
  leaf: boolean
  expanded?: boolean
  children?: FileTreeNode[]
}

// KSynthesis API
export interface KSynthesisState {
  variableId: string
  fm: {
    nodes: string[]
    edges: { source: string; target: string }[]
  }
  rankingLists: {
    feature: string
    parents: string[]
    parentInFM: string | null
    originalParents?: string[]
    isPossibleRoot: boolean
  }[]
  clusters: { name: string; parentInFM: string | null }[][]
  cliques: { name: string; parentInFM: string | null }[][]
  active?: boolean
}

export const ksynthesisApi = {
  start: async (variableId: string): Promise<KSynthesisState> => {
    const response = await api.post<KSynthesisState>('/ksynthesis/start', {}, { params: { variableId } })
    return response.data
  },

  selectParent: async (children: string[], parent: string): Promise<KSynthesisState> => {
    const response = await api.post<KSynthesisState>('/ksynthesis/select-parent', { children, parent })
    return response.data
  },

  ignoreParent: async (child: string, parent: string): Promise<KSynthesisState> => {
    const response = await api.post<KSynthesisState>(
      '/ksynthesis/ignore-parent',
      {},
      { params: { child, parent } }
    )
    return response.data
  },

  setRoot: async (root: string): Promise<KSynthesisState> => {
    const response = await api.post<KSynthesisState>('/ksynthesis/set-root', {}, { params: { root } })
    return response.data
  },

  complete: async (): Promise<KSynthesisState> => {
    const response = await api.post<KSynthesisState>('/ksynthesis/complete')
    return response.data
  },

  undo: async (): Promise<KSynthesisState> => {
    const response = await api.post<KSynthesisState>('/ksynthesis/undo')
    return response.data
  },

  redo: async (): Promise<KSynthesisState> => {
    const response = await api.post<KSynthesisState>('/ksynthesis/redo')
    return response.data
  },

  save: async (newVariableId?: string): Promise<{ variableId: string; value: string }> => {
    const response = await api.post('/ksynthesis/save', {}, { params: { newVariableId } })
    return response.data
  },

  getHeuristics: async (): Promise<{
    heuristics: string[]
    defaultRankingHeuristic: string
    defaultClusteringHeuristic: string
    defaultThreshold: number
  }> => {
    const response = await api.get('/ksynthesis/heuristics')
    return response.data
  },

  setRankingHeuristic: async (heuristic: string): Promise<KSynthesisState> => {
    const response = await api.post<KSynthesisState>(
      '/ksynthesis/heuristic/ranking',
      {},
      { params: { heuristic } }
    )
    return response.data
  },

  setClusteringParameters: async (heuristic: string, threshold: number): Promise<KSynthesisState> => {
    const response = await api.post<KSynthesisState>(
      '/ksynthesis/heuristic/clustering',
      {},
      { params: { heuristic, threshold } }
    )
    return response.data
  },

  getState: async (): Promise<KSynthesisState> => {
    const response = await api.get<KSynthesisState>('/ksynthesis/state')
    return response.data
  },
}

export const workspaceApi = {
  listFiles: async (): Promise<FileTreeNode[]> => {
    const response = await api.get<FileTreeNode[]>('/workspace/files')
    return response.data
  },

  loadFile: async (filename: string): Promise<string> => {
    const response = await api.get<string>('/workspace/file', { params: { filename } })
    return response.data
  },

  saveFile: async (filename: string, content: string): Promise<void> => {
    await api.post('/workspace/file', content, { params: { filename } })
  },

  createFile: async (name: string): Promise<void> => {
    await api.post('/workspace/file/create', {}, { params: { name } })
  },

  deleteFile: async (name: string): Promise<void> => {
    await api.delete('/workspace/file', { params: { name } })
  },

  createFolder: async (name: string): Promise<void> => {
    await api.post('/workspace/folder', {}, { params: { name } })
  },

  deleteFolder: async (name: string): Promise<void> => {
    await api.delete('/workspace/folder', { params: { name } })
  },
}

export default api
