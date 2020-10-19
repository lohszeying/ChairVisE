<template>
  <el-main>
    <h1 class="alignLeft">My Created Conferences </h1>
    <el-button class="alignRight" type="primary" icon="el-icon-plus"
           v-if="!isConferenceListEmpty" @click="createConference">Add New Conference</el-button>
    <br/>
    <el-divider></el-divider>
    <div class="infinite-list-wrapper">
      <el-card v-if="isConferenceListEmpty" >
        <EmptyConference />
      </el-card>
      <ul class="infinite-list" v-infinite-scroll="loadMoreConference" infinite-scroll-disabled="disabled" v-loading="isLoading">
        <li v-for="(conference, index) in conferences" :key="conference.id">
          <zoom-center-transition :duration="500" :delay="100 * (index - 1)">
            <el-card shadow="hover">
              <el-button type="text" class="conferenceCard" v-show="show" @click="viewConference(conference.id)">
                <el-row>
                  <el-col class="conference-id" :span="1">
                    <p> #{{conference.id}} </p>
                  </el-col>
                  <el-col :span="19" :offset="1">
                    <p> {{ conference.name }} </p>
                  </el-col>
                  <el-col :span="19" :offset="1">
                    <p>{{ conference.description }}</p>
                  </el-col>
                </el-row>
              </el-button>
            </el-card>
          </zoom-center-transition>
        </li>
      </ul>
    </div>
  </el-main>
</template>

<script>
  import {ZoomCenterTransition} from 'vue2-transitions'
  import EmptyConference from '@/components/emptyStates/EmptyConference.vue'

  export default {
    name: 'Manage',
    props: {
      id: String,
    },
    data() {
      return {
        show: false,
        count: 0
      }
    },
    watch: {
      'isError'() {
        if (!this.isError) {
          return
        }
        this.$notify.error({
          title: 'Conference list API request fail',
          message: this.$store.state.presentation.presentationListStatus.apiErrorMsg,
          duration: 0
        });
      }
    },
    computed: {
      isLogin() {
        return this.$store.state.userInfo.isLogin
      },
      isAppLoading() {
        return this.$store.state.isPageLoading
      },
      isLoading() {
        return this.$store.state.conference.conferenceListStatus.isLoading
      },
      conferences() {
        return this.$store.state.conference.conferenceList;
      },
      isConferenceListEmpty() {
        return this.$store.state.conference.conferenceList.length <= 0;
      },
      isError() {
        return this.$store.state.conference.conferenceListStatus.isApiError
      },
    },
    components: {
      ZoomCenterTransition,
      EmptyConference
    },
    methods: {
      createConference() {
        this.$router.push("/manage/create");
      },
      loadConferences() {
        this.show = true;
      },
      loadMoreConference () {
        this.count += 5
      },
      viewConference(id) {
        this.$router.push("/manage/" + id);
      }
    },
    mounted() {
      this.$store.dispatch('getConferenceList')
      this.loadConferences();
    }
  }
</script>

<style scoped>
  .alignLeft {
    float: left;
    display: inline-block;
    margin: 0;
  }
  .alignRight {
    float: right;
    display: inline-block;
    margin: 0;
  }
  .background {
    background-color: transparent;
    border-style: hidden;
  }
  .conferenceCard {
    width: 100%;
    height: 100%;
    margin-bottom: 16px;
    background-color: white;
    text-align:left;
    color: black;
    padding: 4px 16px;
  }

  .el-card__body {
  }
  .menuCard {
    width: 100%;
    height: 100%;
  }
  .infinite-list {
    list-style: none;
    padding: 0;
    margin: 0;
  }
  .conferenceCard .button {
    color: black;
    text-align: left;
  } 
  .presentation-image {
    text-align: center;
    vertical-align: middle;
    margin-top: 1rem;
  }
  .conference-id {
    margin-top: 1.7rem;
  }
</style>