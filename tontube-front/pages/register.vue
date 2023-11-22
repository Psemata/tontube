<template>
  <main class="grid place-items-center h-screen">
    <div class='p-5 mx-auto w-11/12 sm:w-1/2 md:w-7/12 lg:w-1/2 xl:w-1/3 2xl:w-1/4 bg-primary border-gray-700 border-2 rounded-xl'>
      <form @submit.prevent="handleRegister" class='w-full md:w-1/2 mx-auto flex flex-col gap-3'>
        <h1 class='text-3xl text-textw text-center'>Inscription</h1>
        <input type='text' v-model='userData.username' placeholder='Pseudo' autofocus/>
        <input type='text' v-model='userData.email' placeholder='Email' />
        <input type='password' v-model='userData.password' placeholder='Mot de passe' />
        <input type='password' v-model='userData.password2' placeholder='Confirmer le mot de passe' />
        <button class='btn-accent'>S'inscrire</button>
      </form>
    </div>
  </main>
</template>

<script>
export default {
  name: 'register',
  data: function(){
    return {
      userData:{
        username: "",
        email: "",
        password: "",
        password2: ""
      },
    }
  },
  methods:{
    /**
     * Handle register
     */
    handleRegister: async function(){
      await this.$axios.post('/auth/register', {"username": this.userData.username, "email": this.userData.email, "password": this.userData.password, "password2": this.userData.password2}).then((result) => {
        this.$toasted.global.defaultSuccess({
          msg: 'Votre compte à été créé'
        });

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
      }).catch((error) => {
        Object.keys(error.response.data).forEach((key) => {
          this.$toasted.global.defaultError({
          msg: error.response.data[key]
          })
        });
      });
    },
  }
}
</script>
