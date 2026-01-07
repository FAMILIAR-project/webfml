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
}

// Workspace API
export interface FileTreeNode {
  label: string
  type: 'file' | 'folder'
  leaf: boolean
  expanded?: boolean
  children?: FileTreeNode[]
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
