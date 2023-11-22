<template>
  <div class="w-10/12 md:w-9/12 lg:w-9/12 xl:w-10/12 2xl:w-11/12 mx-auto grid grid-cols-1 sm:grid-cols-2 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 2xl:grid-cols-5">
    <div v-for="(vod, index) in vods" :key="index" >
      <Video :vod="vod" :isModify="isModify" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'VideoPanel',
  layout: 'header',
  props: {
    vods: {
      type: Array,
      default: () => [],
      required: true
    },
    isModify: {
      type: Boolean,
      default: false,
    }
  }
}
</script>


