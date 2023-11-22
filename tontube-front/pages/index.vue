<template>
  <main>
    <VideoPanel :vods="this.vods" />
  </main>
</template>

<script>
export default {
  name: 'index',
  layout: 'header',
  data: function(){
    return {
      vods: [],
    }
  },
  methods: {
    async getVODs() {
      await this.$axios.get('/video').then((result) => {
        for(const key in result.data) {
          let vod = result.data[key];
          this.vods.push(vod);
        }
      }).catch(error => {
        this.$toasted.global.defaultError({
          msg: "Oupss... une erreur est survenue"
        })
      });
    }
  },
  mounted() {
    this.getVODs();
  }
}
</script>
