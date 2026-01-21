import { useState, useEffect } from 'react'

const STORAGE_KEY = 'pokemon-rankings-auth'

function readFromStorage() {
  if (typeof window === 'undefined') return { token: null, username: null }
  try {
    const raw = window.localStorage.getItem(STORAGE_KEY)
    return raw ? JSON.parse(raw) : { token: null, username: null }
  } catch {
    return { token: null, username: null }
  }
}

export function useAuth() {
  // Initialize with null to avoid hydration mismatch
  const [state, setState] = useState(() => {
    // Only read from localStorage after component mounts (client-side)
    if (typeof window !== 'undefined') {
      return readFromStorage()
    }
    return { token: null, username: null }
  })
  
  const [isInitialized, setIsInitialized] = useState(false)

  useEffect(() => {
    // Read from storage after mount to avoid hydration issues
    setState(readFromStorage())
    setIsInitialized(true)

    // Listen for storage changes (e.g., from other tabs)
    const handleStorageChange = (e) => {
      if (e.key === STORAGE_KEY || e.key === null) {
        setState(readFromStorage())
      }
    }
    window.addEventListener('storage', handleStorageChange)
    return () => window.removeEventListener('storage', handleStorageChange)
  }, [])

  const isAuthenticated = Boolean(state.token)

  const login = ({ token, username }) => {
    const next = { token, username }
    window.localStorage.setItem(STORAGE_KEY, JSON.stringify(next))
    setState(next)
    // Trigger storage event for other tabs
    window.dispatchEvent(new StorageEvent('storage', { key: STORAGE_KEY }))
  }

  const logout = () => {
    window.localStorage.removeItem(STORAGE_KEY)
    setState({ token: null, username: null })
    // Trigger storage event for other tabs
    window.dispatchEvent(new StorageEvent('storage', { key: STORAGE_KEY }))
  }

  return {
    ...state,
    isAuthenticated,
    isInitialized,
    login,
    logout,
  }
}

export function getAuthToken() {
  const { token } = readFromStorage()
  return token
}


