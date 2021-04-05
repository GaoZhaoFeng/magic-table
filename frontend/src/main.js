import { createApp } from 'vue'
import Antd from 'ant-design-vue'
import App from './App'
import router from './router'
import store from './store'
import 'ant-design-vue/dist/antd.css'
import '@/vab'
import 'xe-utils'
import VXETable from 'vxe-table'
import 'vxe-table/lib/style.css'

import FilterInput from './components/filters/FilterInput.vue'
import FilterContent from './components/filters/FilterContent.vue'
import FilterComplex from './components/filters/FilterComplex.vue'
import FilterExcel from './components/filters/FilterExcel.vue'

createApp(App)
  // 引用第三方库
  .use(store)
  .use(router)
  .use(Antd)
  .use(VXETable)
  // 自定义vxe-table的过滤模型
  .component(FilterInput.name, FilterInput)
  .component(FilterContent.name, FilterContent)
  .component(FilterComplex.name, FilterComplex)
  .component(FilterExcel.name, FilterExcel)
  .mount('#app')
