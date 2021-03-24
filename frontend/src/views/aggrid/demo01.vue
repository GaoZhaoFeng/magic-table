<template>
  <div>
    <a-button type="primary" @click="getSelectedRows()">获取选中的行</a-button>
    <ag-grid-vue
      style="width: 1000px; height: 600px"
      class="ag-theme-alpine"
      :columnDefs="columnDefs"
      :rowData="rowData"
      rowSelection="multiple"
      @grid-ready="onGridReady"
    />
  </div>
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
          { headerName: 'ID', field: 'id', sortable: true, filter: true },
          { headerName: '车牌号', field: 'plate', sortable: true, filter: true, rowGroup: true },
          { headerName: '司机名', field: 'driver', sortable: true, filter: true, checkboxSelection: true },
          { headerName: '手机号', field: 'phone', sortable: true, filter: true, checkboxSelection: true },
          { headerName: '油耗', field: 'fuel', sortable: true, filter: true },
        ],
        rowData: null,
        gridApi: null,
        columnApi: null,
        autoGroupColumnDef: {
          headerName: '司机名',
          field: 'driver',
          cellRenderer: 'agGroupCellRenderer',
          cellRendererParams: {
            checkbox: true,
          },
        },
      }
    },
    mounted() {
      this.fetch()
    },
    methods: {
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
      getSelectedRows() {
        const selectedNodes = this.gridApi.getSelectedNodes()
        const selectedData = selectedNodes.map((node) => node.data)
        const selectedDataStringPresentation = selectedData.map((node) => `${node.plate} ${node.driver}`).join(', ')
        alert(`Selected nodes：${selectedDataStringPresentation}`)
      },
      onGridReady(params) {
        this.gridApi = params.api
        this.columnApi = params.columnApi
      },
    },
  }
</script>
<style scoped></style>
