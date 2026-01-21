import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { addMember, fetchGroup } from '../api/groups'

function GroupDetailPage() {
  const { groupId } = useParams()
  const [group, setGroup] = useState(null)
  const [memberUsername, setMemberUsername] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)
  const [adding, setAdding] = useState(false)

  useEffect(() => {
    let cancelled = false
    async function load() {
      setLoading(true)
      setError('')
      try {
        const g = await fetchGroup(groupId)
        if (!cancelled) setGroup(g)
      } catch (err) {
        if (!cancelled) setError(err.message || 'Failed to load group')
      } finally {
        if (!cancelled) setLoading(false)
      }
    }
    load()
    return () => {
      cancelled = true
    }
  }, [groupId])

  const handleAddMember = async e => {
    e.preventDefault()
    if (!memberUsername.trim()) return
    setAdding(true)
    setError('')
    try {
      await addMember(groupId, memberUsername.trim())
      setMemberUsername('')
      const updated = await fetchGroup(groupId)
      setGroup(updated)
    } catch (err) {
      setError(err.message || 'Failed to add member')
    } finally {
      setAdding(false)
    }
  }

  if (loading) {
    return (
      <div className="card">
        <h1>Loading group...</h1>
      </div>
    )
  }

  if (!group) {
    return (
      <div className="card">
        <h1>Group not found</h1>
      </div>
    )
  }

  const leaderboard = group.leaderboard || []

  return (
    <div className="card">
      <h1>{group.name}</h1>
      {group.description && <p>{group.description}</p>}

      <section className="section">
        <h2>Leaderboard</h2>
        {leaderboard.length === 0 ? (
          <p>No rankings yet. Add members and cards to see rankings.</p>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>Rank</th>
                <th>User</th>
                <th>Total Value</th>
                <th>Card Count</th>
              </tr>
            </thead>
            <tbody>
              {leaderboard.map(m => (
                <tr key={m.username}>
                  <td>{m.rank}</td>
                  <td>{m.username}</td>
                  <td>${m.totalValue.toFixed(2)}</td>
                  <td>{m.cardCount}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>

      <section className="section">
        <h2>Members</h2>
        <ul className="list">
          {(group.members || []).map(m => (
            <li key={m.userId}>{m.username}</li>
          ))}
        </ul>
        <form className="form inline" onSubmit={handleAddMember}>
          <input
            type="text"
            placeholder="Username to add"
            value={memberUsername}
            onChange={e => setMemberUsername(e.target.value)}
          />
          <button className="btn secondary" type="submit" disabled={adding}>
            {adding ? 'Adding...' : 'Add Member'}
          </button>
        </form>
        {error && <div className="form-error">{error}</div>}
      </section>
    </div>
  )
}

export default GroupDetailPage


