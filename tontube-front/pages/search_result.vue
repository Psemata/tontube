<template>
  <div class="grid place-items-center h-screen">
    <div v-if="this.isOk">
      <div v-for="(item, id) in searchedResult.slice(beginIndex, endIndex)" :key="id">
        <SearchUser v-if="item.email" :user="item"/>
        <Video v-else :vod="item" :isVideo='false' :isSearchVideo='true' :isRecommended='false'/>
      </div>
      <!-- Pagination tool -->
      <v-pagination v-model="currentPage" :page-count="maxPageCount" :classes="paginationStyle" :labels="paginationText"></v-pagination>
    </div>
    <p v-if="this.isEmpty" class="font-semibold text-text text-2xl">Aucune vidéo trouvé ...</p>
  </div>
</template>

<script>
import vPagination from 'vue-plain-pagination'

export default {
  layout: 'header',
  components: { vPagination },
  data() {
    return {
      searchedResult : [],
      currentPage: 1,
      numberByPage: 5,
      isOk: false,
      isEmpty: false,
      paginationStyle: {
        ul: 'flex flex-row gap-0.5',
        li: 'w-14 h-10 border-solid border-2 border-primary rounded-md text-center',
        liActive: 'bg-accent_hover',
        liDisable: 'bg-tertiary',
        button: 'w-full h-full',
        buttonActive: 'w-full h-full',
        buttonDisable: 'w-full h-full'
      },
      paginationText: {
        first: '<<',
        prev: '<',
        next: '>',
        last: '>>'
      }
    }
  },
  computed : {
    beginIndex : function() {
      return (this.currentPage - 1) * this.numberByPage;
    },
    endIndex : function() {
      return (this.currentPage * this.numberByPage);
    },
    maxPageCount : function() {
      let maxPages = Math.ceil(this.searchedResult.length / this.numberByPage);
      return maxPages
    },
  },
  methods: {
    async globalResearch(keywords) {
      await this.$axios.get('/search', { params : {"query" : keywords}}).then(response => {
        this.searchedResult = [];
        for(const key in response.data['users']) {
          let user = response.data['users'][key];
          this.searchedResult.push(user);
        }
        for(const key in response.data['videos']) {
          let vod = response.data['videos'][key];
          this.searchedResult.push(vod);
        }
        this.isOk = true;
        this.isEmpty = false;
      }).catch(error => {
        this.isEmpty = true;
        this.isOk = false;
      });
    },
    async localResearch(keywords) {
      await this.$axios.get('/search/user/', { params : {"query" : keywords}}).then(response => {
        for(const key in response.data) {
          let vod = response.data[key];
          this.searchedResult.push(vod);
        }
        this.isOk = true;
        this.isEmpty = false;
      }).catch(error => {
        this.isEmpty = true;
        this.isOk = false;
      });
    },
    search(){
      this.searchedResult = [];
      if(this.$route.query.isGlobal) {
        this.globalResearch(this.$route.query.searchKeyWords);
      } else {
        this.localResearch(this.$route.query.searchKeyWords);
      }
    }
  },
  mounted(){
    this.search();
  },
  watch:{
    $route(to, from){
      this.search();
    }
  }
}
</script>
