<template>
  <ag-grid-vue
    style="width: 1000px; height: 600px"
    class="ag-theme-alpine"
    :columnDefs="columnDefs"
    :rowData="rowData"
  />
</template>

<script>
  import 'ag-grid-community/dist/styles/ag-grid.css'
  import 'ag-grid-community/dist/styles/ag-theme-alpine.css'
  import { getPageData } from '@/api/table'
  import { AgGridVue } from 'ag-grid-vue3'

  export default {
    name: 'App',
    components: {
      AgGridVue,
    },
    data() {
      return {
        columnDefs: [
          { field: 'id', sortable: true, filter: true },
          { field: 'plate', sortable: true, filter: true },
          { field: 'driver', sortable: true, filter: true },
          { field: 'phone', sortable: true, filter: true },
          { field: 'fuel', sortable: true, filter: true },
        ],
        rowData: null,
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
          pageSize: 10,
          pageNum: 0,
        }
        getPageData(params, {}).then((res) => {
          this.rowData = res.data.pageData
        })
      },
    },
  }
</script>
<style scoped></style>
