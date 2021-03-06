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
        <a-button
          type="primary"
          @click="getSelectedRows()"
          title="导出勾选数据"
          ghost
        >
          <vab-icon icon="check-double-fill" />
        </a-button>
        <a-button type="primary" title="导出数据" ghost>
          <vab-icon icon="share-forward-2-fill" />
        </a-button>
        <a-button type="primary" title="导入更新" ghost>
          <vab-icon icon="file-download-line" />
        </a-button>
        <a-button type="primary" title="刷新数据" @click="fetch()" ghost>
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
    computed: {
      filterName: function () {
        return 'table_filter_' + this.tableName
      },
      sorterName: function () {
        return 'table_sorter_' + this.tableName
      },
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
        let sorter = {}
        for (let i = 0; i < sortStates.length; i++) {
          sorter[sortStates[i].colId] = sortStates[i].sort === 'asc' ? 1 : -1
        }
        // 将sorter存到localStorage
        localStorage.setItem(this.sorterName, JSON.stringify(sorter))
        return sorter
      },
      getFilter() {
        const model = this.gridApi.getFilterModel()
        console.log(model)
        // 下面解析model生成filter
        let filter = {}
        for (let i = 0; i < this.columnDefs; i++) {
          let field = this.columnDefs[i].field
          console.log(model[field])
        }
        // 将filter存到localStorage
        localStorage.setItem(this.filterName, filter)
      },
      fetchInit() {
        this.loading = true
        // 如果之前存了filter和sorter，要先加载过来
        let filter = localStorage.getItem(this.filterName)
        console.log(filter)
        let sorter = localStorage.getItem(this.sorterName)
        console.log(sorter)
        this.gridApi.setFilterModel(filter)
        this.columnApi.applyColumnState({
          state: sorter,
          defaultState: { sort: null },
        })
        // Todo：后面filter和sorter可能都会从后端数据库获取，直接应用到表格上。
        //  但是不在ag-grid的filter和sorter上体现。直接数据取好就行了。
        // 前端有sorter和filter就用前端的，否则就用后端的 or 后端预先加载到前端也可以
        this.getData(this.tableName, filter, sorter)
      },
      fetch() {
        this.getData(this.tableName, {}, this.getSorter())
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
      // 表格加载号会自动调用，可以当mounted来用
      onGridReady(params) {
        this.gridApi = params.api
        this.columnApi = params.columnApi
        this.fetchInit()
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
        this.fetch()
      },
      onChange(pageNumber) {
        this.current = pageNumber
        this.fetch()
      },
      onSorterChanged() {
        this.fetch()
      },
      onFilterChanged() {
        this.getData(this.tableName, this.getFilter(), this.getSorter())
      },
    },
  }
</script>
<style scoped></style>
