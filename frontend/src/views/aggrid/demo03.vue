<template>
  <div style="margin-top: -10px">
    <div title="工具栏" style="margin-bottom: 8px">
      <a-button-group title="表、字段、筛选条件的配置">
        <a-button type="primary" title="表格配置" ghost>
          <vab-icon icon="settings-2-line" />
        </a-button>
        <a-button type="primary" title="字段配置" ghost>
          <vab-icon icon="list-settings-line" />
        </a-button>
        <a-button type="primary" title="自定义筛选条件" ghost>
          <vab-icon icon="filter-line" />
        </a-button>
        <a-button type="primary" @click="getSelectedRows()" title="导出勾选数据" ghost>
          <vab-icon icon="check-double-fill" />
        </a-button>
        <a-button type="primary" title="导出数据" ghost>
          <vab-icon icon="share-forward-2-fill" />
        </a-button>
        <a-button type="primary" title="导入更新" ghost>
          <vab-icon icon="file-download-line" />
        </a-button>
        <a-button type="primary" title="刷新数据" @click="refresh()" ghost>
          <vab-icon icon="refresh-line" />
        </a-button>
      </a-button-group>
      <a-button-group title="危险操作">
        <a-button type="danger" title="清空表格" ghost>
          <vab-icon icon="delete-bin-line" />
        </a-button>
        <a-button type="danger" title="删除选中数据" ghost>
          <vab-icon icon="delete-back-2-line" />
        </a-button>
        <a-button type="danger" title="删除筛选数据" ghost>
          <vab-icon icon="filter-off-line" />
        </a-button>
      </a-button-group>
    </div>
    <ag-grid-vue
      style="width: 100%; height: 75vh"
      class="ag-theme-alpine"
      :columnDefs="columnDefs"
      :rowData="rowData"
      :pagination="pagination"
      :rowModelType="rowModelType"
      :serverSideStoreType="serverSideStoreType"
      :paginationPageSize="paginationPageSize"
      :cacheBlockSize="cacheBlockSize"
      rowSelection="multiple"
      :autoGroupColumnDef="autoGroupColumnDef"
      @gridReady="onGridReady"
      @gridSizeChanged="fitTable"
      @columnVisible="fitTable"
      id="table-data-detail"
    />
  </div>
</template>

<script>
  import 'ag-grid-community/dist/styles/ag-grid.css'
  import 'ag-grid-community/dist/styles/ag-theme-alpine.css'
  import { getPageData } from '@/api/table'
  import { AgGridVue } from 'ag-grid-vue3'
  import VabIcon from '@/layout/vab-icon'

  export default {
    name: 'demo03',
    components: {
      AgGridVue,
      VabIcon,
    },
    data() {
      return {
        columnDefs: [
          {
            headerName: 'ID',
            field: 'id',
            sortable: true,
            filter: true,
            width: 250,
          },
          {
            headerName: '车牌号',
            field: 'plate',
            sortable: true,
            filter: true,
            // rowGroup: true可以实现列分组
          },
          {
            headerName: '司机名',
            field: 'driver',
            sortable: true,
            filter: true,
            checkboxSelection: true,
          },
          {
            headerName: '手机号',
            field: 'phone',
            sortable: true,
            filter: true,
            checkboxSelection: true,
          },
          {
            headerName: '油耗',
            field: 'fuel',
            sortable: true,
            filter: true,
          },
        ],
        rowData: null,
        gridApi: null, // 表格的API，https://www.ag-grid.com/vue-grid/grid-api/
        columnApi: null, // 列的API，https://www.ag-grid.com/vue-grid/column-api/
        autoGroupColumnDef: {
          headerName: '司机名',
          field: 'driver',
          cellRenderer: 'agGroupCellRenderer',
          cellRendererParams: {
            checkbox: true,
          },
        },
        pagination: true,
        rowModelType: 'serverSide',
        serverSideStoreType: 'partial',
        paginationPageSize: 15,
        cacheBlockSize: 15,
      }
    },
    methods: {
      fetch() {
        this.loading = true
        // 分页条件
        let params = {
          table: 'device',
          pageSize: this.paginationPageSize,
          pageNum: this.gridApi.paginationGetCurrentPage() + 1, // 下标从0开始的
        }
        getPageData(params, {}).then((res) => {
          // Todo：这里的分页还有非常多的问题，最好直接采用ag-grid的服务器端分页
          this.gridApi.setServerSideDatasource(res.data.pageData)
          this.fitTable()
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
        this.fetch()
      },
      refresh() {
        this.fetch()
        this.fitTable()
      },
      // 当页面伸缩的时候自动调整表格宽度
      fitTable() {
        this.gridApi.sizeColumnsToFit()
      },
    },
  }
</script>
<style scoped></style>
