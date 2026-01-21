import React, { useState, useEffect, useMemo } from 'react'
import { useNavigate } from 'react-router-dom'
import { addCard } from '../api/collection'
import { API_BASE } from '../api/client'

function AddCardPage() {
  const [allCards, setAllCards] = useState([])
  const [loadingCards, setLoadingCards] = useState(true)
  const [loadError, setLoadError] = useState('')
  const [filterQuery, setFilterQuery] = useState('')
  const [currentPage, setCurrentPage] = useState(1)
  const [hasMore, setHasMore] = useState(true)
  
  const [cardId, setCardId] = useState('')
  const [cardName, setCardName] = useState('')
  const [quantity, setQuantity] = useState(1)
  const [cardImage, setCardImage] = useState('')
  const [cardSet, setCardSet] = useState('')
  const [cardRarity, setCardRarity] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  // Load cards on mount and when page changes
  useEffect(() => {
    let cancelled = false
    
    async function loadCards() {
      if (!hasMore && currentPage > 1) return
      
      setLoadingCards(true)
      setLoadError('')
      try {
        // Use backend endpoint to avoid CORS issues
        // Backend will proxy the request to Pokemon TCG API
        // Empty query means get all cards
        const params = new URLSearchParams({
          q: '', // Empty query to get all cards
          page: currentPage.toString(),
          pageSize: '250', // API max
        })
        
        const response = await fetch(`${API_BASE}/pokemon-tcg/search?${params.toString()}`)
        
        if (!response.ok) {
          throw new Error(`Failed to load cards: ${response.status}`)
        }
        
        const cards = await response.json()
        
        if (!cancelled) {
          if (currentPage === 1) {
            setAllCards(cards || [])
            // Estimate if there are more (if we got 250, likely more exist)
            setHasMore((cards || []).length >= 250)
          } else {
            setAllCards(prev => [...prev, ...(cards || [])])
            // If we got fewer than 250, we've reached the end
            setHasMore((cards || []).length >= 250)
          }
        }
      } catch (err) {
        if (!cancelled) {
          setLoadError(err.message || 'Failed to load cards. Please try again.')
        }
      } finally {
        if (!cancelled) {
          setLoadingCards(false)
        }
      }
    }
    
    loadCards()
    return () => { cancelled = true }
  }, [currentPage])

  // Filter cards by name (client-side filtering)
  const filteredCards = useMemo(() => {
    if (!filterQuery.trim()) {
      return allCards
    }
    
    const query = filterQuery.toLowerCase().trim()
    return allCards.filter(card => 
      card.name?.toLowerCase().includes(query)
    )
  }, [allCards, filterQuery])

  const handleSelectCard = (card) => {
    setCardId(card.id || '')
    setCardName(card.name || '')
    setCardImage(card.images?.large || card.images?.small || '')
    setCardSet(card.set?.name || '')
    setCardRarity(card.rarity || '')
    // Scroll to form
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  const handleLoadMore = () => {
    if (!loadingCards && hasMore) {
      setCurrentPage(prev => prev + 1)
    }
  }

  const handleSubmit = async e => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await addCard({
        cardId,
        cardName,
        quantity: Number(quantity),
        cardImage: cardImage || undefined,
        cardSet: cardSet || undefined,
        cardRarity: cardRarity || undefined,
      })
      navigate('/collection', { replace: true })
    } catch (err) {
      setError(err.message || 'Failed to add card')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div>
      {/* Selected Card Form */}
      {cardId && (
        <div className="card form-card" style={{ marginBottom: '24px', position: 'sticky', top: '80px', zIndex: 10 }}>
          <h2>Selected Card</h2>
          <div style={{ 
            marginBottom: '16px', 
            padding: '12px', 
            background: 'rgba(34, 197, 94, 0.1)', 
            border: '1px solid rgba(34, 197, 94, 0.3)',
            borderRadius: '8px',
            fontSize: '0.9rem',
            display: 'flex',
            alignItems: 'center',
            gap: '12px'
          }}>
            {cardImage && (
              <img 
                src={cardImage} 
                alt={cardName}
                style={{ width: '60px', height: '84px', objectFit: 'contain', borderRadius: '4px' }}
              />
            )}
            <div style={{ flex: 1 }}>
              <div><strong>{cardName}</strong></div>
              <div style={{ fontSize: '0.85rem', opacity: 0.8 }}>ID: {cardId}</div>
              {cardSet && <div style={{ fontSize: '0.85rem', opacity: 0.8 }}>Set: {cardSet}</div>}
            </div>
            <button
              type="button"
              onClick={() => {
                setCardId('')
                setCardName('')
                setCardImage('')
                setCardSet('')
                setCardRarity('')
              }}
              style={{
                padding: '6px 12px',
                background: 'rgba(239, 68, 68, 0.2)',
                border: '1px solid rgba(239, 68, 68, 0.4)',
                borderRadius: '4px',
                color: '#f5f5f5',
                cursor: 'pointer',
                fontSize: '0.85rem'
              }}
            >
              Clear
            </button>
          </div>
          
          <form className="form" onSubmit={handleSubmit}>
            <label>
              Quantity
              <input
                type="number"
                min="1"
                value={quantity}
                onChange={e => setQuantity(e.target.value)}
                required
              />
            </label>
            {error && <div className="form-error">{error}</div>}
            <button className="btn primary" type="submit" disabled={loading}>
              {loading ? 'Adding...' : 'Add Card to Collection'}
            </button>
          </form>
        </div>
      )}

      {/* Card Library */}
      <div className="card">
        <h1>Browse Pokemon Cards</h1>
        <p>
          Scroll through the card library below and click on a card to select it. 
          Use the search box to filter by Pokemon name.
        </p>
        
        {/* Search/Filter */}
        <div style={{ marginBottom: '24px' }}>
          <label>
            Filter by Pokemon Name
            <input
              type="text"
              value={filterQuery}
              onChange={e => setFilterQuery(e.target.value)}
              placeholder="Type to filter (e.g., Pikachu, Charizard)"
              style={{ width: '100%', marginTop: '8px' }}
            />
          </label>
        </div>

        {loadError && (
          <div className="form-error" style={{ marginBottom: '16px' }}>
            {loadError}
          </div>
        )}

        {loadingCards && allCards.length === 0 && (
          <div style={{ textAlign: 'center', padding: '40px', color: '#94a3b8' }}>
            Loading cards...
          </div>
        )}

        {/* Card Grid */}
        {filteredCards.length > 0 && (
          <>
            <div style={{ 
              display: 'grid', 
              gridTemplateColumns: 'repeat(auto-fill, minmax(160px, 1fr))', 
              gap: '16px',
              marginBottom: '24px'
            }}>
              {filteredCards.map((card) => (
                <div
                  key={card.id}
                  onClick={() => handleSelectCard(card)}
                  style={{
                    padding: '12px',
                    cursor: 'pointer',
                    background: card.id === cardId 
                      ? 'rgba(34, 197, 94, 0.2)' 
                      : 'rgba(30, 41, 59, 0.8)',
                    border: card.id === cardId
                      ? '2px solid rgba(34, 197, 94, 0.6)'
                      : '2px solid rgba(148, 163, 184, 0.2)',
                    borderRadius: '8px',
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    gap: '8px',
                    transition: 'all 0.2s',
                  }}
                  onMouseEnter={(e) => {
                    if (card.id !== cardId) {
                      e.currentTarget.style.background = 'rgba(148, 163, 184, 0.15)'
                      e.currentTarget.style.borderColor = 'rgba(34, 197, 94, 0.5)'
                      e.currentTarget.style.transform = 'translateY(-2px)'
                    }
                  }}
                  onMouseLeave={(e) => {
                    if (card.id !== cardId) {
                      e.currentTarget.style.background = 'rgba(30, 41, 59, 0.8)'
                      e.currentTarget.style.borderColor = 'rgba(148, 163, 184, 0.2)'
                      e.currentTarget.style.transform = 'translateY(0)'
                    }
                  }}
                >
                  {card.images?.small && (
                    <img
                      src={card.images.small}
                      alt={card.name}
                      style={{ 
                        width: '100%', 
                        maxWidth: '140px', 
                        height: 'auto',
                        objectFit: 'contain',
                        borderRadius: '4px'
                      }}
                      onError={(e) => {
                        e.target.style.display = 'none'
                      }}
                    />
                  )}
                  <div style={{ textAlign: 'center', width: '100%' }}>
                    <div style={{ fontWeight: 'bold', fontSize: '0.9rem', marginBottom: '4px' }}>
                      {card.name}
                    </div>
                    {card.set?.name && (
                      <div style={{ fontSize: '0.75rem', opacity: 0.8, marginBottom: '2px' }}>
                        {card.set.name}
                      </div>
                    )}
                    {card.rarity && (
                      <div style={{ fontSize: '0.7rem', opacity: 0.6 }}>
                        {card.rarity}
                      </div>
                    )}
                  </div>
                </div>
              ))}
            </div>

            {/* Load More Button */}
            {hasMore && !filterQuery && (
              <div style={{ textAlign: 'center', marginTop: '24px' }}>
                <button
                  onClick={handleLoadMore}
                  disabled={loadingCards}
                  className="btn secondary"
                  style={{ minWidth: '200px' }}
                >
                  {loadingCards ? 'Loading...' : `Load More Cards (${allCards.length} loaded)`}
                </button>
              </div>
            )}

            {filterQuery && filteredCards.length === 0 && (
              <div style={{ textAlign: 'center', padding: '40px', color: '#94a3b8' }}>
                No cards found matching "{filterQuery}"
              </div>
            )}
          </>
        )}

        {!loadingCards && allCards.length === 0 && !loadError && (
          <div style={{ textAlign: 'center', padding: '40px', color: '#94a3b8' }}>
            No cards available
          </div>
        )}
      </div>
    </div>
  )
}

export default AddCardPage
