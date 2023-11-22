<template>
  <NuxtLink :to="{ path: 'user', query: { username: this.user.username }}" id="searchuser" class="flex flex-col bg-tertiary text-center h-20 cursor-pointer w-2/4 my-2 p-1 border-2 rounded-3xl hover:bg-accent_hover">
    <p class="text-2xl my-auto text-text font-bold">
      {{this.user.username}}
    </p>
  </NuxtLink>
</template>

<script>
export default {
  props: {
    user: {
      type: Object,
      required: true
    }
  }
}
</script>
