import React, { useEffect } from 'react'
import { Routes, Route, Navigate, Link, useLocation, useNavigate } from 'react-router-dom'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import HomePage from './pages/HomePage'
import GroupsPage from './pages/GroupsPage'
import GroupDetailPage from './pages/GroupDetailPage'
import MyCollectionPage from './pages/MyCollectionPage'
import AddCardPage from './pages/AddCardPage'
import { useAuth } from './hooks/useAuth'
import NavBar from './components/NavBar'

// Component to handle 404.html redirects from GitHub Pages
function GitHubPagesRedirect() {
  const navigate = useNavigate()
  const location = useLocation()

  useEffect(() => {
    // Handle redirect from 404.html (GitHub Pages SPA workaround)
    const search = location.search
    if (search && search[1] === '/') {
      const decoded = search
        .slice(1)
        .split('&')
        .map((s) => s.replace(/~and~/g, '&'))
        .join('?')
      navigate(decoded, { replace: true })
    }
  }, [location.search, navigate])

  return null
}

function RequireAuth({ children }) {
  const { isAuthenticated, isInitialized } = useAuth()
  const location = useLocation()

  // Wait for auth to initialize before redirecting
  if (!isInitialized) {
    return null // Or a loading spinner
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />
  }

  return children
}

function App() {
  const { isAuthenticated, isInitialized } = useAuth()

  // Don't render routes until auth is initialized to avoid hydration mismatch
  if (!isInitialized) {
    return (
      <div className="app-root">
        <NavBar />
        <main className="app-main">
          <div className="card centered">
            <p>Loading...</p>
          </div>
        </main>
      </div>
    )
  }

  return (
    <div className="app-root">
      <GitHubPagesRedirect />
      <NavBar />
      <main className="app-main">
        <Routes>
          <Route
            path="/"
            element={
              isAuthenticated ? (
                <Navigate to="/home" replace />
              ) : (
                <div className="card centered">
                  <h1>Pokemon Rankings</h1>
                  <p>Track Pokemon card collections and rank friends in groups.</p>
                  <div className="button-row">
                    <Link className="btn primary" to="/signup">
                      Get Started
                    </Link>
                    <Link className="btn secondary" to="/login">
                      Login
                    </Link>
                  </div>
                </div>
              )
            }
          />

          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<RegisterPage />} />

          <Route
            path="/home"
            element={
              <RequireAuth>
                <HomePage />
              </RequireAuth>
            }
          />

          <Route
            path="/groups"
            element={
              <RequireAuth>
                <GroupsPage />
              </RequireAuth>
            }
          />
          <Route
            path="/groups/:groupId"
            element={
              <RequireAuth>
                <GroupDetailPage />
              </RequireAuth>
            }
          />

          <Route
            path="/collection"
            element={
              <RequireAuth>
                <MyCollectionPage />
              </RequireAuth>
            }
          />
          <Route
            path="/collection/add"
            element={
              <RequireAuth>
                <AddCardPage />
              </RequireAuth>
            }
          />

          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>
    </div>
  )
}

export default App


