<template>
  <div class="icon-container">
    <a-alert message="点击图标即可复制代码" type="success" show-icon></a-alert>
    <a-row :gutter="20">
      <a-col :span="24">
        <a-input-search
          v-model:value="queryForm.title"
          placeholder="图标名称"
          enter-button
          @search="queryData"
        />
      </a-col>
      <a-col
        v-for="(item, index) in queryIcon"
        :key="index"
        :lg="2"
        :md="3"
        :sm="8"
        :xl="2"
        :xs="6"
      >
        <a-card shadow="hover" @click="handleCopyIcon(item.icon, $event)">
          <vab-icon :icon="item.icon"></vab-icon>
        </a-card>
        <div class="icon-text" @click="handleCopyText(item.icon, $event)">
          {{ item.icon }}
        </div>
      </a-col>

      <a-col :span="24">
        <a-pagination
          show-quick-jumper
          v-model:current="queryForm.current"
          :page-size="queryForm.pageSize"
          :total="total"
          @change="handleCurrentChange"
          @showSizeChange="handlePageSizeChange"
        />
      </a-col>
    </a-row>
  </div>
</template>

<script>
  import { getPageData } from '@/api/table'
  import clip from '@/utils/clipboard'
  import VabIcon from '@/layout/vab-icon'

  export default {
    name: 'Icon',
    components: { VabIcon },
    data() {
      return {
        total: 0,
        queryIcon: [],
        queryForm: {
          current: 1,
          pageSize: 72,
          title: '',
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      handlePageSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.current = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.current = 1
        this.fetchData()
      },
      async fetchData() {
        // 分页条件
        let params = {
          table: 'icon',
          pageSize: this.queryForm.pageSize,
          pageNum: this.queryForm.current,
        }
        const { data } = await getPageData(params, {
          filter: {
            icon: { $regex: this.queryForm.title },
          },
        })
        this.queryIcon = data.pageData
        this.total = data.totalCnt
      },
      handleCopyText(item, event) {
        clip(item, event)
      },
      handleCopyIcon(item, event) {
        clip(`<vab-icon icon="${item}" />`, event)
      },
    },
  }
</script>

<style lang="less">
  .icon-container {
    .ant-input-search,
    .ant-alert {
      margin-bottom: @vab-padding;
    }

    .ant-card-body {
      position: relative;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      height: 68px;
      cursor: pointer;

      i {
        font-size: 28px;
        text-align: center;
        pointer-events: none;
        cursor: pointer;
      }
    }

    .icon-text {
      height: 30px;
      overflow: hidden;
      font-size: 12px;
      line-height: 30px;
      text-align: center;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
</style>
