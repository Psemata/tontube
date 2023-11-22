<template>
  <div class="bg-tertiary rounded-xl flex flex-row">
    <p class="text-text font-semibold p-2">{{this.name}}</p>
    <svg @click="handleClic" xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 my-auto text-primary hover:text-accent" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
      <path stroke-linecap="round" stroke-linejoin="round" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
    </svg>
  </div>
</template>

<script>
export default {
  name: 'tag',
  props: ['name'],
  methods: {
    handleClic(){
      this.$emit('handleDelTag', this.name)
    }
  }
}
</script>
