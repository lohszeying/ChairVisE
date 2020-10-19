<template>
    <el-main>
        <el-card>
            <div slot="header" class="clearfix">
                <span> Create New Conference </span>
            </div>
            <el-alert v-if="isNewConference && !isLogin" title="Please login to create new conference" type="error" show-icon
                      class="errorMsg"/>
            <el-form v-else :rules="rules" ref="conferenceForm"
                     :model="conferenceForm" v-loading="isLoading">

                <el-form-item label="Name" :prop="'title'" >
                    <el-col>
                        <el-input v-model="conferenceFormTitle" placeholder="Enter name"/>
                    </el-col>
                </el-form-item>
                <el-form-item label="Description">
                    <el-col>
                        <el-input type="textarea" v-model="conferenceFormDescription" placeholder="Enter description"/>
                    </el-col>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-check" @click="uploadClicked()">Save</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <!-- dialogs -->
        <el-dialog
        title="Confirm"
        :visible.sync="hasSubmitted"
        width="30%" center>
            <span> Are you sure that the conference details are correct?</span>
            <span slot="footer" class="dialog-footer">
                <el-button v-on:click="hasSubmitted = false">Cancel</el-button>
                <el-button type="primary" v-on:click="addConference">Confirm</el-button>
            </span>
        </el-dialog>
        <el-dialog
        title="Success"
        :visible.sync="saveSuccess"
        width="30%" center>
            <span>You have successfully added a new conference</span>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" v-on:click="closeSuccess">Sure</el-button>
            </span>
        </el-dialog>
        <!-- end of dialogs -->
    </el-main>
</template>

<script>
    import {AccessLevel, ID_NEW_CONFERENCE, SPECIAL_IDENTIFIER_PUBLIC} from "@/common/const";

    export default {
        name: 'ConferenceBrief',
        props: {
            id: String
        },
        watch: {
            'id'() {
                this.updateConferenceForm()
            },
        },
        mounted() {
            this.updateConferenceForm();
            //this.$store.dispatch('getVersionList')
        },
        computed: {
            isLogin() {
                return this.$store.state.userInfo.isLogin
            },
            conferenceForm() {
                return {
                    name: this.conferenceFormTitle,
                    creatorIdentifier: this.conferenceFormCreatorIdentifier,
                    description: this.conferenceFormDescription,
                }
            },
            conferenceFormCreatorIdentifier() {
                return this.$store.state.conference.conferenceForm.creatorIdentifier
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
            saveSuccess() {
                return this.$store.state.conference.isSaveSuccess
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
                hasSubmitted: false,
                rules: {
                    name: [
                        {required: true, message: 'Please enter conference name', trigger: 'blur'},
                        {min: 3, message: 'The length should be more than 3 character', trigger: 'blur'}
                    ],
                }
            }
        },
        methods: {
            addConference() {
                this.hasSubmitted = false;
                this.$store.dispatch('saveConference').then(() => {
                    if (this.isNewConference && !this.isLogin) {
                        return
                    }
                })
            },
            updateConferenceForm() {
                if (this.$refs['conferenceForm']) {
                    this.$refs['conferenceForm'].clearValidate();
                }
                this.$store.commit('resetConferenceFormForm');
                if (this.id !== ID_NEW_CONFERENCE) {
                    this.$store.dispatch('getConference', this.id)
                        .then(() => {
                            // check writable or not
                            this.$store.dispatch('fetchAccessControlList', this.id)
                                .then(() => {
                                    let currentUser = this.$store.state.userInfo.userEmail;
                                    let accessControlList = this.$store.state.accessControl.accessControlList;
                                    let isPresentationEditable =
                                        currentUser === this.conferenceFormCreatorIdentifier
                                        || accessControlList.some(acl => acl.userIdentifier === currentUser && acl.permission === AccessLevel.CAN_WRITE)
                                        || accessControlList.some(acl => acl.userIdentifier === SPECIAL_IDENTIFIER_PUBLIC && acl.permission === AccessLevel.CAN_WRITE);
                                    this.$store.commit('setIsPresentationEditable', isPresentationEditable)
                                })
                        })
                } else {
                    this.$store.dispatch("getVersionList")
                }
            },
            uploadClicked() {
                this.$refs['conferenceForm'].validate((valid, object) => {
                    if (!valid) {
                        if('title' in object) {
                            this.$notify.error({
                                title: 'Error',
                                message: object.title[0].message
                            });
                        }
                        return
                    }
                    this.$refs['conferenceForm'].clearValidate();
                    this.hasSubmitted = true;
                });
            },
            closeSuccess() {
                this.$store.commit("setSaveSuccess", false);
                this.$router.push({
                    name: 'manage'
                });
            }
        },
        components: {
        },
    }
</script>

<style scoped>
    .errorMsg {
        margin-bottom: 18px;
    }
</style>
