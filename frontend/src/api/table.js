import request from '@/utils/request'

export function getPageData(params, data) {
  return request({
    url: '/table/page_data?',
    method: 'post',
    params,
    data,
  })
}
