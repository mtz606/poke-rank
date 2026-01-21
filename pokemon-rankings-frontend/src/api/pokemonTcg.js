import { API_BASE } from './client'

export function searchCards(query, page = 1, pageSize = 10) {
  const params = new URLSearchParams({ q: query, page, pageSize })
  return fetch(`${API_BASE}/pokemon-tcg/search?${params.toString()}`)
    .then(async res => {
      if (!res.ok) {
        const errorData = await res.json().catch(() => ({ error: `Request failed with status ${res.status}` }))
        throw new Error(errorData.error || `Search failed: ${res.status}`)
      }
      return res.json()
    })
}

export function getCardById(cardId) {
  return fetch(`${API_BASE}/pokemon-tcg/cards/${cardId}`)
    .then(async res => {
      if (!res.ok) {
        const errorData = await res.json().catch(() => ({ error: `Request failed with status ${res.status}` }))
        throw new Error(errorData.error || `Card not found: ${res.status}`)
      }
      return res.json()
    })
}

