import React from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../hooks/useAuth'

function HomePage() {
  const { username } = useAuth()

  return (
    <div className="card">
      <h1>Welcome, {username}</h1>
      <p>Use Pokemon Rankings to manage your card collection and compete with friends.</p>
      <div className="grid">
        <div className="card">
          <h2>My Groups</h2>
          <p>Create groups and invite other users. See who has the most valuable collection.</p>
          <Link className="btn secondary" to="/groups">
            View Groups
          </Link>
        </div>
        <div className="card">
          <h2>My Collection</h2>
          <p>Track cards in your collection and keep quantities up to date.</p>
          <Link className="btn secondary" to="/collection">
            View Collection
          </Link>
        </div>
      </div>
    </div>
  )
}

export default HomePage


