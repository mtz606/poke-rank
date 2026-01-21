import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { fetchMyCollection, removeCard, updateCardQuantity } from '../api/collection'

function MyCollectionPage() {
  const [cards, setCards] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    let cancelled = false
    async function load() {
      setLoading(true)
      setError('')
      try {
        const data = await fetchMyCollection()
        if (!cancelled) setCards(data || [])
      } catch (err) {
        if (!cancelled) setError(err.message || 'Failed to load collection')
      } finally {
        if (!cancelled) setLoading(false)
      }
    }
    load()
    return () => {
      cancelled = true
    }
  }, [])

  const handleRemove = async cardId => {
    if (!window.confirm('Remove this card from your collection?')) return
    try {
      await removeCard(cardId)
      setCards(prev => prev.filter(c => c.cardId !== cardId))
    } catch (err) {
      alert(err.message || 'Failed to remove card')
    }
  }

  const handleQuantityChange = async (cardId, currentQuantity) => {
    const next = window.prompt('New quantity:', String(currentQuantity))
    if (!next) return
    const quantity = Number(next)
    if (!Number.isFinite(quantity) || quantity <= 0) {
      alert('Quantity must be a positive number')
      return
    }
    try {
      const updated = await updateCardQuantity(cardId, quantity)
      setCards(prev =>
        prev.map(c => (c.cardId === cardId ? { ...c, quantity: updated.quantity } : c)),
      )
    } catch (err) {
      alert(err.message || 'Failed to update quantity')
    }
  }

  return (
    <div className="card">
      <h1>My Collection</h1>
      <p>Cards you have added to your collection.</p>
      <div className="button-row">
        <Link className="btn primary" to="/collection/add">
          Add Card
        </Link>
      </div>
      {loading && <p>Loading...</p>}
      {error && <div className="form-error">{error}</div>}
      {!loading && cards.length === 0 && <p>You haven't added any cards yet.</p>}
      {cards.length > 0 && (
        <table className="table">
          <thead>
            <tr>
              <th>Card</th>
              <th>Set</th>
              <th>Rarity</th>
              <th>Quantity</th>
              <th />
            </tr>
          </thead>
          <tbody>
            {cards.map(card => (
              <tr key={card.cardId}>
                <td>
                  <div className="card-info">
                    {card.cardImage && (
                      <img
                        src={card.cardImage}
                        alt={card.cardName}
                        className="card-thumb"
                      />
                    )}
                    <div>
                      <div>{card.cardName}</div>
                      <div className="muted">{card.cardId}</div>
                    </div>
                  </div>
                </td>
                <td>{card.cardSet || '-'}</td>
                <td>{card.cardRarity || '-'}</td>
                <td>
                  <button
                    className="link-button"
                    onClick={() => handleQuantityChange(card.cardId, card.quantity)}
                  >
                    {card.quantity}
                  </button>
                </td>
                <td>
                  <button
                    className="btn danger small"
                    onClick={() => handleRemove(card.cardId)}
                  >
                    Remove
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}

export default MyCollectionPage


