import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { fetchMyGroups, fetchAllGroups, createGroup } from '../api/groups'

function GroupsPage() {
  const [myGroups, setMyGroups] = useState([])
  const [allGroups, setAllGroups] = useState([])
  const [name, setName] = useState('')
  const [description, setDescription] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)
  const [creating, setCreating] = useState(false)

  useEffect(() => {
    let cancelled = false
    async function load() {
      setLoading(true)
      try {
        const [mine, all] = await Promise.all([fetchMyGroups(), fetchAllGroups()])
        if (!cancelled) {
          setMyGroups(mine || [])
          setAllGroups(all || [])
        }
      } catch (err) {
        if (!cancelled) setError(err.message || 'Failed to load groups')
      } finally {
        if (!cancelled) setLoading(false)
      }
    }
    load()
    return () => {
      cancelled = true
    }
  }, [])

  const handleCreate = async e => {
    e.preventDefault()
    setError('')
    setCreating(true)
    try {
      const group = await createGroup({ name, description })
      setMyGroups(prev => [...prev, group])
      setAllGroups(prev => [...prev, group])
      setName('')
      setDescription('')
    } catch (err) {
      setError(err.message || 'Failed to create group')
    } finally {
      setCreating(false)
    }
  }

  return (
    <div className="page-groups">
      <div className="card">
        <h1>Groups</h1>
        <p>Create a group and invite users by username to compare collection values.</p>
        <form className="form inline" onSubmit={handleCreate}>
          <input
            type="text"
            placeholder="Group name"
            value={name}
            onChange={e => setName(e.target.value)}
            required
          />
          <input
            type="text"
            placeholder="Description (optional)"
            value={description}
            onChange={e => setDescription(e.target.value)}
          />
          <button className="btn primary" type="submit" disabled={creating}>
            {creating ? 'Creating...' : 'Create Group'}
          </button>
        </form>
        {error && <div className="form-error">{error}</div>}
      </div>

      <div className="grid">
        <div className="card">
          <h2>My Groups</h2>
          {loading && <p>Loading...</p>}
          {!loading && myGroups.length === 0 && <p>You are not in any groups yet.</p>}
          <ul className="list">
            {myGroups.map(g => (
              <li key={g.groupId}>
                <Link to={`/groups/${g.groupId}`}>{g.name}</Link>
              </li>
            ))}
          </ul>
        </div>
        <div className="card">
          <h2>All Groups</h2>
          {loading && <p>Loading...</p>}
          {!loading && allGroups.length === 0 && <p>No groups found.</p>}
          <ul className="list">
            {allGroups.map(g => (
              <li key={g.groupId}>
                <Link to={`/groups/${g.groupId}`}>{g.name}</Link>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  )
}

export default GroupsPage


