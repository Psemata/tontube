<template>
  <main class="w-full p-2 sm:w-10/12 md:w-9/12 lg:w-1/2 mx-auto mt-10">
    <FormVOD :isUpdate="true" :vodToModify="this.vod"/>
  </main>
</template>

<script>
export default {
  name: 'update',
  layout: 'header',
  middelware: 'auth',
  data: function() {
    return {
      vod: {},
    }
  },
  mounted: function() {
    if(this.$route != undefined) this.vod = this.$route.params.vod;
  },
}
</script>
