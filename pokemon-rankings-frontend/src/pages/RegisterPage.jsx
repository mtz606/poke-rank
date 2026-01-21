import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { register } from '../api/auth'
import { useAuth } from '../hooks/useAuth'

function RegisterPage() {
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()
  const { login: setAuth } = useAuth()

  const handleSubmit = async e => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const res = await register({ username, email, password })
      setAuth({ token: res.token, username: res.username })
      navigate('/home', { replace: true })
    } catch (err) {
      setError(err.message || 'Registration failed')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="card centered form-card">
      <h1>Sign Up</h1>
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
          Email
          <input
            type="email"
            value={email}
            onChange={e => setEmail(e.target.value)}
            required
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
          {loading ? 'Creating account...' : 'Create Account'}
        </button>
      </form>
      <p className="form-footer">
        Already have an account? <Link to="/login">Login</Link>
      </p>
    </div>
  )
}

export default RegisterPage


