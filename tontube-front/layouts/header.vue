<template>
  <div class="h-28 bg-primary">
    <nav class="h-full flex justify-between items-center w-full">
      <NuxtLink class="flex-1 text-textw font-bold text-center text-5xl" to="/">
        TonTube
      </NuxtLink>

      <SearchBar class="flex-0" :iconcolor="'text-textw'" :isGlobal="true" />

      <div class="flex-1">
        <div v-if="$auth.loggedIn" class="flex flex-row gap-4 justify-center">
          <NuxtLink :to="{ path: 'user', query: { username: this.$auth.user.username }}" class="text-accent hover:text-accent_hover text-center pt-2 my-auto mx-11 text-2xl">
            {{$auth.user.username}}
          </NuxtLink>
          <button @click="$auth.logout()" class="text-textw p-2 font-semibold text-xl border-2 rounded-lg hover:text-accent hover:border-accent" >Déconnexion</button>
        </div>
        <div v-else class="flex flex-row justify-center gap-4">
          <NuxtLink to="/register" class="text-textw p-2 font-semibold text-xl border-2 rounded-lg hover:text-accent hover:border-accent">S'inscrire</NuxtLink>
          <NuxtLink to="/login" class="text-textw p-2 font-semibold text-xl hover:text-accent">Connexion</NuxtLink>
        </div>
      </div>
    </nav>
    <Nuxt />
  </div>
</template>

<script>
export default {
  name: 'header'
}
</script>
