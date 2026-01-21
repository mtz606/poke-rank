import { getAuthToken } from '../hooks/useAuth'

export const API_BASE =
  import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

async function request(path, { method = 'GET', body, headers } = {}) {
  const token = getAuthToken()

  const mergedHeaders = {
    'Content-Type': 'application/json',
    ...(headers || {}),
  }

  if (token) {
    mergedHeaders.Authorization = `Bearer ${token}`
  }

  const res = await fetch(`${API_BASE}${path}`, {
    method,
    headers: mergedHeaders,
    body: body ? JSON.stringify(body) : undefined,
  })

  if (!res.ok) {
    const text = await res.text()
    const message = text || `Request failed with status ${res.status}`
    throw new Error(message)
  }

  if (res.status === 204) return null

  return res.json()
}

export default request


