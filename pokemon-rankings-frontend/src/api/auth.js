import request from './client'

export async function register({ username, email, password }) {
  return request('/auth/register', {
    method: 'POST',
    body: { username, email, password },
  })
}

export async function login({ username, password }) {
  return request('/auth/login', {
    method: 'POST',
    body: { username, password },
  })
}


