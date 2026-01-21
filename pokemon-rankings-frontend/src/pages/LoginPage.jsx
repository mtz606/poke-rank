import React, { useState } from 'react'
import { useLocation, useNavigate, Link } from 'react-router-dom'
import { login } from '../api/auth'
import { useAuth } from '../hooks/useAuth'

function LoginPage() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()
  const location = useLocation()
  const { login: setAuth } = useAuth()

  const handleSubmit = async e => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const res = await login({ username, password })
      setAuth({ token: res.token, username: res.username })
      const redirectTo = location.state?.from?.pathname || '/home'
      navigate(redirectTo, { replace: true })
    } catch (err) {
      setError(err.message || 'Login failed')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="card centered form-card">
      <h1>Login</h1>
      <form onSubmit={handleSubmit} className="form">
        <label>
          Username
          <input
            type="text"
            value={username}
            onChange={e => setUsername(e.target.value)}
            required
            minLength={3}
          />
        </label>
        <label>
          Password
          <input
            type="password"
            value={password}
            onChange={e => setPassword(e.target.value)}
            required
            minLength={6}
          />
        </label>
        {error && <div className="form-error">{error}</div>}
        <button className="btn primary" type="submit" disabled={loading}>
          {loading ? 'Logging in...' : 'Login'}
        </button>
      </form>
      <p className="form-footer">
        Don't have an account? <Link to="/signup">Sign up</Link>
      </p>
    </div>
  )
}

export default LoginPage


