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
      :data="tableData"
    >
      <vxe-table-column type="seq" title="序号" width="60" />
      <vxe-table-column field="name" title="姓名" />
      <vxe-table-column field="sex" title="性别" />
      <vxe-table-column field="age" title="年龄" />
    </vxe-table>
  </div>
</template>

<script>
  export default {
    name: 'demo01',
    data() {
      return {
        allAlign: null,
        tableData: [
          {
            id: 10001,
            name: 'Test1',
            role: 'Develop',
            sex: 'Man',
            age: 28,
            address: 'vxe-table 从入门到放弃',
          },
          {
            id: 10002,
            name: 'Test2',
            role: 'Test',
            sex: 'Women',
            age: 22,
            address: 'Guangzhou',
          },
          {
            id: 10003,
            name: 'Test3',
            role: 'PM',
            sex: 'Man',
            age: 32,
            address: 'Shanghai',
          },
          {
            id: 10004,
            name: 'Test4',
            role: 'Designer',
            sex: 'Women ',
            age: 24,
            address: 'Shanghai',
          },
        ],
      }
    },
  }
</script>

<style scoped></style>
