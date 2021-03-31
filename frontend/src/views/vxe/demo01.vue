<template>
  <div>
    <vxe-toolbar>
      <template #buttons>
        <vxe-button @click="allAlign = 'left'">居左</vxe-button>
        <vxe-button @click="allAlign = 'center'">居中</vxe-button>
        <vxe-button @click="allAlign = 'right'">居右</vxe-button>
      </template>
    </vxe-toolbar>
    <vxe-table
      border
      show-header-overflow
      show-overflow
      highlight-hover-row
      :align="allAlign"
      :data="rowData"
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
          },
          {
            title: '司机名',
            field: 'driver',
          },
          {
            title: '手机号',
            field: 'phone',
          },
          {
            title: '油耗',
            field: 'fuel',
          },
        ],
        rowData: null,
        current: 1,
        pageSize: 15,
        total: 0,
      }
    },
    mounted() {
      this.getData('device')
    },
    methods: {
      getData(table) {
        this.loading = true
        // 分页条件
        let params = {
          table: table,
          pageSize: this.pageSize,
          pageNum: this.current,
        }
        getPageData(params, { filter: {}, sorter: {} }).then((res) => {
          this.rowData = res.data.pageData
          this.total = res.data.totalCnt
          this.loading = false
        })
      },
      handlePageChange({ currentPage, pageSize }) {
        this.current = currentPage
        this.pageSize = pageSize
        this.getData('device')
      },
    },
  }
</script>

<style scoped></style>
