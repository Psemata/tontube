<template>
  <div>
    <div class="relative min-h-screen mx-auto grid pr-7 grid-cols-10 bg-background">
      <div class="col-span-8">
        <iframe class="w-11/12 h-2/3 m-8" :src="'https://www.youtube.com/embed/' + this.vod.id" title="YouTube video player" frameborder="0"
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
              allowfullscreen></iframe>
        <div id="v_informations" class="mx-10 text-text">
          <p class="text-4xl font-bold">
            {{this.vod.title}}
          </p>
          <p class="text-base font-bold mb-3">
            {{this.vod.username}}
          </p>
          <p class="text-base mb-3">
            {{$moment(this.createDate,'MM-DD-YYYY').format('LL')}}
          </p>
          <p class="text-xl">
            {{this.vod.description}}
          </p>
        </div>
      </div>
      <div class="text-xl mt-8 col-span-2 text-text">
        <p>
          Autres vidéos
        </p>
        <div id="recommendations" v-for="recommended in othersVideos" :key="recommended.id">
          <Video @click="this.hangleChangeVOD(recommended.id)" v-if="vod.id != recommended.id" :isRecommended='true' :isVideo='false' :isSearchVideo='false' :vod="recommended" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  layout: 'header',
  data() {
    return {
      vod: Object,
      createDate: Date,
      othersVideos: Object,
    }
  },
  methods : {
    async getVOD(id){
      await this.$axios.get('/video/'+id).then(response => {
        this.vod = response.data[1];
        this.createDate = new Date(this.vod.publishedAt)
      }).catch(error => {
        this.$toasted.global.defaultError({
          msg: "Oupss... une erreur est survenue"
        })
      });
    },
    async getUserVODs(){
      await this.$axios.get('/video/user/' + this.vod.username).then(response => {
        this.othersVideos = response.data;
      }).catch(error => {
        this.$toasted.global.defaultError({
          msg: "Oupss... une erreur est survenue"
        })
      });
    },
    hangleChangeVOD(id){
      this.$router.push({ name: 'watch', query: { id: id } });
    },
    setupVOD(){
      if(this.$route != undefined){
        Promise.resolve(this.getVOD(this.$route.query.id)).then(result => {
          this.getUserVODs();
        }).catch(error => {
          console.log(error)
        })
      }
    }
  },
  mounted: function() {
    this.setupVOD();
  },
  watch:{
    $route: function(to, from){
      this.setupVOD();
    }
  }
}
</script>
