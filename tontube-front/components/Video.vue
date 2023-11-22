<template>
  <div id="video" class="flex cursor-pointer my-4 p-1"
  :class="[isVideo ? 'flex-col bg-tertiary rounded-md hover:bg-accent_hover w-28 md:w-32 lg:w-44 xl:w-56 2xl:w-80' : '', isRecommended ? 'hover:bg-tertiary rounded-sm' : 'w-2/4 hover:bg-tertiary rounded-sm', isSearchVideo ? '' : '']">
    <NuxtLink :to="{ path: 'watch', query: { id: this.vod.id }}" id="preview" class="relative mr-2">
      <img class="w-full rounded-md" alt="thumbnail" :src="this.vod.url_thumbnail"/>
      <p id="timestamp" class="absolute bottom-1 right-1 bg-black text-white p-1 rounded">
        {{this.duration}}
      </p>
    </NuxtLink>
    <div id="info">
      <p id="title" class="mt-2 font-bold text-base w-full h-7 overflow-ellipsis overflow-hidden whitespace-nowrap">
        {{this.vod.title}}
      </p>
      <p id="username" class="font-normal" :class="[isVideo ? 'font-normal' : '', isRecommended || isSearchVideo ? 'text-sm' : '']">
        {{this.vod.username}}
      </p>
      <div id="view-info" class="flex items-start justify-between text-sm">
        <div class="flex flex-row gap-2">
          <p>
            {{this.vod.view_count}} vues
          </p>
          <p v-if="isVideo || isSearchVideo">
            •
          </p>
          <p>
            {{$moment(this.createDate,'MM-DD-YYYY').format('LL')}}
          </p>
        </div>
        <div v-if="this.isModify && (this.$auth.user.isAdmin || this.$auth.user.username === this.vod.username)" class="flex flex-row gap-2">
          <NuxtLink :to="{ name: 'update', params: { vod: this.vod }}">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-primary hover:text-accent" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
            </svg>
          </NuxtLink>
          <svg @click="this.handleDelVOD" xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-primary hover:text-accent" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
          </svg>
        </div>
      </div>
      <p v-if="isSearchVideo">
        Description
      </p>
    </div>
  </div>
</template>

<script>
import 'moment/locale/fr-ch';
export default {
  name: 'video',
  props:{
    vod: {
      type: Object,
      required: true
    },
    isVideo: {
      type: Boolean,
      default: true,
    },
    isSearchVideo: {
      type: Boolean,
      default: false,
    },
    isRecommended: {
      type: Boolean,
      default: false,
    },
    isModify: {
      type: Boolean,
      default: false,
    },
  },
  data: function(){
    return {
      duration: null,
      createDate: null,
    }
  },
  methods:{
    async handleDelVOD(){
      await this.$axios.post('/video/'+this.vod.id+'/delete').then(result =>{
        this.$toasted.global.defaultSuccess({
          msg: "Votre vidéo a bien été supprimer"
        })
        this.$router.push({ path: 'user', query: { username: this.$auth.user.username }});
      }).catch(error =>{
        this.$toasted.global.defaultError({
          msg: "Oupss... une erreur est survenue"
        })
      });
    },
  },
  mounted: function(){
    if(this.vod.duration){
      if(this.vod.duration.includes('M')){
        const durationRAW = this.vod.duration.slice(2, this.vod.duration.length-1);
        const durationSplit = durationRAW.split('M');
        const seconds = durationSplit[0] * 60 + durationSplit[1];
        this.duration = this.$moment(seconds).format('mm:ss');
      }else{
        const seconds = this.vod.duration.slice(2, this.vod.duration.length-1);
        this.duration = this.$moment().minutes(0).seconds(seconds).format('m:ss');
      }
      this.createDate = new Date(this.vod.publishedAt);
    }
  },
}
</script>
