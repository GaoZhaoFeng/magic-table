<template>
  <div style="margin-top: -20px">
    <vxe-toolbar custom print ref="deviceToolbar">
      <template #buttons>
        <vxe-button content="刷新" @click="refresh()" />
        <vxe-button content="对齐">
          <template #dropdowns>
            <vxe-button @click="allAlign = 'left'">居左</vxe-button>
            <vxe-button @click="allAlign = 'center'">居中</vxe-button>
            <vxe-button @click="allAlign = 'right'">居右</vxe-button>
          </template>
        </vxe-button>
        <vxe-button content="导出数据">
          <template #dropdowns>
            <vxe-button>全部数据</vxe-button>
            <vxe-button>勾选数据</vxe-button>
            <vxe-button>本页数据</vxe-button>
          </template>
        </vxe-button>
        <vxe-button content="表格配置">
          <template #dropdowns>
            <vxe-button>表配置</vxe-button>
            <vxe-button>列配置</vxe-button>
            <vxe-button>筛选配置</vxe-button>
          </template>
        </vxe-button>
        <vxe-button content="数据操作">
          <template #dropdowns>
            <vxe-button>删除勾选</vxe-button>
            <vxe-button>复制勾选</vxe-button>
            <vxe-button>清空表格</vxe-button>
          </template>
        </vxe-button>
        <vxe-button content="排序操作">
          <template #dropdowns>
            <vxe-button @click="loadSorter">加载排序</vxe-button>
            <vxe-button>自定义排序</vxe-button>
            <vxe-button @click="clearSorter()">清除排序</vxe-button>
          </template>
        </vxe-button>
      </template>
    </vxe-toolbar>
    <vxe-table
      border
      show-header-overflow
      show-overflow
      highlight-hover-row
      :filter-config="{
        iconNone: 'ri-menu-fill',
        iconMatch: 'ri-menu-add-fill',
        multiple: true,
        remote: true,
      }"
      :sort-config="{ multiple: true, remote: true }"
      :align="allAlign"
      :data="rowData"
      @sort-change="sortChangeEvent"
      @filter-change="filterChangeEvent"
      ref="deviceTable"
    >
      <vxe-table-column
        v-for="(config, index) in columnDefs"
        :key="index"
        v-bind="config"
      />
    </vxe-table>
    <vxe-pager
      :loading="loading"
      :current-page="current"
      :page-size="pageSize"
      :total="total"
      :layouts="[
        'PrevPage',
        'JumpNumber',
        'NextPage',
        'FullJump',
        'Sizes',
        'Total',
      ]"
      @page-change="handlePageChange"
    />
  </div>
</template>

<script>
  import { getPageData } from '@/api/table'
  import VXETable from 'vxe-table'
  import XEUtils from 'xe-utils'

  export default {
    name: 'demo01',
    data() {
      return {
        tableName: 'device',
        loading: false,
        allAlign: null,
        columnDefs: [
          {
            type: 'seq',
            width: 60,
            fixed: null,
          },
          {
            type: 'checkbox',
            width: 50,
            fixed: null,
          },
          {
            title: 'ID',
            field: 'id',
          },
          {
            title: '车牌号',
            field: 'plate',
            sortable: true,
            filters: [{ data: null }],
            filterRender: {
              name: 'FilterInput',
            },
          },
          {
            title: '司机名',
            field: 'driver',
            sortable: true,
            filters: [{ data: { type: 'has', name: '' } }],
            filterRender: {
              name: 'FilterComplex',
            },
          },
          {
            title: '手机号',
            field: 'phone',
            sortable: true,
            // vals表示默认勾选的值，sVal表示默认筛选的值
            filters: [{ data: { vals: ['1', '2', '3'], sVal: '94' } }],
            filterRender: {
              name: 'FilterContent',
            },
          },
          {
            title: '油耗',
            field: 'fuel',
            sortable: true,
            filters: [
              {
                data: {
                  vals: [],
                  sVal: '',
                  fMenu: '',
                  f1Type: '',
                  f1Val: '',
                  fMode: 'and',
                  f2Type: '',
                  f2Val: '',
                },
              },
            ],
            filterRender: {
              name: 'FilterExcel',
            },
          },
        ],
        rowData: null,
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
    mounted() {
      this.addMyFilters()
      let sorter = localStorage.getItem(this.sorterName)
      this.getData(this.tableName, {}, sorter)
      this.$nextTick(() => {
        // 将表格和工具栏进行关联
        const deviceTable = this.$refs.deviceTable
        deviceTable.connect(this.$refs.deviceToolbar)
      })
    },
    methods: {
      addMyFilters() {
        // 创建一个简单的输入框筛选
        VXETable.renderer.add('FilterInput', {
          // 筛选模板
          renderFilter(renderOpts, params) {
            return [<filter-input params={params} />]
          },
          // 重置数据方法
          filterResetMethod(params) {
            const { options } = params
            options.forEach((option) => {
              option.data = ''
            })
          },
          // 重置筛选复原方法（当未点击确认时，该选项将被恢复为默认值）
          filterRecoverMethod({ option }) {
            option.data = ''
          },
          // 筛选方法
          filterMethod(params) {
            const { option, row, column } = params
            const { data } = option
            const cellValue = XEUtils.get(row, column.property)
            if (cellValue) {
              return cellValue.indexOf(data) > -1
            }
            return false
          },
        })
        // 创建一个条件的渲染器
        VXETable.renderer.add('FilterComplex', {
          // 不显示底部按钮，使用自定义的按钮
          showFilterFooter: false,
          // 筛选模板
          renderFilter(renderOpts, params) {
            return [<filter-complex params={params} />]
          },
          // 重置数据方法
          filterResetMethod(params) {
            const { options } = params
            options.forEach((option) => {
              option.data = { type: 'has', name: '' }
            })
          },
          // 筛选数据方法
          filterMethod(params) {
            const { option, row, column } = params
            const cellValue = XEUtils.get(row, column.property)
            const { name } = option.data
            if (cellValue) {
              return cellValue.indexOf(name) > -1
            }
            return false
          },
        })
        // 创建一个支持列内容的筛选
        VXETable.renderer.add('FilterContent', {
          // 不显示底部按钮，使用自定义的按钮
          showFilterFooter: false,
          // 筛选模板
          renderFilter(renderOpts, params) {
            return [<filter-content params={params} />]
          },
          // 重置数据方法
          filterResetMethod(params) {
            const { options } = params
            options.forEach((option) => {
              option.data = { vals: [], sVal: '' }
            })
          },
          // 筛选数据方法
          filterMethod(params) {
            const { option, row, column } = params
            const { vals } = option.data
            const cellValue = XEUtils.get(row, column.property)
            return vals.includes(cellValue)
          },
        })
        // 创建一个实现Excel的筛选器
        VXETable.renderer.add('FilterExcel', {
          // 不显示底部按钮，使用自定义的按钮
          showFilterFooter: false,
          // 筛选模板
          renderFilter(renderOpts, params) {
            return [<filter-excel params={params} />]
          },
          // 重置数据方法
          filterResetMethod(params) {
            const { options } = params
            options.forEach((option) => {
              option.data = {
                vals: [],
                sVal: '',
                fMenu: '',
                f1Type: '',
                f1Val: '',
                fMode: 'and',
                f2Type: '',
                f2Val: '',
              }
            })
          },
          // 筛选数据方法
          filterMethod(params) {
            const { option, row, column } = params
            const cellValue = XEUtils.get(row, column.property)
            const { vals, f1Type, f1Val } = option.data
            if (cellValue) {
              if (f1Type) {
                return cellValue.indexOf(f1Val) > -1
              } else if (vals.length) {
                // 通过指定值筛选
                return vals.includes(cellValue)
              }
            }
            return false
          },
        })
      },
      getData(table, filter, sorter) {
        this.loading = true
        // 分页条件
        let params = {
          table: table,
          pageSize: this.pageSize,
          pageNum: this.current,
        }
        getPageData(params, { filter: filter, sorter: sorter }).then((res) => {
          this.rowData = res.data.pageData
          this.total = res.data.totalCnt
          this.loading = false
          this.loadSorter()
        })
      },
      handlePageChange({ currentPage, pageSize }) {
        this.current = currentPage
        this.pageSize = pageSize
        let sorter = localStorage.getItem(this.sorterName)
        this.getData(this.tableName, {}, sorter)
      },
      sortChangeEvent({ sortList }) {
        console.log(sortList)
        console.log('打印排序顺序')
        let sorter = {}
        sortList.map((item) => {
          sorter[item.property] = item.order === 'asc' ? 1 : -1
        })
        console.log(sorter)
        // 将sorter存到localStorage
        localStorage.setItem(this.sorterName, JSON.stringify(sorter))
        this.getData(this.tableName, {}, sorter)
      },
      loadSorter() {
        let sorter = localStorage.getItem(this.sorterName)
        let sorterObj = JSON.parse(sorter)
        for (let key in sorterObj) {
          console.log({
            field: key,
            order: sorterObj[key] === 1 ? 'asc' : 'desc',
          })
          this.$refs.deviceTable.sort({
            field: key,
            order: sorterObj[key] === 1 ? 'asc' : 'desc',
          })
        }
      },
      clearSorter() {
        this.$refs.deviceTable.clearSort()
        localStorage.removeItem(this.sorterName)
        this.refresh()
      },
      refresh() {
        let sorter = localStorage.getItem(this.sorterName)
        let filter = localStorage.getItem(this.filterName)
        this.getData(this.tableName, filter, sorter)
      },
      filterChangeEvent({ filterList }) {
        console.log(filterList)
        for (let i = 0; i < filterList.length; i++) {
          let filter = filterList[i]
          let filterName = filter.column.filterRender.name
          console.log('过滤器名: ' + filterName)
          let columnName = filter.column.property
          console.log('列名: ' + columnName)
          let filterDatas = filter.datas
          if (filterName === 'FilterInput') {
            let filterData = filterDatas[0]
            console.log('过滤信息：' + filterData)
          }
          if (filterName === 'FilterComplex') {
            let filterData = filterDatas[0]
            // 运算符号 has(包含)、eq(等于)、ne(不等于)、gt(大于)、gte(大于等于)、lt(小于)、lte(小于等于)
            let operator = filterData.type
            // 输入的过滤内容
            let content = filterData.name
            console.log('操作符：' + operator)
            console.log('筛选内容：' + content)
          }
          if (filterName === 'FilterContent') {
            let filterData = filterDatas[0]
            console.log('勾选的项数：' + filterData.vals.length)
            console.log('勾选的内容：' + filterData.vals)
          }
        }
      },
    },
  }
</script>

<style scoped></style>
