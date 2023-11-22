<template>
  <main class="grid place-items-center h-screen">
    <div class='p-5 mx-auto w-11/12 sm:w-1/2 md:w-7/12 lg:w-1/2 xl:w-1/3 2xl:w-1/4 bg-primary border-gray-700 border-2 rounded-xl'>
      <form @submit.prevent="handleLogin" class='w-full md:w-1/2 mx-auto flex flex-col gap-3'>
        <h1 class='text-3xl text-textw text-center'>Connection</h1>
        <input type='text' v-model='userData.username' placeholder='Pseudo' autofocus/>
        <input type='password' v-model='userData.password' placeholder='Mot de passe' />
        <button class='btn-accent'>Se connecter</button>
      </form>
    </div>
  </main>
</template>

<script>
export default {
  name: 'login',
  data: function(){
    return {
      userData: {
        username: '',
        password: ''
      }
    }
  },
  methods: {
    /**
     * Handle login
     */
    async handleLogin(){
      try {
        this.$auth.loginWith('local', {
            data: this.userData
          })
        this.$toasted.global.defaultSuccess({
          msg: "Bienvenue " + this.userData.username
        })
      } catch (error) {
        this.$toasted.global.defaultError({
          msg: "Oupss... une erreur est survenue"
        })
      }
    }
  }
}
</script>
