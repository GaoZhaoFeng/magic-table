<template>
  <ag-grid-vue
    style="width: 500px; height: 500px"
    class="ag-theme-alpine"
    :columnDefs="columnDefs"
    :rowData="rowData"
  />
</template>

<script>
  import 'ag-grid-community/dist/styles/ag-grid.css'
  import 'ag-grid-community/dist/styles/ag-theme-alpine.css'
  import { AgGridVue } from 'ag-grid-vue3'

  export default {
    name: 'App',
    data() {
      return {
        columnDefs: null,
        rowData: null,
      }
    },
    components: {
      AgGridVue,
    },
    beforeMount() {
      this.columnDefs = [
        { field: 'make' },
        { field: 'model' },
        { field: 'price' },
      ]

      this.rowData = [
        { make: 'Toyota', model: 'Celica', price: 35000 },
        { make: 'Ford', model: 'Mondeo', price: 32000 },
        { make: 'Porsche', model: 'Boxter', price: 72000 },
      ]
    },
  }
</script>
<style scoped></style>
