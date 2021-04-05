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
      }"
      :sort-config="{ multiple: true, remote: true }"
      :align="allAlign"
      :data="rowData"
      @sort-change="sortChangeEvent"
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
            filters: [
              { label: '前端', value: '前端' },
              { label: '后端', value: '后端' },
            ],
          },
          {
            title: '司机名',
            field: 'driver',
            sortable: true,
          },
          {
            title: '手机号',
            field: 'phone',
            sortable: true,
          },
          {
            title: '油耗',
            field: 'fuel',
            sortable: true,
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
      let sorter = localStorage.getItem(this.sorterName)
      this.getData(this.tableName, {}, sorter)
      this.$nextTick(() => {
        // 将表格和工具栏进行关联
        const deviceTable = this.$refs.deviceTable
        deviceTable.connect(this.$refs.deviceToolbar)
      })
    },
    methods: {
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
    },
  }
</script>

<style scoped></style>
