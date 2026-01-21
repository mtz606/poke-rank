// Direct Pokemon TCG API v2 client (frontend only)
const API_BASE_URL = 'https://api.pokemontcg.io/v2'

export async function getAllCards(page = 1, pageSize = 250) {
  // Load cards without query - get all cards
  const params = new URLSearchParams({
    page: page.toString(),
    pageSize: Math.min(pageSize, 250).toString(), // API max is 250
  })

  const url = `${API_BASE_URL}/cards?${params.toString()}`
  
  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
      },
    })

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}))
      throw new Error(errorData.error?.message || `API returned ${response.status}`)
    }

    const data = await response.json()
    return {
      cards: data.data || [],
      totalCount: data.totalCount || 0,
      page: data.page || page,
      pageSize: data.pageSize || pageSize,
      count: data.count || 0,
    }
  } catch (error) {
    console.error('Pokemon TCG API error:', error)
    throw error
  }
}

export async function searchCardsDirect(query, page = 1, pageSize = 10) {
  // Format query - if it doesn't contain a colon, assume it's a name search
  let formattedQuery = query.trim()
  if (!formattedQuery.includes(':')) {
    // If query contains spaces, wrap in quotes for phrase search
    if (formattedQuery.includes(' ')) {
      formattedQuery = `name:"${formattedQuery}"`
    } else {
      formattedQuery = `name:${formattedQuery}`
    }
  }

  const params = new URLSearchParams({
    q: formattedQuery,
    page: page.toString(),
    pageSize: Math.min(pageSize, 250).toString(), // API max is 250
  })

  const url = `${API_BASE_URL}/cards?${params.toString()}`
  
  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
      },
    })

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}))
      throw new Error(errorData.error?.message || `API returned ${response.status}`)
    }

    const data = await response.json()
    return data.data || []
  } catch (error) {
    console.error('Pokemon TCG API error:', error)
    throw error
  }
}

export async function getCardByIdDirect(cardId) {
  const url = `${API_BASE_URL}/cards/${cardId}`
  
  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
      },
    })

    if (!response.ok) {
      throw new Error(`Card not found: ${response.status}`)
    }

    const data = await response.json()
    return data.data || null
  } catch (error) {
    console.error('Pokemon TCG API error:', error)
    throw error
  }
}

