<template>
  <div>
    <div v-if="!isLogin">
      <BannerDetail></BannerDetail>
      <FeatureListDetail></FeatureListDetail>
    </div>

    <div v-if="isLogin">
      <Analyze></Analyze>
    </div>

  </div>

</template>

<script>
  // @ is an alias to /src
  import BannerDetail from '@/components/homePageDetail/BannerDetail.vue'
  import FeatureListDetail from '@/components/homePageDetail/FeatureListDetail.vue'
  import Analyze from "@/views/Analyze";

  export default {
    name: 'home',
    components: {
      Analyze,
      BannerDetail,
      FeatureListDetail
    },
    computed: {
      isLogin() {
        return this.$store.state.userInfo.isLogin
      },
    }
  }
  
</script>
<style>
  .navbar-submenu-menu {
    margin-top: -5px;
    left: initial !important;
    right: 0;
  }

  .el-menu--horizontal > .navbar-submenu.is-active .el-submenu__title {
    border-bottom: 0;
  }

  .el-submenu.is-active .el-submenu__title {
    border-bottom: 0;
  }
</style>