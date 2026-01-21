import React from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'

function NavBar() {
  const { isAuthenticated, username, logout } = useAuth()
  const location = useLocation()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login', { replace: true })
  }

  const atAuthPage =
    location.pathname.startsWith('/login') ||
    location.pathname.startsWith('/signup')

  return (
    <header className="navbar">
      <div className="navbar-left">
        <Link to="/" className="navbar-brand">
          Pokemon Rankings
        </Link>
        {isAuthenticated && (
          <nav className="navbar-links">
            <Link to="/home">Home</Link>
            <Link to="/groups">Groups</Link>
            <Link to="/collection">My Collection</Link>
          </nav>
        )}
      </div>
      <div className="navbar-right">
        {isAuthenticated ? (
          <>
            <span className="navbar-username">Signed in as {username}</span>
            <button className="btn secondary small" onClick={handleLogout}>
              Logout
            </button>
          </>
        ) : !atAuthPage ? (
          <div className="navbar-auth-links">
            <Link to="/login">Login</Link>
            <Link to="/signup">Sign Up</Link>
          </div>
        ) : null}
      </div>
    </header>
  )
}

export default NavBar


