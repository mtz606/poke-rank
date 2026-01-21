import request from './client'

export function fetchMyCollection() {
  return request('/card-collection/my-collection')
}

export function addCard(payload) {
  return request('/card-collection/add', {
    method: 'POST',
    body: payload,
  })
}

export function removeCard(cardId) {
  return request(`/card-collection/remove/${cardId}`, {
    method: 'DELETE',
  })
}

export function updateCardQuantity(cardId, quantity) {
  const params = new URLSearchParams({ quantity })
  return request(`/card-collection/update-quantity/${cardId}?${params.toString()}`, {
    method: 'PUT',
  })
}


