<template>
  <div>
    <h1 v-if="this.isUpdate" class="text-text text-4xl">Modifier une vidéo</h1>
    <h1 v-else class="text-text text-4xl">Ajouter une nouvelle vidéo</h1>
    <div class="grid grid-cols-2 divide-x-2 mt-20">
      <div class="pr-5">
        <label>Titre</label><br>
        <input class="mt-6 mb-5" type="text" v-model="vodData.title" placeholder="Titre de la vidéo" autofocus required />
        <label>Description</label><br>
        <input class="mt-6 mb-5" type="text" v-model="vodData.description" placeholder="Description de la vidéo" />
        <label>Ajouter des tags</label><br>
        <input @keydown.enter="handleNewTag" v-model="newTag" class="mt-6 mb-5 w-9/12 inline" type="text" placeholder="Voyage" />
        <svg xmlns="http://www.w3.org/2000/svg" @click="handleNewTag" class="h-10 w-10 ml-5 inline text-primary hover:text-accent cursor-pointer" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
          <path strokeLinecap="round" strokeLinejoin="round" d="M12 9v3m0 0v3m0-3h3m-3 0H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        <div class="mb-5 flex flex-row flex-wrap gap-2 w-full">
          <div v-for="tag in vodData.tags" :key="tag">
            <Tag @handleDelTag="handleDelTag" :name="tag"/>
          </div>
        </div>
        <div v-if="!this.isUpdate">
          <label>Visibilité de la vidéo</label><br>
          <div class="mt-6 mb-5 flex flex-row gap-3">
            <button @click="vodData.privacy = 'public'" class='bg-tertiary p-3 border-2 font-semibold rounded-xl hover:border-accent'
            :class="vodData.privacy === 'public' ? 'border-accent' : 'border-primary'">Public</button>
            <button @click="vodData.privacy = 'private'" class='bg-tertiary p-3 border-2 font-semibold rounded-xl hover:border-accent'
            :class="vodData.privacy === 'private' ? 'border-accent' : 'border-primary'">Privé</button>
            <button @click="vodData.privacy = 'unlisted'" class='bg-tertiary p-3 border-2 font-semibold rounded-xl hover:border-accent'
            :class="vodData.privacy === 'unlisted' ? 'border-accent' : 'border-primary'">Non répertorié</button>
          </div>
        </div>
        <div v-if="!this.isUpdate">
          <label>Nom du fichier video</label><br>
          <div v-if="this.vodData.file.name"  class="flex flex-row gap-1">
            <p class="mt-6 mb-5 font-semibold text-text">{{this.vodData.file.name}}</p>
            <svg @click="handleDelVideo" xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 my-auto text-primary hover:text-accent" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
              <path stroke-linecap="round" stroke-linejoin="round" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <p v-else class="mt-6 mb-5 font-normal text-text">Aucun fichier ajouté</p>
        </div>
        <button v-if="this.isUpdate" @click="handleUpdate" class='btn-accent mt-10'>Modifier la vidéo</button>
        <button v-else @click="this.handleUpload" class='btn-accent mt-10'>Ajouter la vidéo</button>
      </div>
      <div v-if="this.isUpdate" class="pl-5 w-full h-full grid place-items-center">
          <img class="w-full rounded-md" alt="thumbnail" :src="this.vodToModify.url_thumbnail"/>
      </div>
      <div v-else @dragover.prevent @drop.prevent class="pl-5 w-full h-full grid place-items-center">
        <div class="bg-tertiary rounded-3xl">
          <input type="file" id="file" ref="file" hidden />
          <svg @drop="onDrop" @dragenter="setEnter" @dragleave="setLeave" v-if="!this.isFile" xmlns="http://www.w3.org/2000/svg" class="dropzone transform duration-200 h-44 w-44 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12" />
          </svg>
          <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-44 w-44 transition text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
            <path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'upload',
  props: {
    vodToModify: {
      type: Object,
      default: null,
      required: false
    },
    isUpdate: {
      type: Boolean,
      required: true
    }
  },
  data: function() {
    return {
      vodData: {
        title: '',  // title of the video
        description: '', // description of the video
        tags: [], // array
        privacy: 'public', // public, private, unlisted
        file: '' // video file
      },
      isFile: false,
      newTag: '',
    }
  },
  methods: {
    /**
     * Handle upload
     */
    async handleUpload(){
      const formData = new FormData();
      formData.append('title', this.vodData.title);
      formData.append('description', this.vodData.description);
      formData.append('tags', this.vodData.tags);
      formData.append('privacy', this.vodData.privacy);
      formData.append('file', this.vodData.file);
      this.$toasted.global.defaultSuccess({
            msg: "Veuillez patienter pendant l'upload de la vidéo",
          })
      try {
        await this.$axios.post('/video/upload', formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data',
            'Content-length': this.vodData.file.size
          }
        }).then(response => {
          this.$toasted.global.defaultSuccess({
            msg: "Votre vidéo a bien été ajoutée à votre chaîne"
          })
          this.$router.push({ path: 'user', query: { username: this.$auth.user.username }})
        }).catch(error => {
          this.$toasted.global.defaultError({
            msg: "Une erreur est survenue lors de l'ajout de la vidéo"
          })
        });
      } catch (error) {
        this.$toasted.global.defaultError({
          msg: "Oupss... une erreur est survenue"
        })
      }
    },
    handleUpdate(){
      const formData = new FormData();
      formData.append('id', this.vodToModify.id);
      formData.append('title', this.vodData.title);
      formData.append('description', this.vodData.description);
      formData.append('tags', this.vodData.tags);
      try {
        this.$axios.post('/video/update/', formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data',
          }
        }).then(result => {
          this.$toasted.global.defaultSuccess({
            msg: "Votre vidéo a bien été modifiée"
          })
          this.$router.push({ path: 'user', query: { username: this.$auth.user.username }})
        }).catch(error => {
          this.$toasted.global.defaultError({
            msg: "Oupss... une erreur est survenue"
          })
        });
      } catch (error) {
        this.$toasted.global.defaultError({
          msg: "Oupss... une erreur est survenue"
        })
      }
    },
    /**
     * Add a new tag only if is not empty and not already in the array
     */
    handleNewTag(){
      if(this.newTag.length > 0 && this.vodData.tags.indexOf(this.newTag) === -1){
        this.vodData.tags.push(this.newTag)
        this.newTag = ''
      }
    },
    /**
     * Delete a tag
     */
    handleDelTag(tag){
      this.vodData.tags.splice(this.vodData.tags.indexOf(tag), 1)
    },
    /**
     * Delete the video file
     */
    handleDelVideo(){
      this.isFile = false
      this.vodData.file = ''
    },
    /**
     * Handle drag in
     */
    setEnter(e) {
      if (e.target.classList.contains("dropzone")) {
        e.target.classList.add("scale-125");
      }
    },
    /**
     * Handle drag out
     */
    setLeave(e) {
      if (e.target.classList.contains("dropzone")) {
        e.target.classList.remove("scale-125");
      }
    },
    /**
     * Handle drop
     */
    onDrop(e) {
      e.preventDefault();
      document.getElementById("file").files = e.dataTransfer.files;
      this.vodData.file =  document.getElementById("file").files[0];
      this.isFile = true;
      this.setLeave(e);
    },
    setVODData(){
      this.vodData.title = this.vodToModify.title
      this.vodData.description = this.vodToModify.description
      this.vodData.tags = this.vodToModify.tags
      this.vodData.privacy = this.vodToModify.privacy
    }
  },
  watch: {
    vodToModify: function(){
      if(this.isUpdate){
        this.setVODData()
      }
    }
  },
}
</script>
