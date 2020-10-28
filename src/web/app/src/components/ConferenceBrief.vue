<template>
  <div>
    <el-card class="details-card">
      <div slot="header" class="clearfix">
        <span> Conference Details </span>
      </div>
      <el-row>
        <el-col :span="18">
          <el-form class="formStyle" label-position="left" ref="conferenceForm" label-width="150px" :rules="rules"
                   :model="conferenceForm" v-loading="isLoading" >
            <el-alert v-if="isError" :title="apiErrorMsg" type="error" show-icon class="errorMsg"/>

            <el-form-item label="Title: " :prop=" isInEditMode ? 'title' : ''" >
              <div v-if="!isInEditMode">{{ conferenceForm.title }}</div>
              <el-input v-model="conferenceFormTitle" v-if="isInEditMode"/>
            </el-form-item>
            <el-form-item label="Description: ">
              <div v-if="!isInEditMode" id="conference-description">{{ conferenceForm.description }}</div>
              <el-input type="textarea" autosize v-model="conferenceFormDescription" v-if="isInEditMode"/>
            </el-form-item>
            <el-form-item label="Creator: ">
              <div v-if="!isInEditMode">{{ conferenceForm.creatorIdentifier }}</div>
              <el-input type="textarea" autosize v-model="conferenceFormCreatorIdentifier" v-if="isInEditMode"/>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
      <el-divider></el-divider>
      <div class="options-section">
        <el-button-group>
          <el-button type="danger" v-if="!isNewConference && isLogin"
                    @click="deleteConference()">
            <i class="el-icon-delete"> Delete </i>
          </el-button>
        </el-button-group>
      </div>
    </el-card>
  </div>
</template>

<script>
  import {ID_NEW_CONFERENCE} from "@/common/const";

  export default {
    name: 'ConferenceBrief',
    props: {
      id: String
    },
    mounted() {
      this.updateConferenceForm();
    },
    watch: {
      'id'() {
        this.updateConferenceForm()
      },
    },
    computed: {
      isLogin() {
        return this.$store.state.userInfo.isLogin
      },
      conferenceForm() {
        return {
          title: this.conferenceFormTitle,
          creatorIdentifier: this.conferenceFormCreatorIdentifier,
          description: this.conferenceFormDescription,
        }
      },
      conferenceFormTitle: {
        get() {
          return this.$store.state.conference.conferenceForm.title
        },
        set(value) {
          this.$store.commit('setConferenceFormField', {
            field: 'title',
            value
          })
        },
      },
      conferenceFormCreatorIdentifier() {
        return this.$store.state.conference.conferenceForm.userEmail
        //return this.$store.state.conference.conferenceForm.creatorIdentifier
      },
      conferenceFormDescription: {
        get() {
          return this.$store.state.conference.conferenceForm.description
        },
        set(value) {
          this.$store.commit('setConferenceFormField', {
            field: 'description',
            value
          })
        },
      },
      isNewConference() {
        return this.id === ID_NEW_CONFERENCE
      },
      isInEditMode() {
        return this.isEditing || this.isNewConference
      },
      isLoading() {
        return this.$store.state.conference.conferenceFormStatus.isLoading
      },
      isError() {
        return this.$store.state.conference.conferenceFormStatus.isApiError
      },
      apiErrorMsg() {
        return this.$store.state.conference.conferenceFormStatus.apiErrorMsg
      }
    },
    data() {
      return {
        isEditing: false,
        rules: {
          name: [
            {required: true, message: 'Please enter conference name', trigger: 'blur'},
            {min: 3, message: 'The length should be more than 3 character', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      changeEditMode(isEditing) {
        if (isEditing === false) {
          this.updateConferenceForm();
        }
        this.isEditing = isEditing;
      },

      addConference() {
        this.$refs['conferenceForm'].validate((valid) => {
          if (!valid) {
            return
          }
          this.$refs['conferenceForm'].clearValidate();
          if (this.isNewConference) {
            // add
            this.$store.dispatch('saveConference')
                    .then(() => {
                      if (this.isError) {
                        return
                      }
                      // redirect to the newly added conference
                      this.$router.push({
                        name: 'conference',
                        params: {
                          id: this.$store.state.conference.conferenceForm.id
                        }
                      });
                    });
          } else {
            // edit
            this.$store.dispatch('updateConference')
                    .then(() => {
                      if (this.isError) {
                        return
                      }
                      this.isEditing = false
                    })
          }
        });
      },
      deleteConference() {
        this.$store.dispatch('deleteConference', this.id)
                .then(() => {
                  if (this.isError) {
                    return
                  }
                  this.$router.replace({
                    name: 'manage',
                    params: {
                      id: ID_NEW_CONFERENCE
                    }
                  });
                  this.isEditing = false;
                })
      },
      updateConferenceForm() {
        if (this.$refs['conferenceForm']) {
          this.$refs['conferenceForm'].clearValidate();
        }
        this.$store.commit('resetConferenceForm');
        if (this.id !== ID_NEW_CONFERENCE) {
          this.$store.dispatch('getConference', this.id)
        }
      },
    },

    components: {
    },
  }
</script>

<style scoped>
  .formStyle {
    display: inline-block;
    text-align: left;
    margin-right: 8px;
  }ß
   .errorMsg {
     margin-bottom: 18px;
   }
  .el-form-item__label {
    font-weight: bold;
  }
  .el-card {
    margin-bottom: 10px;
  }
  .details-card {
    overflow-x: auto;
  }
  .download-section {
    text-align:center;
    vertical-align:middle;
  }
  .options-section {
    text-align:center;
    vertical-align:middle;
    margin-top: 2rem;
  }
  .v-divide {
    float: left;
    height: 6rem;
  }
</style>
