<template>
  <div class="relative min-h-screen mx-auto bg-background">
    <div id="headbar" class="flex justify-between w-full h-28 px-14">
      <div class="my-auto">
        <p class="my-auto font-semibold text-2xl text-text">
          {{username}}
        </p>
      </div>
      <div class="my-auto">
        <SearchBar v-bind:iconcolor="'text-primary'" v-bind:isGlobal="false" />
      </div>
      <div class="my-auto w-52">
        <NuxtLink to="/upload" class="btn-accent p-3">Poster une vidéo</NuxtLink>
      </div>
    </div>
    <div id="video panel" class="px-14 text-text">
      <p class="text-lg">
        Vidéos de l'utilisateur
      </p>
      <VideoPanel :vods="this.uservods" :isModify="true" />
    </div>
  </div>
</template>

<script>
export default {
  layout: 'header',
  data() {
    return {
      username : '',
      uservods : [],
    }
  },
  methods : {
    async getUserVODs(){
      await this.$axios.get('video/user/' + this.username).then(response => {
        for(const key in response.data) {
          let vod = response.data[key];
          this.uservods.push(vod);
        }
      }).catch(error => {
        this.$toasted.global.defaultError({
          msg: "Oupss... une erreur est survenue"
        })
      });
    }
  },
  mounted: function() {
    if(this.$route != undefined) {
      this.username = this.$route.query.username;
      this.getUserVODs();
    }
  }
}
</script>
