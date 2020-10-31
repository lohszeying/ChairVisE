<template>
  <el-main>
    <h1 class="alignLeft">My Created Conferences </h1>
    <div class="btn-group">
    <el-button class="alignRight" type="primary" icon="el-icon-plus"
           v-if="!isConferenceListEmpty" @click="createConference">Import Data</el-button>
    <el-button class="alignRight-wrap" icon="el-icon-sort" v-model="conferences"
                 v-if="!isConferenceListEmpty" @click="sortConferences">Sort</el-button>
    <br/>
    </div>
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
                  <el-col class="conference-title" :span="19" :offset="1">
                    <p> {{ conference.title }} </p>
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
        confList: [],
        show: false,
        count: 0,
        sortAscend: false,
        confListLength: 0,
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
      },
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
      conferences: {
        get() {
          if (this.confList.length > 0 && this.confList.length === this.$store.state.conference.conferenceList.length) {
            //If sort button is pressed, will call this part
            //console.log("here 1");
            return this.confList;
          } else if (this.confList.length != this.$store.state.conference.conferenceList.length) {
            //After deleting a conference and going back to this page
            //console.log("here 2");
            return this.$store.state.conference.conferenceList;
          } else {
            //When refreshing page
            //console.log("here 3");

            this.confList = this.$store.state.conference.conferenceList;
            return this.confList;
          }
        },
        set(payload) {
          this.confList = payload;
        },
      },
      /*conferences() {
        return this.$store.state.conference.conferenceList;
      },*/
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
        //this.$router.push("/manage/create");
        this.$router.push("/importData");
      },
      sortConferences() {
        //Sort according to conference title
        if (!this.sortAscend) {
          //Sort ascending order
          let sortedConfList = Array.from(new Set(this.$store.state.conference.conferenceList)).sort((a, b) => (a.title > b.title) ? 1 : -1);
          this.conferences = sortedConfList;
          this.sortAscend = true;
        } else {
          //Sort descending order
          let sortedConfList = Array.from(new Set(this.$store.state.conference.conferenceList)).sort((a, b) => (a.title < b.title) ? 1 : -1);
          this.conferences = sortedConfList;
          this.sortAscend = false;
        }
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
      this.$store.dispatch('getConferenceList');
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
  .alignRight-wrap {
    float: right;
    display: inline-block;
    margin-right: 5px;
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
    margin-top: 1rem;
    font-size: 13px;
  }
  .conference-title {
    font-size: 17px;
  }
</style>