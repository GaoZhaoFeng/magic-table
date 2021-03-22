<template>
  <a-table
    :columns="columns"
    :row-key="(record) => record.id"
    :data-source="data"
    :pagination="pagination"
    :loading="loading"
    @change="handleTableChange"
  ></a-table>
</template>
<script>
  import { getPageData } from '@/api/table'

  export default {
    data() {
      return {
        data: [],
        pagination: {
          showLessItems: true,
          showQuickJumper: true,
          showSizeChanger: true,
          pageSize: 10,
          current: 1,
        },
        query: {},
        loading: false,
        columns: [
          {
            title: 'id',
            dataIndex: 'id',
          },
          {
            title: 'plate',
            dataIndex: 'plate',
          },
          {
            title: 'driver',
            dataIndex: 'driver',
          },
          {
            title: 'phone',
            dataIndex: 'phone',
          },
          {
            title: 'fuel',
            dataIndex: 'fuel',
          },
        ],
      }
    },
    mounted() {
      this.fetch()
    },
    methods: {
      handleTableChange(pagination) {
        this.pagination.current = pagination.current
        this.fetch()
      },
      fetch() {
        this.loading = true
        // 分页条件
        let params = {
          table: 'device',
          pageSize: this.pagination.pageSize,
          pageNum: this.pagination.current,
        }
        // 查询条件
        let data = {
          age: {
            $gt: 40,
            $lt: 70,
          },
          $or: [
            {
              driver: {
                $regex: '金',
              },
            },
            {
              driver: {
                $regex: '鲍',
              },
            },
          ],
        }
        getPageData(params, data).then((res) => {
          this.pagination.total = res.data.totalCnt
          this.loading = false
          this.data = res.data.pageData
        })
      },
    },
  }
</script>
