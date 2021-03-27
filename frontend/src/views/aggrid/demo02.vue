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
        <a-button type="danger" title="删除筛选条件" ghost>
          <vab-icon icon="filter-off-line" />
        </a-button>
      </a-button-group>
    </div>
    <ag-grid-vue
      style="width: 100%; height: 72vh"
      class="ag-theme-alpine"
      :defaultColDef="defaultColDef"
      :columnDefs="columnDefs"
      :rowData="rowData"
      :animateRows="true"
      :accentedSort="true"
      rowSelection="multiple"
      @gridReady="onGridReady"
      @gridSizeChanged="fitTable"
      @sortChanged="onSorterChanged"
      @filterChanged="onFilterChanged"
      @columnVisible="fitTable"
      id="table-data-detail"
    />
    <a-pagination
      :current="current"
      :page-size-options="pageSizeOptions"
      :total="total"
      show-size-changer
      show-quick-jumper
      :page-size="pageSize"
      @showSizeChange="onShowSizeChange"
      @change="onChange"
      style="float: right; width: 100%"
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
    name: 'demo02',
    components: {
      AgGridVue,
      VabIcon,
    },
    data() {
      return {
        tableName: 'device',
        columnDefs: [
          {
            headerName: 'ID',
            field: 'id',
            width: 250,
          },
          {
            headerName: '车牌号',
            field: 'plate',
          },
          {
            headerName: '司机名',
            field: 'driver',
          },
          {
            headerName: '手机号',
            field: 'phone',
          },
          {
            headerName: '油耗',
            field: 'fuel',
            filter: 'agNumberColumnFilter',
          },
          {
            headerName: '出厂日期',
            field: 'date',
            filter: 'agDateColumnFilter',
          },
        ],
        defaultColDef: {
          sortable: true,
          filter: true,
          filterParams: {
            buttons: ['reset', 'apply'],
          },
        },
        rowData: null,
        gridApi: null, // 表格的API，https://www.ag-grid.com/vue-grid/grid-api/
        columnApi: null, // 列的API，https://www.ag-grid.com/vue-grid/column-api/
        pageSizeOptions: ['10', '15', '20', '30', '40', '50'],
        current: 1,
        pageSize: 15,
        total: 0,
      }
    },
    mounted() {
      this.loading = true
      // 分页条件
      this.getData(this.tableName, {}, {})
    },
    methods: {
      getSorter() {
        let colState = this.columnApi.getColumnState()
        let sortStates = colState
          .filter(function (s) {
            return s.sort != null
          })
          .map(function (s) {
            return {
              colId: s.colId,
              sort: s.sort,
              sortIndex: s.sortIndex,
            }
          })
        console.log('saved sort', sortStates)
        let sorter = {}
        for (let i = 0; i < sortStates.length; i++) {
          sorter[sortStates[i].colId] = sortStates[i].sort === 'asc' ? 1 : -1
        }
        return sorter
      },
      getData(table, filter, sorter) {
        // 分页条件
        let params = {
          table: table,
          pageSize: this.pageSize,
          pageNum: this.current,
        }
        getPageData(params, { filter: filter, sorter: sorter }).then((res) => {
          this.rowData = res.data.pageData
          this.total = res.data.totalCnt
          this.fitTable()
        })
      },
      getSelectedRows() {
        const selectedNodes = this.gridApi.getSelectedNodes()
        const selectedData = selectedNodes.map((node) => node.data)
        const selectedDataStringPresentation = selectedData
          .map((node) => `${node.plate} ${node.driver}`)
          .join(', ')
        alert(`Selected nodes：${selectedDataStringPresentation}`)
      },
      onGridReady(params) {
        this.gridApi = params.api
        this.columnApi = params.columnApi
        this.fitTable()
      },
      refresh() {
        this.getData(this.tableName, {}, this.getSorter())
        this.fitTable()
      },
      // 当页面伸缩的时候自动调整表格宽度
      fitTable() {
        this.gridApi.sizeColumnsToFit()
      },
      onShowSizeChange(current, pageSize) {
        this.pageSize = pageSize
        // 默认回到第一页
        this.current = 1
        // 重新刷新数据
        this.getData(this.tableName, {}, this.getSorter())
      },
      onChange(pageNumber) {
        this.current = pageNumber
        this.getData(this.tableName, {}, this.getSorter())
      },
      onSorterChanged() {
        this.getData(this.tableName, {}, this.getSorter())
      },
      onFilterChanged() {
        const model = this.gridApi.getFilterModel()
        console.log(model)
      },
    },
  }
</script>
<style scoped></style>
