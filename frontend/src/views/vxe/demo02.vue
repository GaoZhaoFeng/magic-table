<template>
  <div>
    <p>
      <vxe-button content="默认尺寸"></vxe-button>
      <vxe-button size="medium" content="中等尺寸"></vxe-button>
      <vxe-button size="small" content="小型尺寸"></vxe-button>
      <vxe-button size="mini" content="超小尺寸"></vxe-button>
    </p>
    <vxe-table border height="400" :data="tableData">
      <vxe-table-column type="seq" width="60"></vxe-table-column>
      <vxe-table-column
        field="name"
        title="文本筛选"
        :filters="[{ data: null }]"
        :filter-render="{ name: 'FilterInput' }"
      ></vxe-table-column>
      <vxe-table-column
        field="role"
        title="实现条件的筛选"
        :filters="[{ data: { type: 'has', name: '' } }]"
        :filter-render="{ name: 'FilterComplex' }"
      ></vxe-table-column>
      <vxe-table-column
        field="age"
        title="实现内容的筛选"
        :filters="[{ data: { vals: [], sVal: '' } }]"
        :filter-render="{ name: 'FilterContent' }"
      ></vxe-table-column>
      <vxe-table-column
        field="address"
        title="实现Excel复杂的筛选"
        sortable
        :filters="[
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
        ]"
        :filter-render="{ name: 'FilterExcel' }"
      ></vxe-table-column>
    </vxe-table>
  </div>
</template>

<script>
  import VXETable from 'vxe-table'
  import { defineComponent, ref } from 'vue'
  import XEUtils from 'xe-utils'

  // 创建一个简单的输入框筛选
  VXETable.renderer.add('FilterInput', {
    // 筛选模板
    renderFilter(renderOpts, params) {
      return [<filter-input params={params}></filter-input>]
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
      return [<filter-complex params={params}></filter-complex>]
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
      return [<filter-content params={params}></filter-content>]
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
      return [<filter-excel params={params}></filter-excel>]
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
  export default defineComponent({
    setup() {
      const tableData = ref([
        {
          id: 10001,
          name: 'Test1',
          role: 'Develop',
          sex: 'Man',
          age: '28',
          address: 'Shenzhen',
        },
        {
          id: 10002,
          name: 'Test2',
          role: 'Test',
          sex: 'Women',
          age: '22',
          address: 'Guangzhou',
        },
        {
          id: 10003,
          name: 'Test3',
          role: 'PM',
          sex: 'Man',
          age: '32',
          address: 'Shanghai',
        },
        {
          id: 10004,
          name: 'Test4',
          role: 'Designer',
          sex: 'Women ',
          age: '23',
          address: 'Shenzhen',
        },
        {
          id: 10005,
          name: 'Test5',
          role: 'Develop',
          sex: 'Women ',
          age: '30',
          address: 'Shanghai',
        },
        {
          id: 10006,
          name: 'Test6',
          role: 'Designer',
          sex: 'Women ',
          age: '21',
          address: 'Shenzhen',
        },
        {
          id: 10007,
          name: 'Test7',
          role: 'Test',
          sex: 'Man ',
          age: '29',
          address: 'Shenzhen',
        },
        {
          id: 10008,
          name: 'Test8',
          role: 'Develop',
          sex: 'Man ',
          age: '35',
          address: 'Shenzhen',
        },
      ])

      return {
        tableData,
      }
    },
  })
</script>

<style scoped></style>
