import request from './client'

export function fetchMyGroups() {
  return request('/groups/my-groups')
}

export function fetchAllGroups() {
  return request('/groups')
}

export function fetchGroup(groupId) {
  return request(`/groups/${groupId}`)
}

export function createGroup({ name, description }) {
  return request('/groups', {
    method: 'POST',
    body: { name, description },
  })
}

export function addMember(groupId, memberUsername) {
  const params = new URLSearchParams({ memberUsername })
  return request(`/groups/${groupId}/members?${params.toString()}`, {
    method: 'POST',
  })
}


