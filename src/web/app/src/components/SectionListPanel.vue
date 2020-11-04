<template>
  <div>
    <div v-loading="isLoadingDBMetaData || isLoadingSectionList" v-if="!isNewPresentation">
      <el-aside width="300px" class="addRowRightAlign" v-if="isLogin">
        <el-card v-if="!isVersionListEmpty" >
          <div slot="header" class="clearfix">
            <span> Select version </span>
          </div>
          <el-select class= "versionInput" v-model="presentationFormVersion" placeholder="Please select a version" >
            <el-option v-for="v in versions" :key="v" :label="v" :value="v">
            </el-option>
          </el-select>        
        </el-card>

        <el-card v-if="!isVersionListEmpty" class="versionInfo">
          <div slot="header" class="clearfix">
            <span> Version information </span>
          </div>
          <el-button class="recordButton" type="secondary" icon="el-icon-success" v-if="isAuthorUploaded">Author record uploaded</el-button>
          <el-button class="recordButton" type="info" icon="el-icon-warning" v-if="!isAuthorUploaded">No author record found</el-button>
          <el-button class="recordButton" type="secondary" icon="el-icon-success" v-if="isReviewUploaded">Review record uploaded</el-button>
          <el-button class="recordButton" type="info" icon="el-icon-warning" v-if="!isReviewUploaded">No review record found</el-button>
          <el-button class="recordButton" type="secondary" icon="el-icon-success" v-if="isSubmissionUploaded">Submission record uploaded</el-button>
          <el-button class="recordButton" type="info" icon="el-icon-warning" v-if="!isSubmissionUploaded">No submission record found</el-button>
        </el-card>

        <el-card v-if="isOwner">
          <div slot="header" class="clearfix">
            <span> Add section </span>
          </div>
          <el-select class= "selectionInput" v-model="selectedNewSection" placeholder="Please select a section to add"
                    filterable>
            <el-option-group
              v-for="group in predefinedSections"
              :key="group.label"
              :label="group.label">
              <el-option
                v-for="item in group.options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-option-group>
          </el-select>
          <el-button class="selectionInputButton" icon="el-icon-plus" type="success" v-if="isOwner" @click="addNewSection">Add New Section</el-button>
        </el-card>
      </el-aside>
      <br/>
      <el-alert
        v-if="isSectionListApiError"
        :title="sectionListApiErrorMsg"
        type="error" show-icon>
      </el-alert>
      <el-card shadow="hover">
        <abstract-section-detail class="presentation-section" v-for="section in sectionList" :sectionDetail="section"
                                 :key="section.id" versionId="presentationId" :version="presentationFormVersion"/>
        <EmptySection v-if="isSectionListEmpty" />
      </el-card>
    </div>
  </div>
</template>

<script>
  import AbstractSectionDetail from "@/components/AbstractSectionDetail.vue"
  import {ID_NEW_PRESENTATION} from "@/common/const";
  import {ID_NEW_CONFERENCE} from "@/common/const";
  import PredefinedQueries from "@/store/data/predefinedQueries"
  import EmptySection from "@/components/emptyStates/EmptySection.vue"

  export default {
    props: {
      presentationId: String,
      id: String,
    },
    watch: {
      presentationId: 'fetchSectionList',
      'presentationFormVersion'() {
        this.updateVersion();
      },
    },
    beforeCreate() {
      this.recordList = '';
      console.log("clearing this record list");
      this.$store.commit('resetRecordList');
    },
    data() {
      return {
        selectedNewSection: '',
        presentationFormVersion: '',
        verId: '',
        getUpdatedVerList: false,
        recordList: '',
      }
    },
    computed: {
      isLogin() {
        return this.$store.state.userInfo.isLogin;
      },
      isOwner() {
        return this.$store.state.userInfo.isLogin && (this.$store.state.conference.conferenceForm.userEmail === this.$store.state.userInfo.userEmail);
      },

      isPresentationEditable() {
        return this.$store.state.presentation.isPresentationEditable;
      },

      predefinedSections() {

        let sectionOptionsGroup = {};
        // grouping the predefined queries
        for (let key in PredefinedQueries) {
          if (!PredefinedQueries.hasOwnProperty(key)) {
            continue;
          }
          let groupName = PredefinedQueries[key].group;
          if (sectionOptionsGroup[groupName] === undefined) {
            sectionOptionsGroup[groupName] = [];
          }

          sectionOptionsGroup[groupName].push({
            value: key,
            label: PredefinedQueries[key].name,
          })

          /*if (this.recordList.authorRecord && this.recordList.reviewRecord && this.recordList.submissionRecord) {
            sectionOptionsGroup[groupName].push({
              value: key,
              label: PredefinedQueries[key].name,
            })
            break;
          } else if (this.recordList.authorRecord && this.recordList.submissionRecord) {
            if (groupName === 'Co-authorship' || groupName === 'Author Record + Submission Record'
                || groupName === 'Author Record' || groupName === 'Submission Record' ) {

              sectionOptionsGroup[groupName].push({
                value: key,
                label: PredefinedQueries[key].name,
              })
            }
            break;
          } else if (this.recordList.authorRecord && this.recordList.reviewRecord) {
            if (groupName === 'Author Record + Review Record' || groupName === 'Author Record' || groupName === 'Review Record') {
              sectionOptionsGroup[groupName].push({
                value: key,
                label: PredefinedQueries[key].name,
              })
            }
            break;
          } else if (this.recordList.reviewRecord && this.recordList.submissionRecord) {
            if (groupName === 'Review Record + Submission Record' || groupName === 'Review Record' || groupName === 'Submission Record') {
              sectionOptionsGroup[groupName].push({
                value: key,
                label: PredefinedQueries[key].name,
              })
            }
            break;
          } else if (this.recordList.authorRecord) {
            if (groupName === 'Author Record') {
              sectionOptionsGroup[groupName].push({
                value: key,
                label: PredefinedQueries[key].name,
              })
            }
            break;
          } else if (this.recordList.reviewRecord) {
            if (groupName === 'Review Record') {
              sectionOptionsGroup[groupName].push({
                value: key,
                label: PredefinedQueries[key].name,
              })
            }
            break;
          } else if (this.recordList.submissionRecord) {
            if (groupName === 'Submission Record') {
              sectionOptionsGroup[groupName].push({
                value: key,
                label: PredefinedQueries[key].name,
              })
            }
            break;
          } */

        }

        // generate to format that element ui requires
        let sectionOptions = [];
        for (let groupName in sectionOptionsGroup) {
          if (!sectionOptionsGroup.hasOwnProperty(groupName)) {
            continue;
          }
          sectionOptions.push({
            label: groupName,
            options: sectionOptionsGroup[groupName]
          })
        }

        if (!this.recordList.authorRecord && !this.recordList.reviewRecord && !this.recordList.submissionRecord) {
          sectionOptions = [];
        } else if (!this.recordList.authorRecord && !this.recordList.reviewRecord) {
          //Only has submission
          sectionOptions.splice(0,1);
          sectionOptions.splice(1,1);
          sectionOptions.splice(1,1);
          sectionOptions.splice(1,1);
          sectionOptions.splice(1,1);
          sectionOptions.splice(1,1);
        } else if (!this.recordList.authorRecord && !this.recordList.submissionRecord) {
          //Only has review
          sectionOptions.splice(0,1);
          sectionOptions.splice(0,1);
          sectionOptions.splice(1,1);
          sectionOptions.splice(1,1);
          sectionOptions.splice(1,1);
          sectionOptions.splice(1,1);
        } else if (!this.recordList.reviewRecord && !this.recordList.submissionRecord) {
          //Only has author
          sectionOptions.splice(0,1);
          sectionOptions.splice(0,1);
          sectionOptions.splice(0,1);
          sectionOptions.splice(1,1);
          sectionOptions.splice(1,1);
          sectionOptions.splice(1,1);
        } else if (!this.recordList.authorRecord) {
          //Only have review + submission (no author)
          sectionOptions.splice(0,1);
          sectionOptions.splice(2,1);
          sectionOptions.splice(2,1);
          sectionOptions.splice(3,1);
        } else if (!this.recordList.reviewRecord) {
          //Only have author + submission (no review)
          sectionOptions.splice(2,1);
          sectionOptions.splice(4,1);
          sectionOptions.splice(4,1);
        } else if (!this.recordList.submissionRecord) {
          sectionOptions.splice(0,1);
          sectionOptions.splice(0,1);
          sectionOptions.splice(2,1);
          sectionOptions.splice(2,1);
        }


        //console.log(sectionOptions);

        return sectionOptions;
      },

      isNewPresentation() {
        return this.presentationId === ID_NEW_PRESENTATION
      },
      isNewConference() {
        return this.id === ID_NEW_CONFERENCE
      },

      sectionList() {
        return this.$store.state.section.sectionList
      },
      isVersionListEmpty() {
        return this.$store.state.version.versionList.length <= 0
        //return this.$store.state.section.sectionList.length <= 0
      },
      isSectionListEmpty() {
        return this.$store.state.section.sectionList.length <= 0
      },
      isLoadingSectionList() {
        return this.$store.state.section.sectionListStatus.isLoading
      },
      isSectionListApiError() {
        return this.$store.state.section.sectionListStatus.isApiError
      },
      sectionListApiErrorMsg() {
        return this.$store.state.section.sectionListStatus.apiErrorMsg
      },
      isLoadingDBMetaData() {
        return this.$store.state.dbMetaData.entitiesStatus.isLoading
      },
      versions() {
        if (this.getUpdatedVerList) {
          let list = Array.from(new Set(this.$store.state.version.versionList.map(v => v.date.split("-")[0])))
              .sort((a, b) => (a < b) ? 1 : -1);
          this.setDefaultValueForVersionList(list[0]);

          return list;
        }
      },
      isAuthorUploaded() {
        if (this.recordList.authorRecord) {
          //return "Author record uploaded";
          return true;
        } else {
          return false;
          //return "No author record found";
        }
      },
      isReviewUploaded() {
        if (this.recordList.reviewRecord) {
          return true;
          //return "Review record uploaded";
        } else {
          return false;
          //return "No review record found";
        }
      },
      isSubmissionUploaded() {
        if (this.recordList.submissionRecord) {
          return true;
          //return "Submission record uploaded";
        } else {
          return false;
          //return "No submission record found";
        }
      },
    },
    components: {
      AbstractSectionDetail,
      EmptySection
    },
    mounted() {
      this.$store.dispatch('fetchDBMetaDataEntities');
      this.$store.dispatch('getVersionList', this.$route.params.id).then(() => this.getUpdatedVerList = true);
      //this.fetchSectionList();


    },
    methods: {
      updateVersion() {
        //Value is year (will extract out ID)
        var value = this.presentationFormVersion;

        if (value === undefined) {
          value = this.versions[0];
        }

        //For loop to get ID
        for (var i = 0; i < this.$store.state.version.versionList.length; i++) {
          if (value === this.$store.state.version.versionList[i].date.split("-")[0]) {
            //Match!
            this.verId = this.$store.state.version.versionList[i].id;
            break;
          }
        }

        var id = this.verId;
        console.log("id is: " + id);
        this.$store.commit('setPresentationFormField', {
          field: 'version_id', id
        });

        this.$store.dispatch('getRecordList', id).then(() => {
          this.recordList = this.$store.state.record.recordList;
          console.log(this.$store.state.record.recordList);
        });

        this.fetchSectionList();

        this.$notify.info({
          title: 'Selected version',
          message: 'Currently selecting year ' + value + ' to visualize',
          duration: 2000
        });
      },

      setDefaultValueForVersionList(value) {
        this.presentationFormVersion = value;
      },

      fetchSectionList() {
        if (this.isNewPresentation) {
          this.$store.commit('clearSectionList');
        } else {
          this.$store.dispatch('fetchSectionList', this.verId);
        }
      },

      addNewSection() {
        if (this.selectedNewSection.length === 0) {

          this.$notify.error({
            title: 'Error',
            message: 'Please select a section to add into presentation.'
          });
          return;
        }
        this.$store.dispatch('addSectionDetail', {
          versionId: this.verId,
          selectedNewSection: this.selectedNewSection,
          dataSet: this.$store.state.userInfo.userEmail,
        }).then(() => {
          this.selectedNewSection = ''
        })

        this.$notify.success({
          title: 'Added new section',
          message: 'Successfully added a new section to visualize.',
          duration: 2000
        });
      }
    }
  }
</script>

<style scoped>
  .textBold {
    font-weight: bold;
  }
  .versionInput {
    display: inline-block;
    width: 100%;
  }
  .selectionInput {
    display: inline-block;
    width: 100%;
    margin-bottom: 16px;
  }
  .selectionInputButton {
    display: inline-block;
    width: 100%;
  }
  .versionInfo {
    height: 110%;
    display: inline-block;
  }
  .recordButton {
    float: right;
    display: inline-block;
    width: 100%;
    margin-bottom: 2px;

  }
  .addRowRightAlign {
    float: right;
    margin-top: 18px;
    margin-left: 16px;
  }
  .addRowRightAlign .el-card{
    margin-bottom: 16px;
  }
</style>