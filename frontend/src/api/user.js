import request from '@/utils/request'

export async function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data,
  })
}

export function getUserInfo() {
  return request({
    url: '/user/userInfo',
    method: 'get',
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post',
  })
}

export function register() {
  return request({
    url: '/user/register',
    method: 'post',
  })
}
